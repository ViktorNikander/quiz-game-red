package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.util.HashSet;
import java.util.Set;

public class Chat extends JPanel implements ActionListener {
    private String name;
    private JTextArea chatt = new JTextArea(20, 50);
    private JTextField input = new JTextField();
    private JButton sendButton = new JButton("Send");
    private MulticastSocket socket;
    private InetAddress ip;
    private int port;
    private Set<String> users = new HashSet<>();

    public Chat(String name, InetAddress ip, int port, MulticastSocket socket) {
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.socket = socket;

        setLayout(new BorderLayout());

        JScrollPane scroll = new JScrollPane(chatt);
        add(scroll, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(input, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        input.addActionListener(this);
        sendButton.addActionListener(this);

        users.add(name);
        sendMess("LOGON:" + name);

        new Thread(() -> {
            while (true) {
                try {
                    byte[] data = new byte[1024];
                    DatagramPacket p = new DatagramPacket(data, data.length);
                    socket.receive(p);

                    String receivedMess = new String(p.getData(), 0, p.getLength());

                    if (receivedMess.startsWith("LOGON:")) {
                        String parsedName = receivedMess.substring(receivedMess.indexOf(':') + 1);
                        users.add(parsedName);
                        chatt.append("[Sistema] " + parsedName + " has logged in\n");
                        sendMess("PINGBACK:" + name);

                    } else if (receivedMess.startsWith("PINGBACK:")) {
                        String parsedName = receivedMess.substring(receivedMess.indexOf(':') + 1);
                        if (!parsedName.equalsIgnoreCase(name)) {
                            users.add(parsedName);
                            chatt.append("[Sistema] " + parsedName + " is connected\n");
                        }

                    } else if (receivedMess.startsWith("LOGOFF:")) {
                        String parsedName = receivedMess.substring(receivedMess.indexOf(':') + 1);
                        users.remove(parsedName);
                        chatt.append("[Sistema] " + parsedName + " has logged out\n");

                    } else {
                        chatt.append(receivedMess + "\n");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private void sendMess(String mess) {
        DatagramPacket packet = new DatagramPacket(mess.getBytes(), mess.getBytes().length, ip, port);
        try {
            socket.send(packet);
            input.setText("");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String text = name + ": " + input.getText();
        if (!input.getText().trim().isEmpty()) {
            sendMess(text);
        }
    }
}
