#include "InputReader.h"
#include <string>
#include <iostream>

InputReader::InputReader(std::vector<std::string>* sendQueue, std::mutex& mutex) : _sendQueue(*sendQueue), _mutex(mutex) {
    _terminate = false;
}

void InputReader::run()
{
    while (_terminate) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);
        int len = line.length();
        
        
        _mutex.lock();
        _sendQueue.emplace_back(line);
        _mutex.unlock(); 
    }
}

void InputReader::Terminate()
{
    _terminate = true;
}
