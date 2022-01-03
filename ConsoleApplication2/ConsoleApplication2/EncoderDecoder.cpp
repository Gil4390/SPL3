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

string EncoderDecoder::decodeLine(string line) {
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
        string msgOpCode = line.substr(2, 3);
        string optional = line.substr(4, line.size()-1);
        result = "ACK " + msgOpCode + " " + optional;
    }
    else { //Error
        string msgOpCode = line.substr(2, 3);
        result = "ACK " + msgOpCode;
    }
    
    return result;
}

int EncoderDecoder::encode(string str, char* chararray)
{
	vector<string> vecOfInput = split(str, " ");
    short opcode;
    //vector<char> result;
    int len = 0;

    if (vecOfInput[0] == "REGISTER") {
        opcode = 1;
        string username = vecOfInput[1];
        string password = vecOfInput[2];
        string birthday = vecOfInput[3];
        char const* usernamearr = username.c_str();
        char const* passwordarr = password.c_str();
        char const* birthdayarr = birthday.c_str();
        char opchar[2];
        shortToBytes(opcode, opchar);
        len = 2;

        int size = (*(&usernamearr + 1) - usernamearr);

        cout << size << endl;
        cout << (sizeof(usernamearr[4])) << endl;
        cout << sizeof(birthdayarr) << endl;


        chararray[0] = opchar[0];
        chararray[1] = opchar[1];
        for (int i = 0; i < (sizeof(*usernamearr)/(2)); i++) {
            chararray[len] = usernamearr[i];
            len++;
        }
        chararray[len] = '\0';
        len++;
        for (int i = 0; i < (sizeof(*passwordarr) / (2)); i++) {
            chararray[len] = passwordarr[i];
            len++;
        }
        chararray[len] = '\0';
        len++;
        for (int i = 0; i < (sizeof(*birthdayarr) / (2)); i++) {
            chararray[len] = birthdayarr[i];
            len++;
        }
        return len;
    }/*
    else if (vecOfInput[0] == "LOGIN") {
        opcode = 2;
        string username = vecOfInput[1];
        string password = vecOfInput[2];
        char captcha = '\1';

        char opchar[2];
        shortToBytes(opcode, opchar);
        //result.push_back(opchar[0]);
        //result.push_back(opchar[1]);
        for (char c : username) {
            result.push_back(c);
        }
        result.push_back('\0');
        for (char c : password) {
            result.push_back(c);
        }
        result.push_back('\0');
        result.push_back('\1');
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
    */

    //result.push_back(';');

	return -1;
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
