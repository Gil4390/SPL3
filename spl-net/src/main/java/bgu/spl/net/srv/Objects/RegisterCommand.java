package bgu.spl.net.srv.Objects;

import bgu.spl.net.srv.Command;

public class RegisterCommand extends Command {
    private String name;
    private String password;
    private String Birthday;

    public RegisterCommand(int opCode) {
        super(opCode);
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

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }
}
