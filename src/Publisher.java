import java.io.*;
import java.net.*;


public class Publisher extends Thread {
    ObjectInputStream in;
    ObjectOutputStream out;
    BufferedWriter writer;  //And a writer to send messages.
    String profileName;
    String topic;
    Socket connection;
    BufferedReader keyboard;



    public Publisher(Socket connection , String topic , String profileName , ObjectOutputStream out,ObjectInputStream in) {
        try {

            this.profileName = profileName;
            this.connection = connection;
            this.topic = topic;
            this.out = out;
            this.in = in;

            System.out.println("Got a connection Publisher - Broker ...Opening streams....");


            //out = new ObjectOutputStream(connection.getOutputStream());
            //in = new ObjectInputStream(connection.getInputStream());
            this.keyboard = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            this.writer= new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }







    public void run() {
        try {

            System.out.println("Run");                    //OK

            // Dinoume to Connection Type ston broker
            out.writeObject(new SocketMessage("PUBLISHER_CONNECTION",new SocketMessageContent(profileName)));
            out.flush();

            System.out.println("Send ");                  //OK

           // in = new ObjectInputStream(connection.getInputStream());
            SocketMessage reply = (SocketMessage) in.readObject();

            System.out.println(reply.getContent().getMessage());


            keyboard = new BufferedReader(new InputStreamReader(System.in));
            String message;
            MultimediaFile m = null;
            Value v = null;

            if (reply.getType().equals("BROKER_CONNECTED")){
                while(true){
                    message = keyboard.readLine().trim();
                    if(message.equalsIgnoreCase("quit")) break;
                    push(topic,message);
                }
            }


            // Create a scanner for user input.
            //Scanner scanner = new Scanner(System.in);
            //System.out.println("What do you want to send (enter filepath) OR type 'quit' to disconnect : ");

               // File tempFile = new File(filename);

               //if(!tempFile.exists()){
                  //  System.out.println("ERROR: file " + tempFile.getName() + "  does not exists.");
               // }

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




    private void push(String topic,String message)  {

        try {
            if (!(message.startsWith("video") | message.startsWith("photo") | message.startsWith("txt")))   {
                out.writeObject(new SocketMessage("PUSH_STRING_MESSAGE",new SocketMessageContent(message)));
                out.flush();
            }
            else{

                //https://stackoverflow.com/questions/10824027/get-the-metadata-of-a-file
                //https://stackoverflow.com/questions/2168472/media-information-extractor-for-java


                MultimediaFile m = null;
                Value v = null;

                // Split String Message
                // First part is the file's type
                // Second part is the file's path
                // example : video C:\ Users\ user1\ Downloads\ whatsapp.mp4

                String[] arrOfStr = message.split(" ", 2);
                String file_path = arrOfStr[1];   //get the path

                File file         = new File(file_path);


                // Notify Broker that Publisher is going to send a file
                out.writeObject(new SocketMessage("PUSH_FILE",new SocketMessageContent(file.getName())));
                out.flush();

                byte[] chunk = new byte[512 * 1024]; //Creating the chunk array and setting how many bytes each chunk is.

                FileInputStream is = new FileInputStream(file); //Reading the file

                int rc = is.read(chunk); //Reading the first chunk of the file.
                while(rc != -1) { //This keeps reading and splitting the mp3 file until its completely read.

                    //Storing the information of the song in MultimediaFile and Value objects.
                    m = new MultimediaFile(file.getName(),profileName, file.length() , chunk);
                    v = new Value(m);
                    out.writeObject(v); //Sending the Value object through the ObjectOutputStream.
                    out.flush();
                    chunk = new byte[512 * 1024];
                    rc = is.read(chunk); //Reading next chunk.
                }
                is.close(); //Closing FileInputStream.
                m = new MultimediaFile("", "", 0, null); //Creates terminal musicFile.
                v = new Value(m);
                out.writeObject(v); //Sends terminal value.
                out.flush();

            }

        }catch (IOException e) {
        e.printStackTrace();
        }
    }

}
