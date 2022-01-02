package bgu.spl.net.srv;

public class Message {
    final private int type; //PM-0 or Public-1
    final private String content;
    final private String senderName;

    public Message(int type, String content, String senderName){
        this.type=type;
        this.content=content;
        this.senderName = senderName;
    }

    public int getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getSenderName() {
        return senderName;
    }
}
