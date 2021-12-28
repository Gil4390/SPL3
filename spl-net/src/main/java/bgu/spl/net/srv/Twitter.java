package bgu.spl.net.srv;

import bgu.spl.net.srv.Objects.RegisterCommand;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Twitter {
    private int UserID;
    private ConcurrentHashMap<String, User> users;
    private ConcurrentHashMap<String, List<String>> followers;

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

}
