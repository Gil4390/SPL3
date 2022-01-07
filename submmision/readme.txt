1.1 
TPC: mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.TPCMain" -Dexec.args=7777
Reactor: mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.ReactorMain" -Dexec.args="7777 5"

1.2

all commands should be in caps, 
the birthday is in the format: dd-mm-yyyy

REGISTER A 123 01-01-2000
LOGIN A 123 1
LOGOUT
FOLLOW 0 B
FOLLOW 1 B
POST example post
PM B example pm
LOGSTAT
STAT B
BLOCK B


2
you can change the list of filtered words in the constructor of the class Twitter in the variable filteredWords
src/main/java/bgu/spl/net/srv/Twitter.java