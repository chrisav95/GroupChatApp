import java.io.*;
import java.net.*;
import java.util.Scanner;


//kathe fora pairnw ena connection kai to dinw se ena thread
//
public class Publisher extends Thread {
    ObjectInputStream in;
    ObjectOutputStream out;
    BufferedWriter writer;  //And a writer to send messages.
    String profileName;
    Socket connection;
    BufferedReader keyboard;
//ayta ta xreiazomaste apo tin meria tou broker gia na grapsoume sto client


    public Publisher(Socket connection , String topic ,String profileName) {
        try {

            this.profileName = profileName;
            this.connection = connection;
            System.out.println("Got a connection Publisher - Broker ...Opening streams....");


            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());
            this.keyboard = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            this.writer= new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }







    public void run() {
        try {

            // Dinoume to Connection Type ston broker
            out.writeObject(new SocketMessage("PUBLISHER_CONNECTION",new SocketMessageContent(profileName)));
            out.flush();


            in = new ObjectInputStream(connection.getInputStream());
            SocketMessage reply = (SocketMessage) in.readObject();

            keyboard = new BufferedReader(new InputStreamReader(System.in));
            String message;
            MultimediaFile m = null;
            Value v = null;

            if (reply.getType().equals("BROKER_CONNECTED")){
                while(true){
                    message = keyboard.readLine().trim();
                    if (message.startsWith("video") | message.startsWith("photo") | message.startsWith("txt"))   {
                        String[] arrOfStr = message.split(" ", 2);
                        String path = arrOfStr[1];
                        m = new MultimediaFile(path,profileName,"","",null);
                        v = new Value(m);
                        //push(topic,v);
                    }
                    //push();


                }

            }


            // Create a scanner for user input.
            //Scanner scanner = new Scanner(System.in);
            //System.out.println("What do you want to send (enter filepath) OR type 'quit' to disconnect : ");


            while(true) {
                // Ask user for the file they want to send.
                System.out.println("What do you want to send (enter filepath) OR type 'quit' to disconnect : ");
                String filename = keyboard.readLine();

                if(filename.equalsIgnoreCase("quit")) break;

                File tempFile = new File(filename);

                if(!tempFile.exists()){
                    System.out.println("ERROR: file " + tempFile.getName() + "  does not exists.");
                }


                //push(tempFile);




            }







        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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
    private void push(String topic,Value v)  {
        //MultimediaFile m = null;
        //Value v = null;

        try {


            // From AggelosProject
            byte[] chunk = new byte[512 * 1024]; //Creating the chunk array and setting how many bytes each chunk is.
            FileInputStream is = new FileInputStream(v.getMusicFile().getMultimediaFileName());
            int rc = is.read(chunk); //Reading the first chunk of the file.


            while(rc != -1) { //This keeps reading and splitting the mp3 file until its completely read.

                //Storing the information of the song in MusicFile and Value objects.
                m = new MultimediaFile(file.getName(),profileName, "", "", chunk);
                v = new Value(m);
                out.writeObject(v); //Sending the Value object through the ObjectOutputStream.
                out.flush();
                chunk = new byte[512 * 1024];
                rc = is.read(chunk); //Reading next chunk.
            }
            is.close(); //Closing FileInputStream.
            m = new MultimediaFile("", "", "", "", null); //Creates terminal musicFile.
            v = new Value(m);
            out.writeObject(v); //Sends terminal value.
            out.flush();

*/

/*
            //  Trying Alex's code
            byte totalBytes = 0;
            FileInputStream is = new FileInputStream(file);
            byte CHUNK_SIZE = (byte) (512);
            byte[] chunk = new byte[512 * 1024]; //Creating the chunk array and setting how many bytes each chunk is.
            //List<byte[]> chunks = new ArrayList<>();
            //keep file'w extension
            String extension = "";
            int i = file.getName().lastIndexOf('.');
            if (i >= 0) {
                extension = file.getName().substring(i+1);
            }

           i = 0;
            while(true) {

                // If we chunked all the file, break.
                if (totalBytes == file.length()) {
                    break;
                }

                if (totalBytes +  CHUNK_SIZE <= file.length()){


                }
            }



        }catch (IOException e) {
        e.printStackTrace();
        }

    }

*/

/*

    public void sendMessage() {
        try {
            // Initially send the username of the client.
            writer.write(profileName);
            writer.newLine();
            writer.flush();
            // Create a scanner for user input.
            Scanner scanner = new Scanner(System.in);
            // While there is still a connection with the server, continue to scan the terminal and then send the message.
            while (connection.isConnected()) {
                String messageToSend = scanner.nextLine();

                //edw elegxoume an grapsei "send media : "

                writer.write(profileName + ": " + messageToSend);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            // Gracefully close everything.
            //disconnect();
        }
    }


*/

}
