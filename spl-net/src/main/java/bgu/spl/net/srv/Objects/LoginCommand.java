package bgu.spl.net.srv.Objects;

import bgu.spl.net.srv.Command;

public class LoginCommand extends Command {
    private String name;
    private String password;
    private int Captcha;

    public LoginCommand(int opCode) {
        super(opCode);
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCaptcha() {
        return Captcha;
    }

    public void setCaptcha(int captcha) {
        Captcha = captcha;
    }
}
