package bgu.spl.net.srv.Objects;

import bgu.spl.net.srv.Command;
import bgu.spl.net.srv.CommandEncoderDecoder;

public class AckCommand extends ReturnCommand {

    private int msgOpCode;
    private String OptionalData;

    public AckCommand(int opCode) {
        super(opCode);
    }
    public void encodeNextByte(byte nextByte, CommandEncoderDecoder c) {}

    public int getMsgOpCode() {
        return msgOpCode;
    }

    public void setMsgOpCode(int msgOpCode) {
        this.msgOpCode = msgOpCode;
    }

    public String getOptionalData() {
        return OptionalData;
    }

    public void setOptionalData(String optionalData) {
        OptionalData = optionalData;
    }
}
