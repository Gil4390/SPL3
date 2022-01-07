#include "Listener.h"
#include <string>
#include <iostream>
#include "EncoderDecoder.h"

Listener::Listener(ConnectionHandler* ch) : terminate(false), ch(ch) {}

void Listener::run()
{
    EncoderDecoder encdec;
    
    while (!terminate) {
        stringstream msg;
        std::string answer = "";

        if (!ch->getLine(answer)) {
            msg << "Disconnected. Exiting...\n" << std::endl;
            cout << msg.str();            
            break;
        }
        std::string decodedLine;
        if (answer != "") {
            decodedLine = encdec.decodeLine(answer);
            msg << decodedLine << std::endl;
            cout << msg.str();

            if (decodedLine.compare("ACK 3") == 0) {
                stringstream msg2;
                msg2 << "Exiting Listener..." << std::endl;
                cout << msg2.str();
                terminate = true;
                ch->checkIfError = true;
                break;
            }
            if (decodedLine.compare("ERROR 3") == 0)
                ch->checkIfError = true;
        }
    }
}

bool Listener::isTerminate() {
    return terminate;
}