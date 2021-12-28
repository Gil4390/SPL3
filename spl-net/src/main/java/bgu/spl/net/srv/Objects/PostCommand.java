package bgu.spl.net.srv.Objects;

import bgu.spl.net.srv.Command;

import java.util.List;

public class PostCommand extends Command {
    final private String content;
    final private String name;

    public PostCommand(int opCode, String content, String name) {
        super(opCode);
        this.content = content;
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public List<String> getMentionedUsers(){
        //todo
        return null;
    }
}
