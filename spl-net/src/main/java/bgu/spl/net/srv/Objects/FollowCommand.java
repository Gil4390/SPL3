package bgu.spl.net.srv.Objects;

import bgu.spl.net.srv.Command;

public class FollowCommand extends Command {
    private String followName;
    private String clientName;

    public FollowCommand(int opCode) {
        super(opCode);
    }

    public String getFollowName() {
        return followName;
    }

    public void setFollowName(String followName) {
        this.followName = followName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
