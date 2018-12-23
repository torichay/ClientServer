package chatclient;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import static com.sun.deploy.uitoolkit.ToolkitStore.dispose;

public class ChatClient {

    JFrame frame;
    JTextArea incoming;
    JTextField outgoing;
    BufferedReader reader;
    PrintWriter writer;
    Socket sock;

    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        client.go();
    }

    public void go(){

        frame = new JFrame("Ludicrously Chat Client");
        JPanel mainPanel = new JPanel();
        incoming = new JTextArea(15,50);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        JScrollPane qScroller = new JScrollPane(incoming);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        outgoing = new JTextField(30);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new SendButtonListener());
        mainPanel.add(qScroller);
        mainPanel.add(outgoing);
        mainPanel.add(sendButton);
        setUpNetworking();

        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,400);
        frame.setVisible(true);
    }

    private void setUpNetworking(){
        try{
            sock = new Socket("127.0.0.1", 5151);
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(streamReader);
            writer = new PrintWriter(sock.getOutputStream());
            System.out.println("networking established");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public class SendButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent ev){
            String textMes = outgoing.getText();
            try{
                writer.println(textMes);
                writer.flush();
                if (textMes.equals("Buy")){
                    incoming.append(textMes + "\n");
                    System.exit(0);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            outgoing.setText("");
            outgoing.requestFocus();
        }
    }

    public class IncomingReader implements Runnable{
        public void run(){
            String message;
            try{
                while((message = reader.readLine()) != null){
                    System.out.println("write " + message);
                    incoming.append(message + "\n");
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

}
