#include "Listener.h"
#include <string>
#include <iostream>
#include "EncoderDecoder.h"

Listener::Listener(ConnectionHandler* ch) : ch(ch) {
    
}

void Listener::run()
{
    EncoderDecoder encdec;
    
    while (1) {
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
        }
        else if (decodedLine == "ACK 3") {
            msg << "Disconnected. Exiting...\n" << std::endl;
            cout << msg.str();
            break;
        }
    }
}