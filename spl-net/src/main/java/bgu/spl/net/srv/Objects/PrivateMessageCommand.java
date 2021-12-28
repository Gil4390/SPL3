package bgu.spl.net.srv.Objects;

import bgu.spl.net.srv.Command;

public class PrivateMessageCommand extends Command {
    private String senderName;
    private String contentName;
    private String sendingDate;

    public PrivateMessageCommand(int opCode) {
        super(opCode);
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getSendingDate() {
        return sendingDate;
    }

    public void setSendingDate(String sendingDate) {
        this.sendingDate = sendingDate;
    }
}
