package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.Objects.*;

public class Protocol implements BidiMessagingProtocol {
    private Twitter twit;
    private Connections connections;
    private boolean shouldTerminate;
    private int connectionId;

    public Protocol(Twitter twit){
        this.twit=twit;
        shouldTerminate = false;
    }

    @Override
    public void start(int connectionId, Connections connections) {
        this.connections = connections;
        this.connectionId=connectionId;
    }

    @Override
    public void process(Object message) {
        Command com = (Command) message;
        switch (com.getOpCode()){
            case 1:
                twit.Register(((RegisterCommand)com));
                break;
            case 2:
                twit.Login(((LoginCommand)com));
                break;
            case 3:
                twit.Logout(((LogoutCommand)com));
                shouldTerminate=true; // todo check if this happened after client receive ack message
                break;
            case 4:
                twit.Follow(((FollowCommand)com));
                break;
            case 5:
                twit.Post(((PostCommand)com));
                break;
            case 6:
                twit.PrivateMessage(((PrivateMessageCommand)com));
                break;
            case 7:
                twit.LogStat(((LogStatCommand)com));
                break;
            case 8:
                twit.Stats(((StatsCommand)com));
                break;
            case 12:
                twit.Block(((BlockCommand)com));
                break;
            default:
                System.out.println("error in protocol process");
        }
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
