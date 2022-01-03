#include <string>
#include <vector>

using namespace std;

class EncoderDecoder
{
public:
	EncoderDecoder();
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