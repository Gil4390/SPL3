package bgu.spl.net.srv;

import bgu.spl.net.srv.Objects.*;
import bgu.spl.net.srv.Objects.RegisterCommand;

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
    private ConcurrentHashMap<String,List<String>> blockedUsers;
    private List<String>filteredWords;

    public Twitter() {
        UserID = 0;
        this.followers = new ConcurrentHashMap<>();
        this.users = new ConcurrentHashMap<>();
        privateMessages=new ConcurrentHashMap<>();
        blockedUsers= new ConcurrentHashMap<>();
    }

    public Vector<Command> Register(RegisterCommand cmd){
        Vector<Command> result = new Vector<>();
        if(users.containsKey(cmd.getName())){ //already registered
            ErrorCommand errcmd = (ErrorCommand) CommandFactory.makeReturnCommand(10);
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
            ackcmd.setOptionalData("Register Successful");
            result.add(ackcmd);
        }
        return result;
    }

    public void Login(LoginCommand command){
        if(users.containsKey(command.getName())) {
            ErrorCommand errcmd = (ErrorCommand) CommandFactory.makeReturnCommand(10);
            errcmd.setMsgOpCode(2);
            //return errcmd;
        }
        else {
            if (!users.get(command.getName()).getPassword().equals(command.getPassword())) {
                //todo send Error
            }
            else {
                if (checkLoggedIn(command.getName())) {
                    loggedIn.put(command.getName(),true);
                    //todo send Ack
                }
            }
        }
    }

    public Vector<Command> Logout(LogoutCommand cmd){
        Vector<Command> result = new Vector<>();
        if(loggedIn.contains(cmd.getName())){
            loggedIn.put(cmd.getName(),false);
            AckCommand ackcmd = (AckCommand) CommandFactory.makeReturnCommand(10);
            ackcmd.setMsgOpCode(3);
            ackcmd.setOptionalData("Logout Successful");
            result.add(ackcmd);
        }
        else{
            ErrorCommand errcmd = (ErrorCommand) CommandFactory.makeReturnCommand(10);
            errcmd.setMsgOpCode(3);
            result.add(errcmd);
        }
        return result;
    }

    public void Follow(FollowCommand command){
        if(checkLoggedIn(command.getClientName())) {
            if (followers.get(command.getClientName()).contains(command.getFollowName())) {
                //todo Error
            } else {
                if (!users.containsKey(command.getFollowName())) {
                    // todo send Error
                }
                else if(!BlockedUser(command.getClientName(),command.getFollowName())){
                    followers.get(command.getClientName()).add(command.getFollowName());
                }
            }
        }
    }

    public void UnFollow(FollowCommand command){
        if(checkLoggedIn(command.getClientName())) {
            if (!followers.get(command.getClientName()).contains(command.getFollowName())) {
                //todo Error
            } else {
                followers.get(command.getClientName()).remove(command.getFollowName());
            }
        }
    }

    public Vector<Command> Post(PostCommand cmd){
        Vector<Command> result = new Vector<>();
        if(loggedIn.containsKey(cmd.getName())){
            User user = users.get(cmd.getName());
            user.setNumPostedPost(user.getNumPostedPost()+1);

            Vector<String> usersToNotify = cmd.getMentionedUsers();
            for(String sUser : followers.get(cmd.getName())){
                if(!usersToNotify.contains(sUser))
                    usersToNotify.add(sUser);
            }
            for(String sUser : usersToNotify){
                if(!BlockedUser(cmd.getName(), sUser)) {
                    usersToNotify.remove(sUser);
                    NotificationCommand notificationCommand = (NotificationCommand) CommandFactory.makeReturnCommand(9);
                    notificationCommand.setType(1);
                    notificationCommand.setPostingUserName(cmd.getName());
                    notificationCommand.setContent(cmd.getContent());
                    result.add(notificationCommand);
                }
                else{//if the user mentioned someone that blocked them
                    user.setNumPostedPost(user.getNumPostedPost()-1);
                    result = new Vector<>();
                    ErrorCommand errcmd = (ErrorCommand) CommandFactory.makeReturnCommand(10);
                    errcmd.setMsgOpCode(5);
                    result.add(errcmd);
                    break;
                }
            }
        }
        else{
            ErrorCommand errcmd = (ErrorCommand) CommandFactory.makeReturnCommand(10);
            errcmd.setMsgOpCode(5);
            result.add(errcmd);
        }
        return result;
    }

    public void PrivateMessage(PrivateMessageCommand command){
        if(checkLoggedIn(command.getSenderName())){
            if(!users.containsKey(command.getReceiveName())){
                //todo send Error
            }
            else{
                if(!followers.get(command.getSenderName()).contains(command.getReceiveName())){
                    //todo send Error
                }
                else if(!BlockedUser(command.getReceiveName(),command.getSenderName())){
                    String filteredContent=command.getContent();
                    filteredContent=FilterString(filteredContent);
                    Message m = new Message(false,filteredContent);
                    if(privateMessages.containsKey(command.getSendingDate()))
                        privateMessages.get(command.getSendingDate()).add(m);
                    else{
                        List <Message> temp = new LinkedList<>();
                        temp.add(m);
                        privateMessages.put(command.getSendingDate(),temp);
                    }
                    //todo send notification?
                    //todo send Ack?
                }
            }
        }
    }

    public Vector<Command> LogStat(LogStatCommand cmd){
        Vector<Command> result = new Vector<>();
        if(loggedIn.containsKey(cmd.getName())){//if not logged in, then definitely not registered
            for(User user : users.values()){
                AckCommand ackcmd = (AckCommand) CommandFactory.makeReturnCommand(10);
                ackcmd.setMsgOpCode(7);
                ackcmd.setOptionalData(user.getStats());
                result.add(ackcmd);
            }
        }
        else{
            ErrorCommand errcmd = (ErrorCommand) CommandFactory.makeReturnCommand(10);
            errcmd.setMsgOpCode(7);
            result.add(errcmd);
        }
        return result;
    }

    public void Stats(StatsCommand command){
        if(!users.containsKey(command.getSenderName())){
            //todo send Error
        }
        else if(checkLoggedIn(command.getSenderName())){
            for (String user:command.getUserNameList()) {
                if(!users.containsKey(user)) {
                    //todo send error
                }
                if(!BlockedUser(command.getSenderName(),user)) {
                    //todo send ack
                }
            }
        }
    }

    public void Notify(NotificationCommand cmd){
        //todo
    }

    public void Block(BlockCommand command){
        if(!users.containsKey(command.getBlockedName())){
            //todo Error
        }
        else{//todo check if user have to be logged in
            if (users.containsKey(command.getClientName())) {
                followers.getOrDefault(command.getClientName(),new LinkedList<>()).remove(command.getBlockedName());
                followers.getOrDefault(command.getBlockedName(),new LinkedList<>()).remove(command.getClientName());
                if(!BlockedUser(command.getClientName(),command.getBlockedName())){
                    blockedUsers.get(command.getClientName()).add(command.getBlockedName());
                    blockedUsers.get(command.getBlockedName()).add(command.getClientName());
                }
            }
        }
    }

    private boolean checkLoggedIn(String name){
        if(!loggedIn.get(name)){
            //todo send Error
            return false;
        }
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
            filteredString = filteredString.replace(str,"");
        }
        return filteredString;
    }
}
