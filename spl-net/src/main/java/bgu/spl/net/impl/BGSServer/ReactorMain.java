package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.srv.*;

public class ReactorMain {
    public static void main(String[] args) {
        Twitter twitter = new Twitter();

        try(Server<Command> server = Server.reactor(100,7777,()->new Protocol(twitter),()->new CommandEncoderDecoder());){
            server.serve();
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
