package bgu.spl.net.srv.Objects;

import bgu.spl.net.srv.Command;
import bgu.spl.net.srv.CommandEncoderDecoder;

public abstract class ReceivedCommand extends Command {
    private int senderId;

    public ReceivedCommand(int opCode) {
        super(opCode);
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    abstract public void decodeNextByte(byte nextByte, CommandEncoderDecoder c);
}
