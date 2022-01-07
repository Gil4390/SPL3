#include "InputReader.h"
#include <string>
#include <iostream>
#include <chrono>
#include <thread>

InputReader::InputReader(std::vector<std::string>* sendQueue, std::mutex& mutex) : _mutex(mutex), _terminate(false), _sendQueue(sendQueue), checkIfError(false) {}

void InputReader::run()
{
    while (!_terminate) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);       
        _mutex.lock();
        _sendQueue->emplace_back(line);
        _mutex.unlock();
        if (line.compare("LOGOUT") == 0) {
            while (!checkIfError) {
            }
            checkIfError = false;
            std::this_thread::sleep_for(std::chrono::seconds(1));
        }
    }
}

void InputReader::Terminate()
{
    _terminate = true;
    checkIfError = true;
}
