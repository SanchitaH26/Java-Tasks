import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChatClientGUI {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 12345;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private DataInputStream dataIn;

    private JFrame frame;
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton fileButton;

    private DefaultListModel<String> userListModel;
    private JList<String> userList;
    private String selectedUser = null;   
    private String myUsername = null;

    private final java.util.List<String> groupHistory = new ArrayList<>();
    private final Map<String, java.util.List<String>> privateHistories = new HashMap<>();

    private final Map<String, Integer> unreadCounts = new HashMap<>();

    public ChatClientGUI() {
        frame = new JFrame("Chat Client");
        chatArea = new JTextArea(20, 40);
        chatArea.setEditable(false);
        messageField = new JTextField(28);
        sendButton = new JButton("Send");
        fileButton = new JButton("Send File");

        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.add(messageField);
        inputPanel.add(sendButton);
        inputPanel.add(fileButton);
        leftPanel.add(inputPanel, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel("Users"), BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(userList), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        split.setResizeWeight(0.8);
        frame.getContentPane().add(split);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());
        fileButton.addActionListener(e -> sendFile());

       
        userList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String val = userList.getSelectedValue();
                if (val == null || "All".equals(stripCount(val))) {
                    switchToGroup();
                } else {
                    switchToPrivate(stripCount(val));
                }
            }
        });
    }

    private String now() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (message.isEmpty()) return;

        if (selectedUser != null) {
           
            out.println("/msg " + selectedUser + " " + message);
            String display = "[" + now() + "] Me (to " + selectedUser + "): " + message;
            addPrivateMessageLocal(selectedUser, display);
        } else {
            
            out.println(message);
            String display = "[" + now() + "] Me: " + message;
            addGroupMessageLocal(display);
        }
        messageField.setText("");
    }

    private void addPrivateMessageLocal(String user, String msg) {
        synchronized (privateHistories) {
            privateHistories.computeIfAbsent(user, k -> new ArrayList<>()).add(msg);
        }
        if (selectedUser != null && selectedUser.equals(user)) {
            appendToChat(msg);
        }
    }

    private void addGroupMessageLocal(String msg) {
        synchronized (groupHistory) {
            groupHistory.add(msg);
        }
        if (selectedUser == null) {
            appendToChat(msg);
        }
    }

    private void sendFile() {
        JFileChooser chooser = new JFileChooser();
        int res = chooser.showOpenDialog(frame);
        if (res != JFileChooser.APPROVE_OPTION) return;
        File file = chooser.getSelectedFile();
        if (file == null || !file.exists()) return;

        try (FileInputStream fis = new FileInputStream(file)) {
            long length = file.length();
            out.println("FILE:" + file.getName() + ":" + length);
            out.flush();

            OutputStream os = socket.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush();

            String display = "[" + now() + "] Me sent file: " + file.getName();
            addGroupMessageLocal(display);
        } catch (Exception ex) {
            ex.printStackTrace();
            appendToChat("[Error] Failed to send file: " + ex.getMessage());
        }
    }

    private void appendToChat(String line) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(line + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }

    private void displayGroupHistory() {
        SwingUtilities.invokeLater(() -> {
            chatArea.setText("");
            synchronized (groupHistory) {
                for (String s : groupHistory) chatArea.append(s + "\n");
            }
            frame.setTitle("Chat Client - [Group Chat]");
        });
    }

    private void displayPrivateHistory(String user) {
        SwingUtilities.invokeLater(() -> {
            chatArea.setText("");
            java.util.List<String> list;
            synchronized (privateHistories) {
                list = privateHistories.getOrDefault(user, new ArrayList<>());
            }
            for (String s : list) chatArea.append(s + "\n");
            frame.setTitle("Chat Client - [Private: " + user + "]");
        });
    }

    private void switchToGroup() {
        selectedUser = null;
        unreadCounts.put("All", 0);  // reset group unread counter
        refreshUserListDisplay();
        SwingUtilities.invokeLater(() -> {
            int idx = userListModel.indexOf("All");
            if (idx >= 0) userList.setSelectedIndex(idx);
            displayGroupHistory();
        });
    }

    private void switchToPrivate(String user) {
        selectedUser = user;
        unreadCounts.put(user, 0); // reset private unread counter
        refreshUserListDisplay();
        SwingUtilities.invokeLater(() -> {
            if (userListModel.indexOf(user) < 0) {
                userListModel.addElement(user);
            }
            int idx = userListModel.indexOf(user);
            if (idx >= 0) userList.setSelectedIndex(idx);
            displayPrivateHistory(user);
        });
    }

    private void refreshUserListDisplay() {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < userListModel.size(); i++) {
                String base = stripCount(userListModel.get(i));
                int count = unreadCounts.getOrDefault(base, 0);
                String display = (count > 0) ? base + " (" + count + ")" : base;
                userListModel.set(i, display);
            }
        });
    }

    private String stripCount(String val) {
        return val.replaceAll("\\s*\\(\\d+\\)$", "");
    }

    private void start() throws IOException {
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        dataIn = new DataInputStream(socket.getInputStream());

        myUsername = JOptionPane.showInputDialog(frame, "Enter your username:");
        if (myUsername == null || myUsername.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Username cannot be empty. Exiting.");
            System.exit(0);
        }
        out.println(myUsername);

        SwingUtilities.invokeLater(() -> {
            userListModel.clear();
            userListModel.addElement("All");
            userList.setSelectedIndex(0);
            frame.setVisible(true);
        });

        new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    final String incoming = msg;

                    if (incoming.startsWith("/users ")) {
                        String listPart = incoming.substring(7);
                        String[] users = listPart.split(",");

                        SwingUtilities.invokeLater(() -> {
                            String prev = selectedUser;
                            userListModel.clear();
                            userListModel.addElement("All");
                            for (String u : users) {
                                u = u.trim();
                                if (u.isEmpty()) continue;
                                if (u.equals(myUsername)) continue;
                                int count = unreadCounts.getOrDefault(u, 0);
                                userListModel.addElement(count > 0 ? u + " (" + count + ")" : u);
                            }
                            if (prev == null) {
                                userList.setSelectedIndex(0);
                                selectedUser = null;
                                displayGroupHistory();
                            } else {
                                int idx = -1;
                                for (int i = 0; i < userListModel.size(); i++) {
                                    if (stripCount(userListModel.get(i)).equals(prev)) {
                                        idx = i;
                                        break;
                                    }
                                }
                                if (idx >= 0) {
                                    userList.setSelectedIndex(idx);
                                    displayPrivateHistory(prev);
                                } else {
                                    userList.setSelectedIndex(0);
                                    selectedUser = null;
                                    displayGroupHistory();
                                }
                            }
                        });
                    }

                    else if (incoming.startsWith("/openpm ")) {
                        String opener = incoming.substring(8).trim();
                        synchronized (privateHistories) {
                            privateHistories.computeIfAbsent(opener, k -> new ArrayList<>());
                        }
                        switchToPrivate(opener);
                    }

                    else if (incoming.startsWith("FILE:")) {
                        String[] parts = incoming.split(":", 3);
                        if (parts.length >= 3) {
                            String fileName = parts[1];
                            int length = Integer.parseInt(parts[2]);
                            byte[] fileData = new byte[length];
                            int read = 0;
                            while (read < length) {
                                int r = dataIn.read(fileData, read, length - read);
                                if (r == -1) break;
                                read += r;
                            }
                            FileOutputStream fos = new FileOutputStream("received_" + fileName);
                            fos.write(fileData);
                            fos.close();

                            String display = "[" + now() + "] Received file: " + fileName;
                            synchronized (groupHistory) { groupHistory.add(display); }
                            if (selectedUser == null) appendToChat(display);
                            else {
                                unreadCounts.put("All", unreadCounts.getOrDefault("All", 0) + 1);
                                refreshUserListDisplay();
                            }
                        }
                    }

                    else {
                        
                        if (incoming.contains(" (private):")) {
                            int idxBracket = incoming.indexOf("] ");
                            if (idxBracket != -1) {
                                int nameStart = idxBracket + 2;
                                int idxPrivate = incoming.indexOf(" (private):", nameStart);
                                if (idxPrivate > nameStart) {
                                    String sender = incoming.substring(nameStart, idxPrivate);
                                    synchronized (privateHistories) {
                                        privateHistories.computeIfAbsent(sender, k -> new ArrayList<>()).add(incoming);
                                    }
                                    if (selectedUser != null && selectedUser.equals(sender)) {
                                        appendToChat(incoming);
                                    } else {
                                        unreadCounts.put(sender, unreadCounts.getOrDefault(sender, 0) + 1);
                                        refreshUserListDisplay();
                                    }
                                    continue;
                                }
                            }
                            synchronized (groupHistory) { groupHistory.add(incoming); }
                            if (selectedUser == null) appendToChat(incoming);
                            else {
                                unreadCounts.put("All", unreadCounts.getOrDefault("All", 0) + 1);
                                refreshUserListDisplay();
                            }
                        } else {
                            synchronized (groupHistory) { groupHistory.add(incoming); }
                            if (selectedUser == null) appendToChat(incoming);
                            else {
                                unreadCounts.put("All", unreadCounts.getOrDefault("All", 0) + 1);
                                refreshUserListDisplay();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                appendToChat("Connection closed.\n");
            }
        }).start();
    }

    public static void main(String[] args) throws Exception {
        ChatClientGUI client = new ChatClientGUI();
        client.start();
    }
}
