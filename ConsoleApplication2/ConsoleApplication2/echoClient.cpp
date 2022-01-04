#include <stdlib.h>
#include "connectionHandler.h"
#include "InputReader.h"
#include "EncoderDecoder.h"
using namespace std;

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
int main(int argc, char* argv[]) {

    /*  
        if (argc < 3) {
            std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
            return -1;
        }
        std::string host = argv[1];
        short port = atoi(argv[2]);
        */

        //remove at the end.
    int port = 7777;
    std::string host = "127.0.0.1";

    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    std::vector<string>* sendList = new vector<string>;
    std::mutex mutex;
    InputReader inputReader(sendList, mutex);
    std::thread th1(&InputReader::run, &inputReader);
    EncoderDecoder encdec;
    std::string answer="";
    bool bye = false;

    while (1) {

        string line = "";

        mutex.lock();
        if (sendList->size() != 0) {
            line = sendList->at(0);
            sendList->erase(sendList->begin());
        }
        mutex.unlock();

        if (line != "") {
            char encodedLine[1024];
            int len = encdec.encode(line, encodedLine);

            if (!connectionHandler.sendBytes(encodedLine, len)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                bye = true;
                break;
            }
            else {
                std::cout << "Sent " << len + 1 << " bytes to server" << std::endl;
                if (!connectionHandler.getLine(answer)) {
                    std::cout << "Disconnected. Exiting...\n" << std::endl;
                    bye = true;
                    break;
                }

                string decodedLine = encdec.decodeLine(answer);
                len = decodedLine.length();
                std::cout << "Reply: " << decodedLine << " " << len << " bytes " << std::endl << std::endl;
            }


            if (line == "LOGOUT") {
                break;
            }
        }
    }
    inputReader.Terminate();
    while (!bye)
    {
        if(encdec.decodeLine(answer) == "ACK 3"){
            connectionHandler.close();
            std::cout << "Exiting...\n" << std::endl;
            bye = true;
        }
        else {
            int len = answer.length();
            std::cout << "Reply: " << answer << " " << len << " bytes " << std::endl << std::endl;
        }

        if (bye && !connectionHandler.getLine(answer)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            bye = true;
        }
    }

    return 0;
}