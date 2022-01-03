#include <string>

#include <rpcndr.h>
#include <vector>

using namespace std;

class EncoderDecoder
{
public:
	EncoderDecoder();
	string decodeNextByte(byte nextByte);
	string decodeLine(string line);
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