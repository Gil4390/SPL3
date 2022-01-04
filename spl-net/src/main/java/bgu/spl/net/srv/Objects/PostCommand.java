package bgu.spl.net.srv.Objects;

import bgu.spl.net.srv.Command;
import bgu.spl.net.srv.CommandEncoderDecoder;

import java.util.List;
import java.util.Vector;

public class PostCommand extends ReceivedCommand {
    private String content;
    private String sendingDate;
    private String name;

    public PostCommand(int opCode) {
        super(opCode);
    }
    public void decodeNextByte(byte nextByte, CommandEncoderDecoder c) {
        c.decodeNextByte(nextByte,this);
    }
    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSendingDate() {
        return sendingDate;
    }

    public void setSendingDate(String sendingDate) {
        this.sendingDate = sendingDate;
    }

    public Vector<String> getMentionedUsers(){
        //todo
        return null;
    }
}
