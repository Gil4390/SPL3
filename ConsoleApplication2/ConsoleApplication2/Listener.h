#pragma once
#include "connectionHandler.h"

class Listener
{
public:
	Listener(ConnectionHandler* ch);
	void run();
	bool isTerminate();
	bool terminate;
private:
	ConnectionHandler* ch;
};