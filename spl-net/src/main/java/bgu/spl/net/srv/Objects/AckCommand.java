package bgu.spl.net.srv.Objects;

import bgu.spl.net.srv.Command;

public class AckCommand extends Command {

    public AckCommand(int opCode) {
        super(opCode);
    }
}
