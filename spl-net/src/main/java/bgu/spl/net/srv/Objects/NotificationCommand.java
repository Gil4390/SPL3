package bgu.spl.net.srv.Objects;

import bgu.spl.net.srv.Command;

public class NotificationCommand extends Command {
    private String type; //PM or Public
    private String postingUserName;
    private String content;

    public NotificationCommand(int opCode, String type, String postingUserName, String content) {
        super(opCode);
        this.type = type;
        this.postingUserName = postingUserName;
        this.content = content;
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
