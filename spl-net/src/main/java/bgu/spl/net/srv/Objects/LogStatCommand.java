package bgu.spl.net.srv.Objects;

import bgu.spl.net.srv.Command;

public class LogStatCommand extends Command {
    private String name;
    public LogStatCommand(int opCode, String name) {
        super(opCode);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
