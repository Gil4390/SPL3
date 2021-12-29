package bgu.spl.net.srv.Objects;

import bgu.spl.net.srv.Command;
import bgu.spl.net.srv.CommandEncoderDecoder;

public class AckCommand extends Command {

    public AckCommand(int opCode) {
        super(opCode);
    }
    public void decodeNextByte(byte nextByte, CommandEncoderDecoder c) {}
}
