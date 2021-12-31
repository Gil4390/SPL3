package bgu.spl.net.srv;

public class Message {
    final private boolean postMessage;
    final private String content;

    public Message(boolean postMessage, String content){
        this.postMessage=postMessage;
        this.content=content;
    }

    public boolean isPostMessage() {
        return postMessage;
    }

    public String getContent() {
        return content;
    }
}
