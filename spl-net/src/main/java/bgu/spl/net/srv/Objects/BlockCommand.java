package bgu.spl.net.srv.Objects;

import bgu.spl.net.srv.Command;

public class BlockCommand extends Command {
    private String clientName;
    private String BlockedName;

    public BlockCommand(int opCode) {
        super(opCode);
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getBlockedName() {
        return BlockedName;
    }

    public void setBlockedName(String blockedName) {
        BlockedName = blockedName;
    }
}
