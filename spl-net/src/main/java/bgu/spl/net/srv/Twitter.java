package bgu.spl.net.srv;

import bgu.spl.net.srv.Objects.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class Twitter {
    private int UserID;
    private ConcurrentHashMap<String, User> users;//registered users
    private ConcurrentHashMap<String, List<String>> followers;//all followers of a user
    private ConcurrentHashMap<String,Boolean> loggedIn;// if the user is loggedIn

    public Twitter() {
        UserID = 0;
        this.followers = new ConcurrentHashMap<>();
        this.users = new ConcurrentHashMap<>();
    }

    public void Register(RegisterCommand cmd){
        if(users.containsKey(cmd.getName())){
            //todo send ERROR
        }
        else{
            User user = new User(UserID++, cmd.getName(), cmd.getPassword(), cmd.getBirthday());
            users.put(user.getName(), user);
            followers.put(user.getName(),new LinkedList<>());
            loggedIn.put(user.getName(),false);
            //todo send ACK
        }
    }

    public void Login(LoginCommand command){
        if(users.containsKey(command.getName())) {
            //todo send Error
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

    public void Logout(LogoutCommand cmd){
        if(loggedIn.contains(cmd.getName())){
            loggedIn.remove(cmd.getName());
            users.remove(cmd.getName());
            for(String sUser : followers.get(cmd.getName())){
                User user = users.get(sUser);
                user.setNumOfFollowing(user.getNumOfFollowing()-1);

            }
        }
        else{
            //todo ERROR
        }
    }

    public void Follow(FollowCommand command){
        if(checkLoggedIn(command.getClientName())) {
            if (followers.get(command.getClientName()).contains(command.getFollowName())) {
                //todo Error
            } else {//todo check follow name is registered
                if (!users.containsKey(command.getFollowName())) {
                    // todo send Error
                }
                else {
                    followers.get(command.getClientName()).add(command.getFollowName());
                }
            }
        }
    }

    public void UnFollow(UnFollowCommand command){
        if(checkLoggedIn(command.getClientName())) {
            if (!followers.get(command.getClientName()).contains(command.getUnFollowName())) {
                //todo Error
            } else {
                followers.get(command.getClientName()).remove(command.getUnFollowName());
            }
        }
    }

    public void Post(PostCommand cmd){
        if(users.containsKey(cmd.getName())){
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

    public void PrivateMessage(PrivateMessageCommand command){
        if(checkLoggedIn(command.getSenderName())){
            if(!users.containsKey(command.getContentName())){
                //todo send Error
            }
            else{
                if(!followers.get(command.getSenderName()).contains(command.getContentName())){
                    //todo send Error
                }
                else{
                    //todo save the pm, filtered it
                    // todo send Ack
                }
            }
        }
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

    public void Stats(StatsCommand command){
        if(!users.containsKey(command.getSenderName())){
            //todo send Error
        }
        else if(checkLoggedIn(command.getSenderName())){
            // todo implement this
        }
    }

    public void Notify(NotificationCommand cmd){
        //todo
    }

    public void Block(BlockCommand command){
        if(!users.containsKey(command.getBlockedName())){
            //todo Error
        }
        else{
            if (users.containsKey(command.getClientName())) {
                followers.getOrDefault(command.getClientName(),new LinkedList<>()).remove(command.getBlockedName());
                followers.getOrDefault(command.getBlockedName(),new LinkedList<>()).remove(command.getClientName());
                // todo add both to blocked users
            }
        }
    }

    private boolean checkLoggedIn(String name){
        if(loggedIn.get(name)){
            //todo send Error
            return false;
        }
        return true;
    }
}
