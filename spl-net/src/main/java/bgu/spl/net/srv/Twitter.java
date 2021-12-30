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

    public Command Register(RegisterCommand cmd){
        if(users.containsKey(cmd.getName())){
            ErrorCommand errcmd = (ErrorCommand) CommandFactory.makeReturnCommand(10);
            errcmd.setMsgOpCode(1);
            return errcmd;
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
            return ackcmd;
        }
    }

    public ReturnCommand Login(LoginCommand command){
        if(!checkLoggedIn(command.getName())) {
            if (users.get(command.getName()).getPassword().equals(command.getPassword())) {
                if (checkLoggedIn(command.getName())) {
                    loggedIn.put(command.getName(),true);
                    AckCommand ack = new AckCommand(10);
                    ack.setMsgOpCode(command.getOpCode());
                    return ack;
                }
            }
        }
        return new ErrorCommand(command.getOpCode());
    }

    public void Logout(LogoutCommand cmd){
        if(loggedIn.contains(cmd.getName())){
            loggedIn.put(cmd.getName(),false);
        }
        else{
            //todo ERROR
        }
    }

    public ReturnCommand Follow(FollowCommand command){
        if(checkLoggedIn(command.getClientName())){
            if (!followers.get(command.getClientName()).contains(command.getFollowName())) {
                if (users.containsKey(command.getFollowName()) || !BlockedUser(command.getClientName(),command.getFollowName())) {
                    followers.get(command.getClientName()).add(command.getFollowName());
                    AckCommand ack = new AckCommand(10);
                    ack.setMsgOpCode(command.getOpCode());
                    ack.setOptionalData(command.getFollowName()+"0");
                    return ack;
                }
            }
        }
        return new ErrorCommand(command.getOpCode());
    }

    public ReturnCommand UnFollow(FollowCommand command){
        if(checkLoggedIn(command.getClientName())) {
            if (followers.get(command.getClientName()).contains(command.getFollowName())) {
                followers.get(command.getClientName()).remove(command.getFollowName());
                AckCommand ack = new AckCommand(10);
                ack.setMsgOpCode(command.getOpCode());
                ack.setOptionalData(command.getFollowName());
                return ack;
            }
        }
        return new ErrorCommand(command.getOpCode());
    }

    public void Post(PostCommand cmd){//todo ask gil if implement blocked user?
        if(checkLoggedIn(cmd.getName())){
            for(String sUser : followers.get(cmd.getName())){
                User user = users.get(sUser);
                //todo send post request to followers
            }
            for(String sUser : cmd.getMentionedUsers()){
                User user = users.get(sUser);
                //todo send post request to users
            }
        }
    }

    public ReturnCommand [] PrivateMessage(PrivateMessageCommand command){
        ReturnCommand [] answer = new ReturnCommand[2];
        if(checkLoggedIn(command.getSenderName())){
            if(users.containsKey(command.getReceiveName())){
                if(followers.get(command.getSenderName()).contains(command.getReceiveName())
                        || !BlockedUser(command.getReceiveName(),command.getSenderName())){
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
                    AckCommand ack = new AckCommand(10);
                    ack.setMsgOpCode(command.getOpCode());
                    answer[0]=ack;
                    NotificationCommand notCommand = new NotificationCommand(command.getOpCode());
                    notCommand.setType(0);
                    notCommand.setPostingUserName(command.getSenderName());
                    notCommand.setContent(command.getContent());
                    answer[1]=notCommand;
                    return answer;
                }
            }
        }
        answer[0] = new ErrorCommand(command.getOpCode());
        answer[1]= null;
        return answer;
    }

    public void LogStat(LogStatCommand cmd){
        if(checkLoggedIn(cmd.getName())){
            List<String> output = new Vector<>();
            for(User user : users.values()){
                output.add(user.getStats());
            }
            //todo send output to user
        }
    }

    public ReturnCommand [] Stats(StatsCommand command){
        ReturnCommand [] answer = new ReturnCommand[1];
        answer[0] = new ErrorCommand(command.getOpCode());
        if(checkLoggedIn(command.getSenderName())){
            for (String user:command.getUserNameList()) {
                if(!users.containsKey(user) || BlockedUser(command.getSenderName(),user)) {
                    answer[0] = new ErrorCommand(command.getOpCode());
                    return answer;
                }
            }
            answer = new ReturnCommand[command.getUserNameList().size()];
            for (int i=0; i<command.getUserNameList().size();i++){
                AckCommand ack = new AckCommand(10);
                ack.setMsgOpCode(command.getOpCode());
                User user = users.get(command.getUserNameList().get(i));
                ack.setOptionalData(","+user.getAge()+","+user.getNumPostedPost()+","+user.getNumOfFollowing()+","+followers.get(user.getName()).size());
                answer[0] = ack;
            }
            return answer;
        }
        return answer;
    }

    public void Notify(NotificationCommand cmd){
        //todo
    }

    public ReturnCommand Block(BlockCommand command){
        if(users.containsKey(command.getBlockedName())){
            followers.getOrDefault(command.getClientName(),new LinkedList<>()).remove(command.getBlockedName());
            followers.getOrDefault(command.getBlockedName(),new LinkedList<>()).remove(command.getClientName());
            if(!BlockedUser(command.getClientName(),command.getBlockedName())) {
                blockedUsers.get(command.getClientName()).add(command.getBlockedName());
                blockedUsers.get(command.getBlockedName()).add(command.getClientName());
                AckCommand ack = new AckCommand(10);
                ack.setMsgOpCode(command.getOpCode());
                return ack;
            }
        }
        return new ErrorCommand(command.getOpCode());
    }

    private boolean checkLoggedIn(String name){
        if(!users.contains(name) || !loggedIn.get(name))
            return false;
        return true;
    }

    private boolean BlockedUser(String client, String client2){
        for (String str:blockedUsers.get(client)) {
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
}
