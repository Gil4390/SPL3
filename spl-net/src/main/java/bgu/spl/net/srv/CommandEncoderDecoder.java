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
        return (command.getOpCode() + command.getMsgOpCode() + command.getOptionalData() + ";").getBytes();
    }

    public byte[] encode(ErrorCommand command){
        return (command.getOpCode() + command.getMsgOpCode() +";").getBytes();
    }

//    public byte[] encode(NotificationCommand command){
//        byte [] opCode = shortToBytes((short)command.getOpCode());
//        byte [] type = shortToBytes((short)command.getType());
//        byte [] PostingUserName = command.getPostingUserName().getBytes(StandardCharsets.UTF_8);
//        byte [] content = command.getContent().getBytes(StandardCharsets.UTF_8);
//        byte [] zero = shortToBytes((short)0);
//
//        byte [] result = new byte[20];
//        List <Byte> resultList=new LinkedList<>();
//        Collections.addAll(resultList,opCode,type,PostingUserName,content,zero);
//
//
//        return resultList.toArray();
//    }

//    static <T> T[] concatWithCollection(T[] array1, T[] array2) {
//        List<T> resultList = new ArrayList<>(array1.length + array2.length);
//        Collections.addAll(resultList, array1);
//        Collections.addAll(resultList, array2);
//
//        @SuppressWarnings("unchecked")
//        //the type cast is safe as the array1 has the type T[]
//        T[] resultArray = (T[]) Array.newInstance(array1.getClass().getComponentType(), 0);
//        return resultList.toArray(resultArray);
//    }




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
