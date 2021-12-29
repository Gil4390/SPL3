package bgu.spl.net.srv.Objects;

import bgu.spl.net.srv.Command;
import bgu.spl.net.srv.CommandEncoderDecoder;

public class NotificationCommand extends Command {
    private String type; //PM or Public
    private String postingUserName;
    private String content;

    public NotificationCommand(int opCode) {
        super(opCode);
    }

    public void decodeNextByte(byte nextByte, CommandEncoderDecoder c) {
        c.decodeNextByte(nextByte,this);
    }
    public String getType() {
        return type;
    }

    public String getPostingUserName() {
        return postingUserName;
    }

    public String getContent() {
        return content;
    }
}
