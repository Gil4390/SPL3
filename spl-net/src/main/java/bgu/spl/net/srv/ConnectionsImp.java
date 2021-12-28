package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.Connections;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImp implements Connections<Command> {

    private ConcurrentHashMap<Integer,NonBlockingConnectionHandler<Command>> connections;

    private static class ConnectionHolder{
        private static ConnectionsImp clusterInstance = new ConnectionsImp();
    }

    public static ConnectionsImp getInstance() {
        return ConnectionHolder.clusterInstance;
    }


    @Override
    public boolean send(int connectionId, Command msg) {
        if(connections.contains(connectionId)) {
            connections.get(connectionId).send(msg);
            return true;
        }
        return false;
    }

    @Override
    public void broadcast(Command msg) {
        for(NonBlockingConnectionHandler<Command> client : connections.values()){
            client.send(msg);
        }
    }

    @Override
    public void disconnect(int connectionId) {
        connections.remove(connectionId);
    }
}
