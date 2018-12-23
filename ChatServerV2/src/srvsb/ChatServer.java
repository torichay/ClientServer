package srvsb;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.System.exit;
import static java.lang.System.setOut;

public class ChatServer {

    ArrayList clientOutputStreams;
    Connection connect = ConnectionSingleton.getInstance();
    DatabaseWork incomMes = new DatabaseWork(connect);

    public class ClientHandler implements Runnable{
        BufferedReader reader;
        Socket sock;

        public ClientHandler(Socket clientSocket){
            try{
                sock = clientSocket;
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(isReader);
            } catch (Exception ex) {ex.printStackTrace(); }
        }

        public void run(){
            String timeStamp = new SimpleDateFormat("dd.MM.yyyy  HH:mm:ss").format(Calendar.getInstance().getTime());
            String message;
            try{
                while ((message = reader.readLine()) != null){
                    System.out.println("read " + message);
                    if (message.equals("Buy")){
                        System.out.println("Good Buy");
                        sock.close();
                    }
                    sendToDatabase(timeStamp, message);
                    tellEveryone();
                }
            } catch (Exception ex) {//ex.printStackTrace();
                System.out.println("There aren't any connections"); }
        }
    }

    public static void main(String[] args) {
        new ChatServer().go();
    }

    public void go(){
        clientOutputStreams = new ArrayList();
        try{
            ServerSocket serverSock = new ServerSocket(5151);

            while(true){
                Socket clientSocket = serverSock.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                clientOutputStreams.add(writer);

                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
                getHistory(clientSocket);
                System.out.println("got a connection");
            }
        } catch (Exception ex) {ex.printStackTrace(); }
    }

    public void sendToDatabase(String time, String text){
        try {
            incomMes.insertMessage(time, text);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void tellEveryone(){
        Iterator it = clientOutputStreams.iterator();
        while(it.hasNext()){
            try{
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(incomMes.selectMessage());
                writer.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void getHistory(Socket client){
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(client.getOutputStream());
            writer.println(incomMes.selectHistory());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        writer.flush();

    }
}
