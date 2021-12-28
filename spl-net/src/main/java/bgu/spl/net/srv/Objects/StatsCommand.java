package bgu.spl.net.srv.Objects;

import bgu.spl.net.srv.Command;

import java.util.List;

public class StatsCommand extends Command {

    private String senderName;
    private List<String> userNameList;

    public StatsCommand(int opCode) {
        super(opCode);
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public List<String> getUserNameList() {
        return userNameList;
    }

    public void setUserNameList(List<String> userNameList) {
        this.userNameList = userNameList;
    }
}
