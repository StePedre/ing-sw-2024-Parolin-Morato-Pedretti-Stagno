# Software Engineering Final Exam - Codex Naturalis
## Academic year 2023/2024
The aim of the project is to implement a software version of the board game "Codex Naturalis", produced by Cranio Creations, using Java.
### Group composition
The group responsible for the project is composed by (alfabetically): Morato Matteo, Parolin Silvia, Pedretti Stefano, Stagno Andrea.
### Game rules adopted
The implementation follows the full content of the official board game rulebook (https://www.craniocreations.it/storage/media/product_downloads/126/1516/CODEX_ITA_Rules_compressed.pdf).
### Implementation details
The project includes both Textual User Interface and Graphic User Interface. The graphics library used is JavaFX. 

It includes two types of different connection: Remote Method Invocation and Socket TCP.

It also includes one advanced functionality, which is specific for the software version: the possibility to create multiple rooms and therefore handle multiple games.
### Jar instructions
In order to make the project work through .jar files these are the steps to follow:
1) Setup your computer: make sure to have a Java JRE installed, deactivate every firewall and conncect to a private network.
2) Launch *Server.jar* from command line so that it can print its IP address. Each client must connect to this specific address.
     Note: the command used to launch jar files from cmd is: java -jar *filename*.
3) Launch *Client.jar* from command line. Choose between the two possible interfaces and the two possible connection types.
4) Follow the instructions on the chosen interface until the message "Please wait for other players" is displayed.
5) Run up to three more clients, as the minimum number of players to start a game is two and the maximum is four. Repeat step 3 for each client.
6) Enjoy the game!

**Additional instructions**

Make sure every client creates or joins the correct room. 
