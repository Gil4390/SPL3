package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;

public class Protocol implements BidiMessagingProtocol {
    private Twitter twit;

    public Protocol(Twitter twit){
        this.twit=twit;
    }

    @Override
    public void start(int connectionId, Connections connections) {

    }

    @Override
    public void process(Object message) {

    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }
}
