package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.Objects.*;

import java.util.Vector;

public class Protocol implements BidiMessagingProtocol<Command> {
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
    public void process(Command message) {
        Command com = message;
        Vector<Command> processedMessage = new Vector<>();
        switch (com.getOpCode()){
            case 1:
                processedMessage = twit.Register(((RegisterCommand)com));
                break;
            case 2:
                processedMessage = twit.Login(((LoginCommand)com));
                break;
            case 3:
                processedMessage = twit.Logout(((LogoutCommand)com));
                shouldTerminate=true; // todo check if this happened after client receive ack message
                break;
            case 4:
                processedMessage = twit.Follow(((FollowCommand)com));
                break;
            case 5:
                processedMessage = twit.Post(((PostCommand)com));
                break;
            case 6:
                processedMessage = twit.PrivateMessage(((PrivateMessageCommand)com));
                break;
            case 7:
                processedMessage = twit.LogStat(((LogStatCommand)com));
                break;
            case 8:
                processedMessage = twit.Stats(((StatsCommand)com));
                break;
            case 12:
                processedMessage = twit.Block(((BlockCommand)com));
                break;
            default:
                System.out.println("error in protocol process");
        }

        //todo check if this needs to be here
        CommandEncoderDecoder encoderDecoder = new CommandEncoderDecoder();
        if(!processedMessage.isEmpty()){
            for (Command cmd : processedMessage){
                byte[] bytes = encoderDecoder.encode(cmd);
                connections.send(connectionId,bytes);
            }
        }

    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
