package bgu.spl.net.srv;

import bgu.spl.net.srv.Objects.*;
import bgu.spl.net.srv.Objects.RegisterCommand;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Twitter {
    private int UserID;
    private ConcurrentHashMap<String, User> users;//registered users
    private ConcurrentHashMap<String, List<String>> followers;//all followers of a user
    private ConcurrentHashMap<String,Boolean>loggedIn;// if the user is loggedIn
    private List<Message> privateMessages;
    private List<String>filteredWords;

    public Twitter() {
        UserID = 0;
        this.followers = new ConcurrentHashMap<>();
        this.users = new ConcurrentHashMap<>();
        privateMessages=new LinkedList<>();
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

    public void Follow(FollowCommand command){
        if(checkLoggedIn(command.getClientName())) {
            if (followers.get(command.getClientName()).contains(command.getFollowName())) {
                //todo Error
            }
            else {
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
                    String filterdContent=command.getContent();
                    //message
                    //todo save the pm, filtered it
                    // todo send Ack
                }
            }
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
