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
        std::string answer = "";
        if (!ch->getLine(answer)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        std::string decodedLine;
        if (answer != "") {
            decodedLine = encdec.decodeLine(answer);
            std::cout << decodedLine << std::endl;
        }

        if (decodedLine == "ACK 3") {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
    }
}