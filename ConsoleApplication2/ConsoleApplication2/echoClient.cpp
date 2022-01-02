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

    std::vector<string>* sendList;
    std::mutex mutex;
    InputReader inputReader(sendList, mutex);
    std::thread th1(&InputReader::run, &inputReader);
    EncoderDecoder encdec;

    while (1) {
        
        string line = "";

        mutex.lock();
        if (sendList->size() != 0) {
            line = sendList->at(0);
            sendList->erase(sendList->begin());
        }
        mutex.unlock();

        if (line != "") {
            int len = line.size();
            string encodedLine = encdec.encode(line);
            
            if (!connectionHandler.sendLine(encodedLine)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                inputReader.Terminate();
                break;
            }
            else {
                std::string answer;
                if (!connectionHandler.getLine(answer)) {
                    std::cout << "Disconnected. Exiting...\n" << std::endl;
                    break;
                }
                //string decodedLine = encdec
                //========================================================continue here
                len = answer.length();
                answer.resize(len - 1);
                std::cout << "Reply: " << answer << " " << len << " bytes " << std::endl << std::endl;
            }
            // connectionHandler.sendLine(line) appends '\n' to the message. Therefor we send len+1 bytes.
            std::cout << "Sent " << len + 1 << " bytes to server" << std::endl;

            if (line == "LOGOUT") {
                break;
                
            }
        }

        bool bye = false;
        while (!bye)
        {
            std::string answer;
            if (!connectionHandler.getLine(answer)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            if (answer[0] == '1' && answer[1] == '0' && answer[2] == '0' && answer[3] == '3') {
                connectionHandler.close();
                std::cout << "Exiting...\n" << std::endl;
                bye = true;
            }
        }
        


        // We can use one of three options to read data from the server:
        // 1. Read a fixed number of characters
        // 2. Read a line (up to the newline character using the getline() buffered reader
        // 3. Read up to the null character
        std::string answer;
        // Get back an answer: by using the expected number of bytes (len bytes + newline delimiter)
        // We could also use: connectionHandler.getline(answer) and then get the answer without the newline char at the end
        if (!connectionHandler.getLine(answer)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }

        len = answer.length();
        // A C string must end with a 0 char delimiter.  When we filled the answer buffer from the socket
        // we filled up to the \n char - we must make sure now that a 0 char is also present. So we truncate last character.
        answer.resize(len - 1);
        std::cout << "Reply: " << answer << " " << len << " bytes " << std::endl << std::endl;
        if (answer == "bye") {
            std::cout << "Exiting...\n" << std::endl;
            break;
        }
    }
    return 0;
}
