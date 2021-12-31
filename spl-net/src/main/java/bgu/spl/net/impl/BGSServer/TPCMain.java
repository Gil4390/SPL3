package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.impl.echo.EchoProtocol;
import bgu.spl.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl.net.srv.BaseServer;
import bgu.spl.net.srv.Server;

public class TPCMain {
    public static void main(String[] args) {
        try(Server<String> server = Server.threadPerClient(7777,()->new EchoProtocol(),()->new LineMessageEncoderDecoder());){
            server.serve();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
