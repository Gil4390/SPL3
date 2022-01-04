package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
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

    public CommandEncoderDecoder() {
        fieldBytes = new Vector<>();
    }


    @Override
    public ReceivedCommand decodeNextByte(byte nextByte) {
        if(nextByte == ';') {
            if(command instanceof FollowCommand) {
                decodeNextByte(("0").getBytes()[0], ((FollowCommand) command));
            }
            return returnCommand();
        }
        else if(command==null){
            if(opCodeCount==0) {
                opCode[opCodeCount]=nextByte;
                opCodeCount++;
            }
            else if(opCodeCount == 1){
                opCode[opCodeCount]=nextByte;
                opCodeCount++;
                ByteBuffer bb = ByteBuffer.wrap(opCode);
                int num = bb.getShort();
                command=CommandFactory.makeReceivedCommand(num);
            }
        }
        else{
            command.decodeNextByte(nextByte, this);
        }
        return null;
    }

    public void decodeNextByte(byte nextByte, RegisterCommand command){
        if(nextByte == '\0') {
            byte [] temp = new byte[fieldBytes.size()];
            for (int i=0;i<fieldBytes.size();i++) {
                temp[i]=fieldBytes.elementAt(i);
            }
            ByteBuffer bb = ByteBuffer.wrap(temp).order(ByteOrder.BIG_ENDIAN);
            if (fieldCounter == 1) {
                clientUserName=StandardCharsets.UTF_8.decode(bb).toString();
                command.setName(clientUserName);
            }
            else if (fieldCounter == 2)
                command.setPassword(StandardCharsets.UTF_8.decode(bb).toString());
            else
                command.setBirthday(StandardCharsets.UTF_8.decode(bb).toString());
            fieldCounter++;
            fieldBytes=new Vector<>();
        }
        else{
            fieldBytes.add(nextByte);
        }
    }

    public void decodeNextByte(byte nextByte, LoginCommand command){
        if(nextByte == '\0' | fieldCounter == 3) {
            if(fieldCounter != 3) {
                byte[] temp = new byte[fieldBytes.size()];
                for (int i = 0; i < fieldBytes.size(); i++) {
                    temp[i] = fieldBytes.elementAt(i);
                }
                ByteBuffer bb = ByteBuffer.wrap(temp).order(ByteOrder.BIG_ENDIAN);
                if (fieldCounter == 1) {
                    clientUserName=StandardCharsets.UTF_8.decode(bb).toString();
                    command.setName(clientUserName);
                }
                else if (fieldCounter == 2)
                    command.setPassword(StandardCharsets.UTF_8.decode(bb).toString());
                else{
                    command.setCaptcha(Integer.parseInt(StandardCharsets.UTF_8.decode(bb).toString()));
                }
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
        if(nextByte == '\0') {
            byte [] temp = new byte[fieldBytes.size()];
            for (int i=0;i<fieldBytes.size();i++) {
                temp[i]=fieldBytes.elementAt(i);
            }
            ByteBuffer bb = ByteBuffer.wrap(temp).order(ByteOrder.BIG_ENDIAN);
            command.setName(StandardCharsets.UTF_8.decode(bb).toString());
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
            if(nextByte=='\0'){
                byte[] temp = new byte[fieldBytes.size()];
                for (int i = 0; i < fieldBytes.size(); i++) {
                    temp[i] = fieldBytes.elementAt(i);
                }
                ByteBuffer bb = ByteBuffer.wrap(temp).order(ByteOrder.BIG_ENDIAN);
                command.setFollowName(StandardCharsets.UTF_8.decode(bb).toString());
                command.setClientName(clientUserName);
            }
            else
                fieldBytes.add(nextByte);
        }
    }

    public void decodeNextByte(byte nextByte, LogStatCommand command){
        if(nextByte == '\0') {
            byte [] temp = new byte[fieldBytes.size()];
            for (int i=0;i<fieldBytes.size();i++) {
                temp[i]=fieldBytes.elementAt(i);
            }
            ByteBuffer bb = ByteBuffer.wrap(temp).order(ByteOrder.BIG_ENDIAN);
            command.setName(StandardCharsets.UTF_8.decode(bb).toString());
            fieldCounter++;
            fieldBytes=new Vector<>();
        }
        else{
            fieldBytes.add(nextByte);
        }
    }
    public void decodeNextByte(byte nextByte, PostCommand command){
        if(nextByte == '\0') {
            byte [] temp = new byte[fieldBytes.size()];
            for (int i=0;i<fieldBytes.size();i++) {
                temp[i]=fieldBytes.elementAt(i);
            }
            ByteBuffer bb = ByteBuffer.wrap(temp).order(ByteOrder.BIG_ENDIAN);
            if (fieldCounter == 1)
                command.setContent(StandardCharsets.UTF_8.decode(bb).toString());
            else
                command.setName(StandardCharsets.UTF_8.decode(bb).toString());
            fieldCounter++;
            fieldBytes=new Vector<>();
        }
        else{
            fieldBytes.add(nextByte);
        }
    }

    public void decodeNextByte(byte nextByte, PrivateMessageCommand command){
        if(nextByte == '\0') {
            byte [] temp = new byte[fieldBytes.size()];
            for (int i=0;i<fieldBytes.size();i++) {
                temp[i]=fieldBytes.elementAt(i);
            }
            ByteBuffer bb = ByteBuffer.wrap(temp).order(ByteOrder.BIG_ENDIAN);
            if (fieldCounter == 1) {
                clientUserName=StandardCharsets.UTF_8.decode(bb).toString();
                command.setSenderName(clientUserName);
            }
            else if (fieldCounter == 2)
                command.setReceiveName(StandardCharsets.UTF_8.decode(bb).toString());
            else
                command.setSendingDate(StandardCharsets.UTF_8.decode(bb).toString());
            fieldCounter++;
            fieldBytes=new Vector<>();
        }
        else{
            fieldBytes.add(nextByte);
        }
    }

    public void decodeNextByte(byte nextByte, StatsCommand command){
        if(nextByte == '\0') {
            byte [] temp = new byte[fieldBytes.size()];
            for (int i=0;i<fieldBytes.size();i++) {
                temp[i]=fieldBytes.elementAt(i);
            }
            ByteBuffer bb = ByteBuffer.wrap(temp).order(ByteOrder.BIG_ENDIAN);
            String userNamesString=StandardCharsets.UTF_8.decode(bb).toString();

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
        if(nextByte == '\0') {
            byte [] temp = new byte[fieldBytes.size()];
            for (int i=0;i<fieldBytes.size();i++) {
                temp[i]=fieldBytes.elementAt(i);
            }
            ByteBuffer bb = ByteBuffer.wrap(temp).order(ByteOrder.BIG_ENDIAN);
            command.setBlockedName(StandardCharsets.UTF_8.decode(bb).toString());
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
        byte [] opCode = new byte[2];
        opCode[0] = '1';
        opCode[1] = '0';
        byte [] msgOpCode = new byte[2];
        if(command.getMsgOpCode() != 12){
            msgOpCode[0] = '0';
            String s = String.valueOf(command.getMsgOpCode());
            msgOpCode[1] = (byte)s.charAt(0);
        }
        else{
            msgOpCode[0] = '1';
            msgOpCode[1] = '2';
        }
        byte [] optional = (command.getOptionalData()+";").getBytes(StandardCharsets.UTF_8);

        byte [] result = addArray(opCode,msgOpCode);
        result = addArray(result,optional);
        return result;
    }

    public byte[] encode(ErrorCommand command){
        byte [] opCode = new byte[2];
        opCode[0] = '1';
        opCode[1] = '1';
        byte [] msgOpCode = new byte[2];
        if(command.getMsgOpCode() != 12){
            msgOpCode[0] = '0';
            String s = String.valueOf(command.getMsgOpCode());
            msgOpCode[1] = (byte)s.charAt(0);
        }
        else{
            msgOpCode[0] = '1';
            msgOpCode[1] = '2';
        }
        byte [] end = ";".getBytes(StandardCharsets.UTF_8);

        byte [] result = addArray(opCode,msgOpCode);
        result = addArray(result,end);
        return result;
    }

    public byte[] encode(NotificationCommand command){
        byte [] opCode = new byte[2];
        opCode[0] = '9';
        opCode[1] = '0';
        byte [] type = new byte[1];
        String s = String.valueOf(command.getType());
        type[0] = (byte)s.charAt(0);
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
                result[i]=second[i-first.length];
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

    public short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
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
