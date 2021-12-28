package bgu.spl.net.srv.Objects;

import bgu.spl.net.srv.Command;

public class UnFollowCommand extends Command {

    private String unFollowName;
    private String clientName;

    public UnFollowCommand(int opCode) {
        super(opCode);
    }

    public String getUnFollowName() {
        return unFollowName;
    }

    public void setUnFollowName(String unFollowName) {
        this.unFollowName = unFollowName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
