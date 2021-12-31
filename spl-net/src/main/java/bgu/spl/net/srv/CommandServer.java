package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Supplier;

public abstract class CommandServer implements Server<Command>{

    private final int port;
    private final Supplier<MessagingProtocol<Command>> protocolFactory;
    private final Supplier<MessageEncoderDecoder<Command>> encdecFactory;
    private ServerSocket sock;
    private int connectionId;

    public CommandServer(
            int port,
            Supplier<MessagingProtocol<Command>> protocolFactory,
            Supplier<MessageEncoderDecoder<Command>> encdecFactory) {

        this.port = port;
        this.protocolFactory = protocolFactory;
        this.encdecFactory = encdecFactory;
        this.sock = null;
        connectionId=0;
    }

    @Override
    public void serve() {

        try (ServerSocket serverSock = new ServerSocket(port)) {
            System.out.println("Server started");

            this.sock = serverSock; //just to be able to close

            while (!Thread.currentThread().isInterrupted()) {

                Socket clientSock = serverSock.accept();

                BlockingConnectionHandler<Command> handler = new BlockingConnectionHandler<>(
                        clientSock,
                        encdecFactory.get(),
                        protocolFactory.get());
                handler.initiate(connectionId,ConnectionsImp.getInstance());
                connectionId++;
                execute(handler);

            }
        } catch (IOException ex) {
        }

        System.out.println("server closed!!!");
    }

    @Override
    public void close() throws IOException {
        if (sock != null)
            sock.close();
    }

    protected abstract void execute(BlockingConnectionHandler<Command>  handler);
}

