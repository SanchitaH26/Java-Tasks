
---

### **Beginner/README.md**
```markdown
# Task 1 - Enhanced Console-Based Calculator 

## Goal: 
Create a console calculator supporting basic arithmetic, scientific functions, and unit conversions. 

##  Run Instructions
```bash
javac ChatServer.java  ChatClient.java
java ChatServer




```
```bash
open other terminal and run
java ChatClientGUI
```
### **Features**

Group Chat:

All connected users can chat together in a shared room (All).

Messages include timestamps and usernames.

Private Chat (1:1 Messaging):

Send direct messages to specific users using /msg username message.

Private chats are stored separately from group chat history.

Unread message counts appear next to usernames in the user list until the chat is opened.

User List Management:

Real-time display of all connected users.

Updates dynamically when users join/leave.

“All” entry always represents the group chat.

File Sharing:

Send files to all connected users.

Files are transferred via sockets and saved locally as received_<filename>.

Transfer progress is automatic — no manual extraction needed.

Chat History:

Group chat and each private chat keep their own history.

Switching between chats displays the relevant history.

Notifications:

Join messages (username joined) and disconnects (username disconnected).

Unread counts shown beside usernames (e.g., Alice (2)).

Timestamps:

Every message and file transfer is timestamped ([HH:mm:ss]).

Cross-Platform GUI:

Built with Swing (JFrame, JList, JTextArea).

Split screen: left for chat, right for users.

Easy-to-use input box with Send and Send File buttons.

### **Expected Output**

Starting the Server

$ java ChatServer
Chat server started on port 12345


Connecting Clients

On launching ChatClientGUI, the user is prompted to enter a username.

The main window opens, showing the chat area (left) and user list (right).

Group Chat Example

[12:01:05] Alice joined the chat.
[12:01:10] Bob joined the chat.
[12:01:15] Alice: Hello everyone!
[12:01:20] Me: Hi Alice!


Private Chat Example

Alice selects "Bob" from the user list and sends: Hey Bob!

Bob sees:

[12:02:10] Alice (private): Hey Bob!


Alice sees in her private window:

[12:02:10] Me (to Bob): Hey Bob!


Unread Messages

If Bob is in the group chat and Alice sends him a private message, Bob’s user list shows:

Alice (1)


Once Bob opens the private chat with Alice, the counter resets.

File Sharing

Alice clicks Send File, selects report.pdf.

All other users automatically receive the file saved locally as:

received_report.pdf


In group chat:

[12:03:00] Me sent file: report.pdf
[12:03:05] Received file: report.pdf


User Disconnects

Bob disconnected.