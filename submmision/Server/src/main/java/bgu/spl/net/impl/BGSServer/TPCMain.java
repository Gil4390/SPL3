package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.impl.echo.EchoProtocol;
import bgu.spl.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl.net.srv.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

public class TPCMain {
    public static void main(String[] args) {
        Twitter twitter = new Twitter();

        try(Server<Command> server = Server.threadPerClient(7777,()->new Protocol(twitter),()->new CommandEncoderDecoder());){
            server.serve();
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
