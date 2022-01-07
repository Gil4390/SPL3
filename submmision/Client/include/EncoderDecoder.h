#include <string>
#include <vector>

using namespace std;

class EncoderDecoder
{
public:
	EncoderDecoder();
	string decodeLine(string line);
	int encode(string str, char* chararray);

private:
	void shortToBytes(short num, char* bytesArr);
	short bytesToShort(char* bytesArr);
	vector<string> split(string s, string delimiter);
};