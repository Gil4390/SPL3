package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.srv.Objects.CommandFactory;
import bgu.spl.net.srv.Objects.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class CommandEncoderDecoder implements MessageEncoderDecoder {

    private Command command;
    private byte [] opCode = new byte[2];
    private Vector <Byte> fieldBytes;
    private int opCodeCount=0;
    private int fieldCounter=1;
    private String clientUserName;

    @Override
    public Command decodeNextByte(byte nextByte) {
        if(nextByte == ';')
            return returnCommand();
        else if(command==null){
            if(opCodeCount<2) {
                opCode[opCodeCount]=nextByte;
                opCodeCount++;
            }
            else{
                ByteBuffer bb = ByteBuffer.wrap(opCode).order(ByteOrder.BIG_ENDIAN);
                command=CommandFactory.makeCommand(bb.getInt());
            }
        }
        else{
            command.decodeNextByte(nextByte, this);
        }
        return null;
    }

    public void decodeNextByte(byte nextByte, RegisterCommand command){
        if(nextByte == 0) {
            byte [] temp = new byte[fieldBytes.size()];
            for (int i=0;i<fieldBytes.size();i++) {
                temp[i]=fieldBytes.elementAt(i);
            }
            ByteBuffer bb = ByteBuffer.wrap(temp).order(ByteOrder.BIG_ENDIAN);
            if (fieldCounter == 1) {
                clientUserName=bb.toString();
                command.setName(bb.toString());
            }
            else if (fieldCounter == 2)
                command.setPassword(bb.toString());
            else
                command.setBirthday(bb.toString());
            fieldCounter++;
            fieldBytes=new Vector<>();
        }
        else{
            fieldBytes.add(nextByte);
        }
    }
    public void decodeNextByte(byte nextByte, LoginCommand command){
        if(nextByte == 0 | fieldCounter == 3) {
            if(fieldCounter != 3) {
                byte[] temp = new byte[fieldBytes.size()];
                for (int i = 0; i < fieldBytes.size(); i++) {
                    temp[i] = fieldBytes.elementAt(i);
                }
                ByteBuffer bb = ByteBuffer.wrap(temp).order(ByteOrder.BIG_ENDIAN);
                if (fieldCounter == 1) {
                    clientUserName=bb.toString();
                    command.setName(bb.toString());
                }
                else if (fieldCounter == 2)
                    command.setPassword(bb.toString());
                fieldCounter++;
                fieldBytes = new Vector<>();
            }
            else
                command.setCaptcha(nextByte);
        }
        else{
            fieldBytes.add(nextByte);
        }
    }

    }
    public void decodeNextByte(byte nextByte, LogoutCommand command){
        if(nextByte == 0) {
            byte [] temp = new byte[fieldBytes.size()];
            for (int i=0;i<fieldBytes.size();i++) {
                temp[i]=fieldBytes.elementAt(i);
            }
            ByteBuffer bb = ByteBuffer.wrap(temp).order(ByteOrder.BIG_ENDIAN);
            command.setName(bb.toString());
            fieldCounter++;
            fieldBytes=new Vector<>();
        }
        else{
            fieldBytes.add(nextByte);
        }
    }
    public void decodeNextByte(byte nextByte, LogStatCommand command){
        if(nextByte == 0) {
            byte [] temp = new byte[fieldBytes.size()];
            for (int i=0;i<fieldBytes.size();i++) {
                temp[i]=fieldBytes.elementAt(i);
            }
            ByteBuffer bb = ByteBuffer.wrap(temp).order(ByteOrder.BIG_ENDIAN);
            command.setName(bb.toString());
            fieldCounter++;
            fieldBytes=new Vector<>();
        }
        else{
            fieldBytes.add(nextByte);
        }
    }
    public void decodeNextByte(byte nextByte, NotificationCommand command){

    }
    public void decodeNextByte(byte nextByte, PostCommand command){
        if(nextByte == 0) {
            byte [] temp = new byte[fieldBytes.size()];
            for (int i=0;i<fieldBytes.size();i++) {
                temp[i]=fieldBytes.elementAt(i);
            }
            ByteBuffer bb = ByteBuffer.wrap(temp).order(ByteOrder.BIG_ENDIAN);
            if (fieldCounter == 1)
                command.setContent(bb.toString());
            else
                command.setName(bb.toString());
            fieldCounter++;
            fieldBytes=new Vector<>();
        }
        else{
            fieldBytes.add(nextByte);
        }
    }

    public void decodeNextByte(byte nextByte, PrivateMessageCommand command){
        if(nextByte == 0) {
            byte [] temp = new byte[fieldBytes.size()];
            for (int i=0;i<fieldBytes.size();i++) {
                temp[i]=fieldBytes.elementAt(i);
            }
            ByteBuffer bb = ByteBuffer.wrap(temp).order(ByteOrder.BIG_ENDIAN);
            if (fieldCounter == 1) {
                clientUserName=bb.toString();
                command.setSenderName(bb.toString());
            }
            else if (fieldCounter == 2)
                command.setReceiveName(bb.toString());
            else
                command.setSendingDate(bb.toString());
            fieldCounter++;
            fieldBytes=new Vector<>();
        }
        else{
            fieldBytes.add(nextByte);
        }
    }

    public void decodeNextByte(byte nextByte, LogStatCommand command){

    }

    public void decodeNextByte(byte nextByte, StatsCommand command){
        if(nextByte == 0) {
            byte [] temp = new byte[fieldBytes.size()];
            for (int i=0;i<fieldBytes.size();i++) {
                temp[i]=fieldBytes.elementAt(i);
            }
            ByteBuffer bb = ByteBuffer.wrap(temp).order(ByteOrder.BIG_ENDIAN);
            String userNamesString=bb.toString();

            List<String> userNames = new LinkedList<>();
            String str = "";
            for(int i=0; i<userNamesString.length();i++){
                if(userNamesString.charAt(i) != '|'){
                    str +=userNamesString.charAt(i);
                }
                else{
                    userNames.add(str);
                    i += str.length();
                    str="";
                }
            }
            command.setUserNameList(userNames);
        }
        else{
            fieldBytes.add(nextByte);
        }
    }
    public void decodeNextByte(byte nextByte, AckCommand command){

    }
    public void decodeNextByte(byte nextByte, BlockCommand command){
        if(nextByte == 0) {
            byte [] temp = new byte[fieldBytes.size()];
            for (int i=0;i<fieldBytes.size();i++) {
                temp[i]=fieldBytes.elementAt(i);
            }
            ByteBuffer bb = ByteBuffer.wrap(temp).order(ByteOrder.BIG_ENDIAN);
            command.setBlockedName(bb.toString());
            command.setClientName(clientUserName);
        }
        else{
            fieldBytes.add(nextByte);
        }
    }


    @Override
    public byte[] encode(Object message) {
        return new byte[0];
    }

    private Command returnCommand(){
        Command temp=command;
        command=null;
        opCode=new byte[2];
        opCodeCount=0;
        fieldCounter=1;
        return temp;
    }
}
