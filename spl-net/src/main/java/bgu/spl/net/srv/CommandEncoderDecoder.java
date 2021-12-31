package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.srv.Objects.CommandFactory;
import bgu.spl.net.srv.Objects.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CommandEncoderDecoder implements MessageEncoderDecoder {

    private ReceivedCommand command;
    private byte [] opCode = new byte[2];
    private Vector <Byte> fieldBytes;
    private int opCodeCount=0;
    private int fieldCounter=1;
    private String clientUserName;


    @Override
    public ReceivedCommand decodeNextByte(byte nextByte) {
        if(nextByte == ';') {
            if(command instanceof FollowCommand) {
                decodeNextByte(("0").getBytes()[0], ((FollowCommand) command));
            }
            return returnCommand();
        }
        else if(command==null){
            if(opCodeCount<2) {
                opCode[opCodeCount]=nextByte;
                opCodeCount++;
            }
            else{
                ByteBuffer bb = ByteBuffer.wrap(opCode).order(ByteOrder.BIG_ENDIAN);
                command=CommandFactory.makeReceivedCommand(bb.getInt());
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

    public void decodeNextByte(byte nextByte, FollowCommand command){
        if(fieldCounter==1){
            command.setUnFollow(nextByte);
            fieldCounter++;
        }
        else{
            if(nextByte==0){
                byte[] temp = new byte[fieldBytes.size()];
                for (int i = 0; i < fieldBytes.size(); i++) {
                    temp[i] = fieldBytes.elementAt(i);
                }
                ByteBuffer bb = ByteBuffer.wrap(temp).order(ByteOrder.BIG_ENDIAN);
                command.setFollowName(bb.toString());
                command.setClientName(clientUserName);
            }
            else
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
        return ((ReturnCommand)message).encode(this);
    }

    public byte[] encode(AckCommand command){
        byte [] opCode = shortToBytes((short)command.getOpCode());
        byte [] msgOpCode = shortToBytes((short)command.getMsgOpCode());
        byte [] optional = (command.getOptionalData()+";").getBytes(StandardCharsets.UTF_8);

        byte [] result = addArray(opCode,msgOpCode);
        result = addArray(result,optional);
        return result;
    }

    public byte[] encode(ErrorCommand command){
        byte [] opCode = shortToBytes((short)command.getOpCode());
        byte [] msgOpCode = shortToBytes((short)command.getMsgOpCode());
        byte [] end = ";".getBytes(StandardCharsets.UTF_8);

        byte [] result = addArray(opCode,msgOpCode);
        result = addArray(result,end);
        return result;
    }

    public byte[] encode(NotificationCommand command){
        byte [] opCode = shortToBytes((short)command.getOpCode());
        byte [] type = shortToBytes((short)command.getType());
        byte [] PostingUserName = command.getPostingUserName().getBytes(StandardCharsets.UTF_8);
        byte [] content = command.getContent().getBytes(StandardCharsets.UTF_8);
        byte [] zero = shortToBytes((short)0);


        byte [] result = addArray(opCode,type);
        result = addArray(result,PostingUserName);
        result = addArray(result,content);
        result = addArray(result,zero);
        return result;
    }

    private byte [] addArray(byte [] first, byte [] second){
        byte [] result = new byte[first.length+second.length];
        for(int i=0; i<first.length+second.length;i++) {
            if(i<first.length)
                result[i] =first[i];
            else
                result[i]=second[i];
        }
        return result;
    }

    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    private ReceivedCommand returnCommand(){
        ReceivedCommand temp=command;
        command=null;
        opCode=new byte[2];
        opCodeCount=0;
        fieldCounter=1;
        return temp;
    }
}
