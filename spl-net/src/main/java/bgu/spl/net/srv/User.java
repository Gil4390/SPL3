package bgu.spl.net.srv;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

public class User {
    final private int ID;
    final private String name;
    final private String password;
    final private String Birthday;
    private int numOfFollowing;
    private int numOfFollowers; //todo
    private int numPostedPost;
    private ConcurrentLinkedQueue<Message> unreadMsg;

    public User(int id, String name, String password, String birthday) {
        this.ID = id;
        this.name = name;
        this.password = password;
        this.Birthday = birthday;
        this.numOfFollowing = 0;
        this.numOfFollowers = 0;
        this.numPostedPost=0;
        unreadMsg = new ConcurrentLinkedQueue<>();
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthday() {
        return Birthday;
    }

    public int getID() {
        return ID;
    }

    public int getNumOfFollowing() {
        return numOfFollowing;
    }

    public void setNumOfFollowing(int numOfFollowing) {
        this.numOfFollowing = numOfFollowing;
    }

    public int getNumOfFollowers() {
        return numOfFollowers;
    }

    public void setNumOfFollowers(int numOfFollowers) {
        this.numOfFollowers = numOfFollowers;
    }

    public int getNumPostedPost() {
        return numPostedPost;
    }

    public void setNumPostedPost(int numPostedPost) {
        this.numPostedPost = numPostedPost;
    }

    public int getAge(){
//        LocalDate today = LocalDate.now();
//        LocalDate birthday = LocalDate.parse(Birthday);
//
//        Period p = Period.between(birthday, today);
//        return p.getYears();
        return 1;
    }

    public String getStats(){
        return "" +  getAge() +" "+ numPostedPost +" "+ numOfFollowers +" "+ numOfFollowing;
    }

    public synchronized ConcurrentLinkedQueue<Message> getUnreadMsgAndReset() {
        ConcurrentLinkedQueue<Message> tmp = this.unreadMsg;
        this.unreadMsg = new ConcurrentLinkedQueue<>();
        return tmp;
    }


    public synchronized void addUnreadMsg(Message msg){
        this.unreadMsg.add(msg);
    }
}
