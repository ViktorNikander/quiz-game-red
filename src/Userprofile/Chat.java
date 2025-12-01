package Userprofile;

import javax.swing.*;
import javax.net.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashSet;
import java.util.Set;

public class Chat extends JFrame implements ActionListener {
    private String name;
    private JTextArea chatt = new JTextArea(20,50);
    private JTextArea userList = new JTextArea(20,20);
    private JTextField input = new JTextField();
    private MulticastSocket socket;
    private InetAddress ip;
    private int port;
    private Set<String> users = new HashSet<>();

    public Chat(String name, InetAddress ip, int port, MulticastSocket socket){
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.socket = socket;

        setTitle("Chat:" + name);
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        JScrollPane scroll = new JScrollPane(chatt);
        panel.add(scroll,BorderLayout.CENTER);
        panel.add(input, BorderLayout.SOUTH);
        panel.add(userList, BorderLayout.EAST);

        input.addActionListener(this);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                sendMess("LOGOFF" + name);
            }
        });

        users.add(name);
        sendMess("LOGON:" + name);
        setSize(700,500);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        new Thread(()->{
            while(true){
                try{
                    byte[] data = new byte[1024];
                    DatagramPacket p = new DatagramPacket(data,data.length);
                    socket.receive(p);

                    String receivedMess = new String(p.getData(),0,p.getLength());

                    if (receivedMess.startsWith("LOGON:")){
                        String parsedName = receivedMess.substring(receivedMess.indexOf(':') + 1);
                        users.add(parsedName);
                        updateUserList();
                        sendMess("PINGBACK:" + name);
                    } else if (receivedMess.startsWith("PINGBACK:")) {
                        String parsedName = receivedMess.substring(receivedMess.indexOf(':') + 1);
                        if(!parsedName.equalsIgnoreCase(name)){
                            users.add(parsedName);
                            updateUserList();
                        }

                    } else if (receivedMess.startsWith("LOGOFF:")) {
                        String parsedName = receivedMess.substring(receivedMess.indexOf(':') +1);
                        users.remove(parsedName);
                        updateUserList();

                    }else{
                        chatt.append(receivedMess + "\n");
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }).start();
    }

    private void updateUserList() {
        userList.setText("");
        for(String n : users){
            userList.append(n + "\n");
        }
    }

    private void sendMess(String mess) {
        DatagramPacket packet = new DatagramPacket(mess.getBytes(), mess.getBytes().length, ip ,port);
        try{
            socket.send(packet);
            input.setText("");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e){
        String text = name + ":" + input.getText();
        sendMess(text);
    }


}