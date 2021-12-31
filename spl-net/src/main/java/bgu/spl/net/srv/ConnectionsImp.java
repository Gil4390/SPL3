package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.bidi.ConnectionHandler;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImp implements Connections<Command> {

    private ConcurrentHashMap<Integer, ConnectionHandler<Command>> connections;

    private static class ConnectionHolder{
        private static ConnectionsImp connectionsInstance = new ConnectionsImp();
    }

    public static ConnectionsImp getInstance() {
        return ConnectionHolder.connectionsInstance;
    }

    public void connect(int connectionId, ConnectionHandler con){
        connections.put(connectionId,con);
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
        for(ConnectionHandler<Command> client : connections.values()){
            client.send(msg);
        }
    }

    @Override
    public void disconnect(int connectionId) {
        connections.remove(connectionId);
    }
}
