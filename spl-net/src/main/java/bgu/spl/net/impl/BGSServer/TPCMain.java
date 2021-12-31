package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.impl.echo.EchoProtocol;
import bgu.spl.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl.net.srv.*;

public class TPCMain {
    public static void main(String[] args) {
        /*
        try(Server<String> server = Server.threadPerClient(7777,()->new EchoProtocol(),()->new LineMessageEncoderDecoder());){
            server.serve();
        }catch(Exception e){
            e.printStackTrace();
        }

*/
        Twitter twitter = new Twitter();

        try(Server<Command> server = Server.threadPerClient(7777,()->new Protocol(twitter),()->new CommandEncoderDecoder());){
            server.serve();
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
