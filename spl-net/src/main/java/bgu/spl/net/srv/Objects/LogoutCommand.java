package bgu.spl.net.srv.Objects;

import bgu.spl.net.srv.Command;

public class LogoutCommand extends Command {
    private String name;

    public LogoutCommand(int opCode, String name) {
        super(opCode);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
