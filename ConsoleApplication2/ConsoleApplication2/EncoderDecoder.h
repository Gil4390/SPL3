#include <string>

#include <rpcndr.h>
#include <vector>
#ifndef ENCODERDECODER_H_
#define ENCODERDECODER_H_

using namespace std;

class EncoderDeconder
{
public:
	EncoderDeconder();
	string decodeNextByte(byte nextByte);
	string encode(string str);
private:
	void shortToBytes(short num, char* bytesArr);
	short bytesToShort(char* bytesArr);
	vector<string> split(string s, string delimiter);

	int byteCounter;
	int zeroCounter;
	string opCode;
	string msgOpCode;
	string optional;


};

#endif

