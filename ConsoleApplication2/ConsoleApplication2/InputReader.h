#pragma once
#include <string>
#include <vector>
#include <mutex>
class InputReader
{
private:
	std::mutex& _mutex;
	bool _terminate;

public:
	InputReader(std::vector<std::string>* sendQueue, std::mutex& mutex);
	std::vector<std::string>* _sendQueue;
	void run();
	void Terminate();
};

