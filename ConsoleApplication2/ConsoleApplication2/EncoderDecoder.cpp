#include "EncoderDecoder.h"
#include <iostream>

EncoderDecoder::EncoderDecoder()
{
    byteCounter = 0;
    zeroCounter = 0;
    opCode = "";
    msgOpCode = "";
    optional = "";
}

string EncoderDecoder::decodeNextByte(byte nextByte)
{
    if ((char)nextByte != ';') {
        if (byteCounter <= 1) {
            opCode = opCode + (char)nextByte;
        }
        else {
            if (opCode == "09") { //Notification
                if (byteCounter == 2) {//Type: PM/Post
                    optional = (char)nextByte + " ";
                }
                else if (zeroCounter == 0) {
                    if ((char)nextByte == '\0') {
                        zeroCounter++;
                        optional += " ";
                    }
                    else {
                        optional += (char)nextByte;
                    }
                }
                else {
                    if ((char)nextByte == '\0') {
                        zeroCounter++;
                    }
                    else {
                        optional += (char)nextByte;
                    }
                }
            }
            else if (opCode == "10") { //ACK
                if (byteCounter <= 3) {
                    msgOpCode = msgOpCode + (char)nextByte;
                }
                else {
                    optional += optional + (char)nextByte;
                }
            }
            else { //Error
                msgOpCode = msgOpCode + (char)nextByte;
            }

        }
        return nullptr;
    }
    else {
        if (opCode[0] == '0') {
            opCode = opCode[1];
        }
        if (msgOpCode[0] == '0') {
            msgOpCode = msgOpCode[1];
        }
        return opCode + " " + msgOpCode + " " + optional;
    }

    byteCounter++;
    /*
    if (nextByte == ';') {
        if (command instanceof FollowCommand) {
            decodeNextByte(("0").getBytes()[0], ((FollowCommand)command));
        }
        return returnCommand();
    }
    else if (command == null) {
        if (opCodeCount < 2) {
            opCode[opCodeCount] = nextByte;
            opCodeCount++;
        }
        else {
            ByteBuffer bb = ByteBuffer.wrap(opCode).order(ByteOrder.BIG_ENDIAN);
            command = CommandFactory.makeReceivedCommand(bb.getInt());
        }
    }
    else {
        command.decodeNextByte(nextByte, this);
    }
    return null;
    */
	return string();
}

string EncoderDecoder::decodeLine(string line) {
    int charCount = 0;
    int zeroCounter = 0;
    string opCode = "";
    string msgOpCode = "";
    string optional = "";

    string result = "";

    opCode = line.substr(0, 2);
    if (opCode == "09") { //Notification
        string msgType = "";
        if (line[3] == '0') msgType = "PM";
        else msgType = "Public";

        line = line.substr(3);
        vector<string> lineSplit = split(line, 0);
        string postingUser = lineSplit[0];
        string content = lineSplit[1];

        result = "NOTIFICATION " + msgType + " " + postingUser + " " + content;
    }
    else if (opCode == "10") { //ACK
        
    }
    else { //Error
        msgOpCode = msgOpCode + (char)nextByte;
    }
    
    for (int i = 0; i < line.size(); i++) {
        if (charCount <= 1) {
            opCode = opCode + (char)nextByte;
        }
    }
        if (byteCounter <= 1) {
            opCode = opCode + (char)nextByte;
        }
        else {
            

        }
        return nullptr;
    }

        if (opCode[0] == '0') {
            opCode = opCode[1];
        }
        if (msgOpCode[0] == '0') {
            msgOpCode = msgOpCode[1];
        }
        return opCode + " " + msgOpCode + " " + optional;

    byteCounter++;
    return string();
}

string EncoderDecoder::encode(string str)
{
	vector<string> vecOfInput = split(str, " ");
    char zero = '\0';
    string opcode;
    string result = "";

    if (vecOfInput[0] == "REGISTER") {
        opcode = "01";
        string username = vecOfInput[1];
        string password = vecOfInput[2];
        string birthday = vecOfInput[3];

        result = opcode + username + zero + password + zero + birthday + zero;
    }
    else if (vecOfInput[0] == "LOGIN") {
        opcode = "02";

        string username = vecOfInput[1];
        string password = vecOfInput[2];
        char captcha = '\1';

        result = opcode + username + zero + password + zero + captcha;
    }
    else if (vecOfInput[0] == "LOGOUT") {
        opcode = "03";

        result = opcode;
    }
    else if (vecOfInput[0] == "FOLLOW") {
        opcode = "04";
        char follow = '\0';
        string username = vecOfInput[1];

        result = opcode + follow + username;
    }
    else if (vecOfInput[0] == "UNFOLLOW") {
        opcode = "04";
        char follow = '\1';
        string username = vecOfInput[1];

        result = opcode + follow + username;
    }
    else if (vecOfInput[0] == "POST") {
        opcode = "05";
        string content = vecOfInput[1];

        result = opcode + content + zero;
    }
    else if (vecOfInput[0] == "PM") {
        opcode = "06";
        string username = vecOfInput[1];
        string content = vecOfInput[2];
        string sendingDate = vecOfInput[3];

        result = opcode + username + zero + content + zero + sendingDate + zero;;
    }
    else if (vecOfInput[0] == "LOGSTAT") {
        opcode = "07";

        result = opcode;
    }
    else if (vecOfInput[0] == "STAT") {
        opcode = "08";
        string listOfUsernames = vecOfInput[1];

        result = opcode + listOfUsernames + zero;
    }
    else if (vecOfInput[0] == "BLOCK") {
        opcode = "12";
        string username = vecOfInput[1];

        result = opcode + username + zero;
    }
    else {
        cout << "illegal command";
    }

    result = result + ";";

	return result;
}

void EncoderDecoder::shortToBytes(short num, char* bytesArr)
{
	bytesArr[0] = ((num >> 8) & 0xFF);
	bytesArr[1] = (num & 0xFF);
}

short EncoderDecoder::bytesToShort(char* bytesArr)
{
	short result = (short)((bytesArr[0] & 0xff) << 8);
	result += (short)(bytesArr[1] & 0xff);
	return result;
}

vector<string> EncoderDecoder::split(string s, string delimiter)
{
    size_t pos_start = 0, pos_end, delim_len = delimiter.length();
    string token;
    vector<string> res;

    while ((pos_end = s.find(delimiter, pos_start)) != string::npos)
    {
        token = s.substr(pos_start, pos_end - pos_start);
        pos_start = pos_end + delim_len;
        res.push_back(token);
    }

    res.push_back(s.substr(pos_start));
    return res;
}
