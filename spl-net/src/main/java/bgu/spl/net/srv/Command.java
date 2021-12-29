package bgu.spl.net.srv;

public abstract class Command {
    protected int opCode;

    public Command(int opCode) {
        this.opCode = opCode;
    }

    abstract public void decodeNextByte(byte nextByte,CommandEncoderDecoder c);
}
