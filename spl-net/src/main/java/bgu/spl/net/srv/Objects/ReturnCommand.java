package bgu.spl.net.srv.Objects;

import bgu.spl.net.srv.Command;
import bgu.spl.net.srv.CommandEncoderDecoder;

public abstract class ReturnCommand extends Command {
    public ReturnCommand(int opCode) {
        super(opCode);
    }

    abstract public byte[] encode(CommandEncoderDecoder c);
}
