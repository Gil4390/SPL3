package bgu.spl.net.srv;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

public class User {
    final private int ID;
    final private String name;
    final private String password;
    final private String Birthday;
    private short numOfFollowing;
    private short numOfFollowers; //todo
    private short numPostedPost;

    public User(int id, String name, String password, String birthday) {
        this.ID = id;
        this.name = name;
        this.password = password;
        this.Birthday = birthday;
        this.numOfFollowing = 0;
        this.numOfFollowers = 0;
        this.numPostedPost=0;
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

    public short getNumOfFollowing() {
        return numOfFollowing;
    }

    public void setNumOfFollowing(short numOfFollowing) {
        this.numOfFollowing = numOfFollowing;
    }

    public short getNumOfFollowers() {
        return numOfFollowers;
    }

    public void setNumOfFollowers(short numOfFollowers) {
        this.numOfFollowers = numOfFollowers;
    }

    public short getNumPostedPost() {
        return numPostedPost;
    }

    public void setNumPostedPost(short numPostedPost) {
        this.numPostedPost = numPostedPost;
    }

    public int getAge(){
        LocalDate today = LocalDate.now();
        LocalDate birthday = LocalDate.parse(Birthday);
        Period p = Period.between(birthday, today);
        return p.getYears();
    }

    public String getStats(){
        return "" +  getAge() + numPostedPost + numOfFollowers + numOfFollowing;
    }
}
