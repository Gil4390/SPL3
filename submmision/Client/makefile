CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system

all: Client
	g++ -o bin/BGSclient bin/connectionHandler.o bin/EncoderDecoder.o bin/InputReader.o bin/Listener.o bin/Client.o $(LDFLAGS) 

Client: bin/connectionHandler.o bin/EncoderDecoder.o bin/InputReader.o bin/Listener.o bin/Client.o
	
bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/EncoderDecoder.o: src/EncoderDecoder.cpp
	g++ $(CFLAGS) -o bin/EncoderDecoder.o src/EncoderDecoder.cpp

bin/InputReader.o: src/InputReader.cpp
	g++ $(CFLAGS) -o bin/InputReader.o src/InputReader.cpp

bin/Listener.o: src/Listener.cpp
	g++ $(CFLAGS) -o bin/Listener.o src/Listener.cpp

bin/Client.o: src/Client.cpp
	g++ $(CFLAGS) -o bin/Client.o src/Client.cpp


	
.PHONY: clean
clean:
	rm -f bin/*