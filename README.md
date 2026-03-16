# Codex Naturalis - Digital Board Game

**Team:** Matteo Morato, Silvia Parolin, Stefano Pedretti, Andrea Stagno

## 📌 Overview
This project was developed as the final exam for the Software Engineering course (A.A. 2023/2024). The aim is to implement a complete software version of the board game *"Codex Naturalis"* (produced by Cranio Creations) using Java. The implementation strictly follows the full content of the [official board game rulebook](https://www.craniocreations.it/storage/media/product_downloads/126/1516/CODEX_ITA_Rules_compressed.pdf).

## 🛠️ Technologies
* **Language:** Java
* **Graphics Library:** JavaFX
* **Networking:** TCP Sockets, Remote Method Invocation (RMI)
* **Core Concepts:** Model-View-Controller pattern, Client-Server architecture, Graphic/Textual User Interfaces, Multi-threading.

## 🚀 Key Features

* **Dual Interface:** Players can choose to interact with the game through a rich Graphical User Interface (GUI) or a lightweight Textual User Interface (TUI).
* **Versatile Connectivity:** The system supports two different types of network connections, allowing clients to connect via RMI or standard TCP Sockets.
* **Multiple Game Rooms:** As an advanced custom feature, the server architecture can concurrently handle multiple rooms, allowing different groups of players to run multiple independent games at the same time.

## 💻 Usage

To play the game using the provided `.jar` files, ensure you have a Java JRE installed, deactivate any active firewalls, and connect to a private network.

### 1. Start the Server

Open your command line and launch the server. It will print its IP address, which clients will need in order to connect.

```bash
# Launch the server application
java -jar Server.jar

```

### 2. Start the Clients

Open a new command line for each player (minimum 2, maximum 4 per room) and launch the client.

```bash
# Launch the client application
java -jar Client.jar

```

### 3. Configure and Play

* Choose between the GUI or TUI.
* Select your preferred connection type (RMI or TCP).
* Connect to the Server's IP address.
* Create a new room or join an existing one (make sure every client joins the correct room).
* Wait for the "Please wait for other players" message. Once everyone is in, enjoy the game!

---
