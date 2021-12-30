package bgu.spl.net.srv.Objects;

import bgu.spl.net.srv.Command;
import bgu.spl.net.srv.CommandEncoderDecoder;

public abstract class ReceivedCommand extends Command {
    public ReceivedCommand(int opCode) {
        super(opCode);
    }

    abstract public void decodeNextByte(byte nextByte, CommandEncoderDecoder c);
}
