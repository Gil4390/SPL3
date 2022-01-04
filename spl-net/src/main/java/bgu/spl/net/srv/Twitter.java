package bgu.spl.net.srv;

import bgu.spl.net.srv.Objects.*;
import bgu.spl.net.srv.Objects.RegisterCommand;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class Twitter {
    private int UserID;
    private ConcurrentHashMap<String, User> users;//registered users
    private ConcurrentHashMap<String, List<String>> followers;//all followers of a user
    private ConcurrentHashMap<String,Boolean>loggedIn;// if the user is loggedIn
    private ConcurrentHashMap<String,List<Message>> privateMessages;
    private ConcurrentHashMap<String,List<Message>> postMessages;
    private ConcurrentHashMap<String,List<String>> blockedUsers;
    private List<String>filteredWords;

    public Twitter() {
        UserID = 0;
        followers = new ConcurrentHashMap<>();
        users = new ConcurrentHashMap<>();
        privateMessages=new ConcurrentHashMap<>();
        postMessages = new ConcurrentHashMap<>();
        blockedUsers= new ConcurrentHashMap<>();
        loggedIn = new ConcurrentHashMap<>();
        filteredWords = new Vector<>();
    }

    public Vector<ReturnCommand> Register(RegisterCommand cmd){
        Vector<ReturnCommand> result = new Vector<>();
        if(users.containsKey(cmd.getName())){ //already registered
            ErrorCommand errcmd = (ErrorCommand) CommandFactory.makeReturnCommand(11);
            errcmd.setMsgOpCode(1);
            result.add(errcmd);
        }
        else{
            User user = new User(UserID++, cmd.getName(), cmd.getPassword(), cmd.getBirthday());
            users.put(user.getName(), user);
            followers.put(user.getName(),new LinkedList<>());
            loggedIn.put(user.getName(),false);
            blockedUsers.put(user.getName(),new LinkedList<>());

            AckCommand ackcmd = (AckCommand) CommandFactory.makeReturnCommand(10);
            ackcmd.setMsgOpCode(1);
            result.add(ackcmd);
        }
        return result;
    }

    public Vector<ReturnCommand> Login(LoginCommand command){
        Vector<ReturnCommand> result = new Vector<>();
        if(!checkLoggedIn(command.getName()) && users.containsKey(command.getName()) && command.getCaptcha() == 1) {
            synchronized (users.get(command.getName())) {
                if (users.get(command.getName()).getPassword().equals(command.getPassword())) {
                    if (!checkLoggedIn(command.getName())) {
                        loggedIn.put(command.getName(), true);
                        AckCommand ack = new AckCommand(10);
                        ack.setMsgOpCode(command.getOpCode());
                        result.add(ack);

                        User user = users.get(command.getName());
                        for (Message msg : user.getUnreadMsgAndReset()) {
                            NotificationCommand notificationCmd = new NotificationCommand(9);
                            if(msg.getType() == 0)
                                notificationCmd.setContent(msg.getContent()+" "+ msg.getSendingDate());
                            else
                                notificationCmd.setContent(msg.getContent());
                            notificationCmd.setPostingUserName(msg.getSenderName());
                            notificationCmd.setType(msg.getType());
                            result.add(notificationCmd);
                        }

                        return result;
                    }
                }
            }
        }
        ErrorCommand errcmd = (ErrorCommand) CommandFactory.makeReturnCommand(11);
        errcmd.setMsgOpCode(2);
        result.add(errcmd);
        return result;
    }

    public Vector<ReturnCommand> Logout(LogoutCommand cmd){
        Vector<ReturnCommand> result = new Vector<>();
        if(loggedIn.contains(cmd.getName())){
            loggedIn.put(cmd.getName(),false);
            AckCommand ackcmd = (AckCommand) CommandFactory.makeReturnCommand(10);
            ackcmd.setMsgOpCode(3);
            result.add(ackcmd);
        }
        else{
            ErrorCommand errcmd = (ErrorCommand) CommandFactory.makeReturnCommand(11);
            errcmd.setMsgOpCode(3);
            result.add(errcmd);
        }
        return result;
    }

    public Vector<ReturnCommand> Follow(FollowCommand command){
        Vector<ReturnCommand> result = new Vector<>();
        if(checkLoggedIn(command.getClientName())){
            if (!followers.get(command.getClientName()).contains(command.getFollowName())) {
                if (users.containsKey(command.getFollowName()) || !BlockedUser(command.getClientName(),command.getFollowName())) {
                    followers.get(command.getClientName()).add(command.getFollowName());
                    User user = users.get(command.getClientName());
                    user.setNumOfFollowers((user.getNumOfFollowers()+1));
                    AckCommand ack = new AckCommand(10);
                    ack.setMsgOpCode(command.getOpCode());
                    ack.setOptionalData(command.getFollowName()+"0");//todo amen
                    result.add(ack);
                    return result;
                }
            }
        }
        ErrorCommand errcmd = (ErrorCommand) CommandFactory.makeReturnCommand(11);
        errcmd.setMsgOpCode(4);
        result.add(errcmd);
        return result;
    }

    public Vector<ReturnCommand> UnFollow(FollowCommand command){
        Vector<ReturnCommand> result = new Vector<>();
        if(checkLoggedIn(command.getClientName())) {
            if (followers.get(command.getClientName()).contains(command.getFollowName())) {
                followers.get(command.getClientName()).remove(command.getFollowName());
                User user = users.get(command.getClientName());
                user.setNumOfFollowers((user.getNumOfFollowers()-1));
                AckCommand ack = new AckCommand(10);
                ack.setMsgOpCode(command.getOpCode());
                ack.setOptionalData(command.getFollowName()+"0");
                result.add(ack);
                return result;
            }
        }
        ErrorCommand errcmd = (ErrorCommand) CommandFactory.makeReturnCommand(11);
        errcmd.setMsgOpCode(4);
        result.add(errcmd);
        return result;
    }

    public Vector<ReturnCommand> Post(PostCommand cmd){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        cmd.setSendingDate(formatter.format(date));

        //todo save post message
        //todo only add the notification of the receiving user is logged in
        //todo sync users.get(senderName) with logIn and PM
        Vector<ReturnCommand> result = new Vector<>();
        if(loggedIn.containsKey(cmd.getName())){
            String filteredContent=cmd.getContent();
            filteredContent=FilterString(filteredContent);
            Message m = new Message(0,filteredContent, cmd.getName(), cmd.getSendingDate());
            if(privateMessages.containsKey(cmd.getSendingDate()))
                privateMessages.get(cmd.getSendingDate()).add(m);
            else{
                List<Message> temp = new LinkedList<>();
                temp.add(m);
                privateMessages.put(cmd.getSendingDate(),temp);
            }
            AckCommand ack = new AckCommand(10);
            ack.setMsgOpCode(cmd.getOpCode());
            result.add(ack);

            User user = users.get(cmd.getName());
            user.setNumPostedPost((user.getNumPostedPost()+1));

            Vector<String> usersToNotify = cmd.getMentionedUsers();
            for(String sUser : followers.get(cmd.getName())){
                if(!usersToNotify.contains(sUser))
                    usersToNotify.add(sUser);
            }
            for(String sUser : usersToNotify){
                if(users.containsKey(sUser) && !BlockedUser(cmd.getName(), sUser)) {
                    usersToNotify.remove(sUser);
                    NotificationCommand notificationCommand = (NotificationCommand) CommandFactory.makeReturnCommand(9);
                    notificationCommand.setType(1);
                    notificationCommand.setPostingUserName(cmd.getName());
                    notificationCommand.setContent(cmd.getContent());
                    notificationCommand.setDestUserID(users.get(sUser).getID());
                    result.add(notificationCommand);
                }
                else{//if the user mentioned someone that blocked them
                    user.setNumPostedPost((user.getNumPostedPost()-1));
                    result = new Vector<>();
                    ErrorCommand errcmd = (ErrorCommand) CommandFactory.makeReturnCommand(11);
                    errcmd.setMsgOpCode(5);
                    result.add(errcmd);
                    break;
                }
            }
        }
        else{
            ErrorCommand errcmd = (ErrorCommand) CommandFactory.makeReturnCommand(11);
            errcmd.setMsgOpCode(5);
            result.add(errcmd);
        }
        return result;
    }

    public Vector<ReturnCommand> PrivateMessage(PrivateMessageCommand command){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        command.setSendingDate(formatter.format(date));

        Vector<ReturnCommand> result = new Vector<>();
        if(checkLoggedIn(command.getSenderName())){
            if(users.containsKey(command.getReceiveName())){
                if(followers.get(command.getSenderName()).contains(command.getReceiveName())
                        || !BlockedUser(command.getReceiveName(),command.getSenderName())){
                    String filteredContent=command.getContent();
                    filteredContent=FilterString(filteredContent);
                    Message m = new Message(0,filteredContent, command.getSenderName(), command.getSendingDate());
                    if(privateMessages.containsKey(command.getSendingDate()))
                        privateMessages.get(command.getSendingDate()).add(m);
                    else{
                        List <Message> temp = new LinkedList<>();
                        temp.add(m);
                        privateMessages.put(command.getSendingDate(),temp);
                    }
                    AckCommand ack = new AckCommand(10);
                    ack.setMsgOpCode(command.getOpCode());
                    result.add(ack);
                    NotificationCommand notCommand = new NotificationCommand(command.getOpCode());
                    notCommand.setType(0);
                    notCommand.setPostingUserName(command.getSenderName());
                    notCommand.setContent(command.getContent()+" "+command.getSendingDate());
                    notCommand.setDestUserID(users.get(command.getReceiveName()).getID());
                    synchronized (users.get(command.getSenderName())) {
                        if (!checkLoggedIn(command.getReceiveName())) {
                            Message mes = new Message(0, command.getContent(), command.getSenderName(), command.getSendingDate());
                            users.get(command.getReceiveName()).addUnreadMsg(mes);
                            notCommand.setDestUserID(users.get(command.getReceiveName()).getID());
                        }
                    }

                    result.add(notCommand);
                    return result;
                }
            }
        }
        ErrorCommand errcmd = (ErrorCommand) CommandFactory.makeReturnCommand(11);
        errcmd.setMsgOpCode(6);
        result.add(errcmd);
        return result;
    }

    public Vector<ReturnCommand> LogStat(LogStatCommand cmd){
        Vector<ReturnCommand> result = new Vector<>();
        if(loggedIn.containsKey(cmd.getName())){//if not logged in, then definitely not registered
            for(User user : users.values()){
                if(!blockedUsers.get(user.getName()).contains(cmd.getName())){
                    AckCommand ackcmd = (AckCommand) CommandFactory.makeReturnCommand(10);
                    ackcmd.setMsgOpCode(7);
                    ackcmd.setOptionalData(user.getStats());
                    result.add(ackcmd);
                }
            }
        }
        else{
            ErrorCommand errcmd = (ErrorCommand) CommandFactory.makeReturnCommand(11);
            errcmd.setMsgOpCode(7);
            result.add(errcmd);
        }
        return result;
    }

    public Vector<ReturnCommand> Stats(StatsCommand command){
        Vector<ReturnCommand> result = new Vector<>();
        if(checkLoggedIn(command.getSenderName())){
            for (String user:command.getUserNameList()) {
                if(!users.containsKey(user) || BlockedUser(command.getSenderName(),user)) {
                    ErrorCommand errcmd = (ErrorCommand) CommandFactory.makeReturnCommand(10);
                    errcmd.setMsgOpCode(8);
                    result.add(errcmd);
                    return result;
                }
            }
            for (int i=0; i<command.getUserNameList().size();i++){
                AckCommand ack = new AckCommand(10);
                ack.setMsgOpCode(command.getOpCode());
                User user = users.get(command.getUserNameList().get(i));
                ack.setOptionalData(user.getStats());
                result.add(ack);
            }
        }
        else{
            ErrorCommand errcmd = (ErrorCommand) CommandFactory.makeReturnCommand(11);
            errcmd.setMsgOpCode(8);
            result.add(errcmd);
        }
        return result;
    }

    public Vector<ReturnCommand> Block(BlockCommand command){
        Vector<ReturnCommand> result = new Vector<>();
        if(users.containsKey(command.getBlockedName())){
            User user_1 = users.get(command.getClientName());
            User user_2 = users.get(command.getBlockedName());
            if(followers.get(user_1.getName()).contains(user_2.getName())){
                followers.get(user_1.getName()).remove(user_2.getName());
                user_1.setNumOfFollowers(user_1.getNumOfFollowers()-1);
                user_2.setNumOfFollowing(user_2.getNumOfFollowing()-1);
            }
            if(followers.get(user_2.getName()).contains(user_1.getName())){
                followers.get(user_2.getName()).remove(user_1.getName());
                user_2.setNumOfFollowers(user_2.getNumOfFollowers()-1);
                user_1.setNumOfFollowing(user_1.getNumOfFollowing()-1);
            }
            if(!BlockedUser(command.getClientName(),command.getBlockedName())) {

                blockedUsers.get(command.getClientName()).add(command.getBlockedName());
                blockedUsers.get(command.getBlockedName()).add(command.getClientName());
                AckCommand ack = new AckCommand(10);
                ack.setMsgOpCode(command.getOpCode());
                result.add(ack);
                return result;
            }
        }
        ErrorCommand errcmd = (ErrorCommand) CommandFactory.makeReturnCommand(11);
        errcmd.setMsgOpCode(12);
        result.add(errcmd);
        return result;
    }

    private boolean checkLoggedIn(String name){
        if(!users.containsKey(name) || !loggedIn.get(name))
            return false;
        return true;
    }

    private boolean BlockedUser(String client, String client2){
        for (String str : blockedUsers.get(client)) {
            if(client2==str)
                return true;
        }
        return false;
    }

    private String FilterString(String unFilteredStr){
        String filteredString=unFilteredStr;
        for (String str:filteredWords) {
            filteredString = filteredString.replace(str,"<filtered>");
        }
        return filteredString;
    }

    public User getUserByName(String name){
        return users.get(name);
    }
}
