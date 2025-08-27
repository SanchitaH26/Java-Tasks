import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345;
    private static Set<ClientHandler> clients = new HashSet<>();
    private static Map<String, ClientHandler> userMap = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Chat server started on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket s = serverSocket.accept();
                ClientHandler handler = new ClientHandler(s);
                handler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   
    private static void broadcast(String message, ClientHandler exclude) {
        synchronized (clients) {
            for (ClientHandler c : clients) {
                if (c != exclude) {
                    c.sendLine(message);
                }
            }
        }
    }

    
    private static void updateUserList() {
        StringBuilder sb = new StringBuilder();
        synchronized (userMap) {
            for (String u : userMap.keySet()) {
                sb.append(u).append(",");
            }
        }
        String listMsg = "/users " + sb.toString();
        synchronized (clients) {
            for (ClientHandler c : clients) {
                c.sendLine(listMsg);
            }
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private DataOutputStream dataOut;
        private DataInputStream dataIn;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        private void sendLine(String line) {
            if (out != null) {
                out.println(line);
                out.flush();
            }
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                dataOut = new DataOutputStream(socket.getOutputStream());
                dataIn = new DataInputStream(socket.getInputStream());

                
                username = in.readLine();
                if (username == null) {
                    socket.close();
                    return;
                }

                synchronized (clients) { clients.add(this); }
                synchronized (userMap) { userMap.put(username, this); }

                broadcast("📢 " + username + " joined the chat.", this);
                updateUserList();

                String message;
                while ((message = in.readLine()) != null) {
                    
                    if (message.startsWith("/msg ")) {
                        String[] parts = message.split(" ", 3);
                        if (parts.length >= 3) {
                            String recipient = parts[1];
                            String msgContent = parts[2];
                            String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
                            String formatted = "[" + time + "] " + username + " (private): " + msgContent;

                            ClientHandler target;
                            synchronized (userMap) { target = userMap.get(recipient); }
                            if (target != null) {
                                
                                target.sendLine(formatted);
                               
                            } else {
                                
                                sendLine("[Server] user not found: " + recipient);
                            }
                        } else {
                            sendLine("[Server] Invalid private message format. Use: /msg username message");
                        }
                    }
                
                    else if (message.startsWith("FILE:")) {
                        String[] parts = message.split(":", 3);
                        if (parts.length >= 3) {
                            String fileName = parts[1];
                            int length;
                            try {
                                length = Integer.parseInt(parts[2]);
                            } catch (NumberFormatException e) {
                                sendLine("[Server] Invalid file length.");
                                continue;
                            }

                            byte[] fileData = new byte[length];
                            int read = 0;
                            while (read < length) {
                                int r = dataIn.read(fileData, read, length - read);
                                if (r == -1) break;
                                read += r;
                            }

                           
                            broadcastFile(fileName, fileData, this);
                        } else {
                            sendLine("[Server] Invalid FILE header.");
                        }
                    }
                  
                    else {
                        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
                        String formatted = "[" + time + "] " + username + ": " + message;
                        broadcast(formatted, this);
                    }
                }
            } catch (IOException e) {
               
            } finally {
                try {
                    if (username != null) {
                        synchronized (clients) { clients.remove(this); }
                        synchronized (userMap) { userMap.remove(username); }
                        broadcast("❌ " + username + " disconnected.", this);
                        updateUserList();
                    }
                    socket.close();
                } catch (IOException e) {
                    
                }
            }
        }

        
        private void broadcastFile(String fileName, byte[] fileData, ClientHandler sender) {
            synchronized (clients) {
                for (ClientHandler c : clients) {
                    if (c != sender) {
                        try {
                            c.sendLine("FILE:" + fileName + ":" + fileData.length);
                            c.dataOut.write(fileData);
                            c.dataOut.flush();
                        } catch (IOException e) {
                            System.out.println("Error sending file to " + c.username + ": " + e.getMessage());
                        }
                    }
                }
            }
        }
    }
}
