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
    private int numOfFollowing;
    private int numOfFollowers; //todo
    private int numPostedPost;

    public User(int id, String name, String password, String birthday) {
        this.ID = id;
        this.name = name;
        this.password = password;
        this.Birthday = birthday;
        this.numOfFollowing = 0;
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

    public int getNumOfFollowing() {
        return numOfFollowing;
    }

    public void setNumOfFollowing(int numOfFollowing) {
        this.numOfFollowing = numOfFollowing;
    }

    public int getID() {
        return ID;
    }

    public int getNumPostedPost() {
        return numPostedPost;
    }

    public void setNumPostedPost(int numPostedPost) {
        this.numPostedPost = numPostedPost;
    }

    public int getNumOfFollowers() {
        return numOfFollowers;
    }

    public void setNumOfFollowers(int numOfFollowers) {
        this.numOfFollowers = numOfFollowers;
    }

    public int getAge(){
        LocalDate today = LocalDate.now();
        LocalDate birthday = LocalDate.parse(Birthday);
        Period p = Period.between(birthday, today);
        return p.getYears();
    }

    public String getStats(){
        return "<" + getAge() + "><" + numPostedPost + "><" + numOfFollowers + "><" + "><" + numOfFollowing + ">";
    }
}
