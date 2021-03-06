import java.io.*;
import java.net.*;
import java.util.Scanner;


//kathe fora pairnw ena connection kai to dinw se ena thread
//
public class Consumer extends Thread {
    ObjectInputStream in;
    ObjectOutputStream out;
    BufferedWriter writer;  //And a writer to send messages.
    String profileName;
    Socket connection;
    String topic;
    BufferedReader keyboard;




    public Consumer(Socket connection , String topic) {
        try {

            this.profileName = profileName;
            this.connection = connection;
            this.topic = topic;
            System.out.println("Got a connection Consumer - Broker ...Opening streams....");


            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());



            /*fakelo tha ftiaxnoume gia kathe topic kai oxi gia kathe user
            // The folder we're gonna save all the files that the brokers sent us.
            File theDir = new File("./History/" + profileName);
            if (!theDir.exists()){
                theDir.mkdirs();
            }
            */


        } catch (IOException e) {
            e.printStackTrace();
        }
    }







    public void run() {
        try {

            // Intermediate queue to hold chunks.


            // // Pull the topic's history.
            out.writeObject(new SocketMessage("USER_PULL_TOPIC",new SocketMessageContent(topic)));
            out.flush();


            in = new ObjectInputStream(connection.getInputStream());
            SocketMessage reply = (SocketMessage) in.readObject();

            while(true) {
                // Listen for broker messages.
                if (reply.getType() == "USER_TOPIC_FULL_HISTORY") {
                    //prepei na parw olo to istoriko gia to sugkekrimeno topic
                }

                if (reply.getType() == "USER_MULTIMEDIA_CHUNK") {
                    // Get info from the message.


                }



            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }



    /*
    // Listening for a message is blocking so need a separate thread for that.
    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
                // While there is still a connection with the server, continue to listen for messages on a separate thread.
                while (socket.isConnected()) {
                    try {
                        // Get the messages sent from other users and print it to the console.
                        msgFromGroupChat = bufferedReader.readLine();
                        System.out.println(msgFromGroupChat);
                    } catch (IOException e) {
                        // Close everything gracefully.
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }




     */


}
