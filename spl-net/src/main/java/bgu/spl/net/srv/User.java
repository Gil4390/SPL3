package bgu.spl.net.srv;

public class User {
    final private int ID;
    final private String name;
    final private String password;
    final private String Birthday;
    private int numOfFollowing;

    public User(int id, String name, String password, String birthday) {
        this.ID = id;
        this.name = name;
        this.password = password;
        this.Birthday = birthday;
        this.numOfFollowing = 0;
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

    public int getAge(){
        //todo
        return 0;
    }

    public String getStats(){
        //todo
        return "";
    }
}
