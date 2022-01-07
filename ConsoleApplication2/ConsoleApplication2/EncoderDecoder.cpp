#define _CRT_SECURE_NO_WARNINGS
#include "EncoderDecoder.h"
#include <iostream>
#include <ctime>
using namespace std;

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

        if (line[2] == '1') msgType = "Public";
        else msgType = "PM";

        line = line.substr(3);
        vector<string> lineSplit = split(line, " ");
        string postingUser = lineSplit[0];

        string content = "";
        for (int i = 1; i < lineSplit.size();i++) {
            content += lineSplit[i] + " ";
        }
        result = "NOTIFICATION " + msgType + " " + postingUser + " " + content.substr(0, content.length() - 2);
    }
    else if (opCode == "10") { //ACK
        string msgOpCode = line.substr(2, 2);
        if (msgOpCode[0] == '0') msgOpCode = msgOpCode.substr(1);
        string optional = line.substr(4, line.size()-5);
        if(optional.length() > 0)
            result = "ACK " + msgOpCode + " " + optional;
        else
            result = "ACK " + msgOpCode;
    }
    else { //Error
        string msgOpCode = line.substr(2, 2);
        if (msgOpCode[0] == '0') msgOpCode = msgOpCode.substr(1);
        result = "Error " + msgOpCode;
    }    
    return result;
}

int EncoderDecoder::encode(string str, char* chararray)
{
	vector<string> vecOfInput = split(str, " ");
    short opcode;
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

        chararray[0] = opchar[0];
        chararray[1] = opchar[1];
        for (int i = 0; i < username.size(); i++) {
            chararray[len] = usernamearr[i];
            len++;
        }
        chararray[len] = '\0';
        len++;
        for (int i = 0; i < password.size(); i++) {
            chararray[len] = passwordarr[i];
            len++;
        }
        chararray[len] = '\0';
        len++;
        for (int i = 0; i < birthday.size(); i++) {
            chararray[len] = birthdayarr[i];
            len++;
        }
        chararray[len] = '\0';
        len++;
    }

    else if (vecOfInput[0] == "LOGIN") {
        opcode = 2;
        string username = vecOfInput[1];
        string password = vecOfInput[2];
        string captcha = vecOfInput[3];

        char const* usernamearr = username.c_str();
        char const* passwordarr = password.c_str();

        char opchar[2];
        shortToBytes(opcode, opchar);
        len = 2;

        chararray[0] = opchar[0];
        chararray[1] = opchar[1];
        for (int i = 0; i < username.size(); i++) {
            chararray[len] = usernamearr[i];
            len++;
        }
        chararray[len] = '\0';
        len++;
        for (int i = 0; i < password.size(); i++) {
            chararray[len] = passwordarr[i];
            len++;
        }
        chararray[len] = '\0';
        len++;
        if (captcha == "0") chararray[len] = '\0';
        else chararray[len] = '\1';
        len++;
    }
    else if (vecOfInput[0] == "LOGOUT" || vecOfInput[0] == "LOGSTAT") {
        opcode = 3;
        if(vecOfInput[0] == "LOGSTAT")
            opcode = 7;
        char opchar[2];
        shortToBytes(opcode, opchar);
        len = 2;
        chararray[0] = opchar[0];
        chararray[1] = opchar[1];
    }
    else if (vecOfInput[0] == "FOLLOW") {
        opcode = 4;

        char opchar[2];
        shortToBytes(opcode, opchar);
        
        chararray[0] = opchar[0];
        chararray[1] = opchar[1];

        if (vecOfInput[1] == "0")
            chararray[2] = '\0';
        else
            chararray[2] = '\1';

        len = 3;
        string userName = vecOfInput[2];

        char const* usernamearr = userName.c_str();
        for (int i = 0; i < userName.size(); i++) {
            chararray[len] = usernamearr[i];
            len++;
        }
        chararray[len] = '\0';
        len++;
    }

    else if (vecOfInput[0] == "POST") {
    opcode = 5;

    string content = "";
    for (int i = 1; i < vecOfInput.size();i++) {
        content += vecOfInput[i] + " ";
    }
    content = content.substr(0, content.length() - 1);

    char opchar[2];
    shortToBytes(opcode, opchar);
    len = 2;
    chararray[0] = opchar[0];
    chararray[1] = opchar[1];

    char const* contentarr = content.c_str();
    for (int i = 0; i < content.size(); i++) {
        chararray[len] = contentarr[i];
        len++;
    }
    chararray[len] = '\0';
    len++;
    }

    if (len != 0) {
        string end = ";";
        chararray[len] = end.c_str()[0];
        len++;
    }


    else if (vecOfInput[0] == "PM") {
        opcode = 6;

        string username = vecOfInput[1];

        string content = "";
        for (int i = 2; i < vecOfInput.size();i++) {
            content += vecOfInput[i] +" ";
        }
        content = content.substr(0, content.length() - 1);

        char const* usernameArr = username.c_str();
        char const* contentArr = content.c_str();


        char opchar[2];
        shortToBytes(opcode, opchar);
        len = 2;

        chararray[0] = opchar[0];
        chararray[1] = opchar[1];
        for (int i = 0; i < username.size(); i++) {
            chararray[len] = usernameArr[i];
            len++;
        }
        chararray[len] = '\0';
        len++;
        for (int i = 0; i < content.size(); i++) {
            chararray[len] = contentArr[i];
            len++;
        }
        chararray[len] = '\0';
        len++;

        time_t rawtime;
        
        tm* timeinfo;
        char buffer[100];

        time(&rawtime);
        timeinfo = localtime(&rawtime);

        strftime(buffer, 50, "%d-%m-%Y %H:%M:%S", timeinfo);

        std::string dateStr(buffer);
        char const* dateArr = dateStr.c_str();
        for (int i = 0; i < dateStr.size(); i++) {
            chararray[len] = dateArr[i];
            len++;
        }

        chararray[len] = '\0';
        len++;        
    }
    
    else if (vecOfInput[0] == "STAT" || vecOfInput[0] == "BLOCK") {
    opcode = 8;
    if(vecOfInput[0] == "BLOCK")
        opcode = 12;
    string content = vecOfInput[1];

        char opchar[2];
        shortToBytes(opcode, opchar);
        len = 2;
        chararray[0] = opchar[0];
        chararray[1] = opchar[1];

        char const* contentarr = content.c_str();
        for (int i = 0; i < content.size(); i++) {
            chararray[len] = contentarr[i];
            len++;
        }
        chararray[len] = '\0';
        len++;
    }

    if (len != 0) {
        string end = ";";
        chararray[len] = end.c_str()[0];
        len++;
    }

	return len;
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
