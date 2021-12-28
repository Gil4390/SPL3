package bgu.spl.net.srv;

import bgu.spl.net.srv.Objects.*;
import bgu.spl.net.srv.Objects.RegisterCommand;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Twitter {
    private int UserID;
    private ConcurrentHashMap<String, User> users;//registered users
    private ConcurrentHashMap<String, List<String>> followers;//all followers of a user
    private ConcurrentHashMap<String,Boolean>loggedIn;// if the user is loggedIn

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
            //todo send ACK
        }
    }

    public void Login(LoginCommand command){
        if(users.containsKey(command.getName())) {
            //todo send Error
        }
        else {
            if (users.get(command.getName()).getPassword() != command.getPassword()) {
                //todo send Error
            }
            else {
                if (loggedIn.get(command.getName())) {
                    //todo send Error
                }
                else {
                    //todo send Ack
                }
            }
        }
    }

    public void Follow(){

    }

    public void Unfollow(){

    }

}
