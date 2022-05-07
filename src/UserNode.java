import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.io.*;

//https://stackoverflow.com/questions/61639703/read-and-write-object-on-a-socket-java
// For pass an object with a socket, the class and the package needs to be the same, and then you need to set the same serialVersionUID


public class UserNode implements Node{
    private static BufferedReader keyboard;
    private static Socket requestSocket;
    private PrintWriter initializeQuery;
    //private BufferedWriter writer;
    //private BufferedReader out;

    private ObjectInputStream input;
    private ObjectOutputStream output;

    // private ObjectInputStream inB;
    private static int port;
    private static String profileName;
    private static String topic;

    public UserNode(String profileName) {
        this.profileName = profileName;
    }




    public static void main(String[] args) throws IOException{

        System.out.println("Enter profile name: ");

        keyboard = new BufferedReader(new InputStreamReader(System.in));
        profileName = keyboard.readLine();
        UserNode user = new UserNode(profileName);

        user.connect();  //Συνδεση του UserNode με εναν τυχαίο Broker
        //System.out.println("mpika  ston broker");

        port = user.init(getSocket().getPort());

        //
        // System.out.println(port);
        Thread consumer = new Consumer(getSocket(),topic,profileName);
        consumer.start();

        Thread publisher = new Publisher(getSocket(),topic,profileName);
        publisher.start();

        System.out.println("Bye!");
    }


    @Override
    public int init(int port) throws UnknownHostException, IOException {
        try {


            // Dinoume to Connection Type ston broker
            output.writeObject(new SocketMessage("USER_CONNECTION",new SocketMessageContent(profileName)));
            output.flush();


            System.out.println("Available group-chats/topics to enter: ");  //printing groups/topics for which a broker is responsible
            //Pairnei apo ton Broker tin lista me to topics pou yparxoun
            input = new ObjectInputStream(requestSocket.getInputStream());
            SocketMessage reply = (SocketMessage) input.readObject();

            /**
             * The topic list from broker
             */
            if (reply.getType() == "TOPIC_LIST") {
                System.out.print(reply.getContent().getMessage());
            }

            /*
            for (String topic : topics) {
                System.out.println(topic);
            }
            */

            System.out.println("Type the name of an available group-chat/topic (type 'quit' to disconnect): ");

            while (true) {

                //String
                topic = keyboard.readLine().trim();

                // Ask broker for topic info.
                output.writeObject(new SocketMessage("USER_TOPIC_LOOKUP",new SocketMessageContent(topic)));
                output.flush();


                //input = new ObjectInputStream(requestSocket.getInputStream());
                reply = (SocketMessage) input.readObject();
                System.out.println(reply.getType());



                if (topic.equals("quit")) { //Terminal message
                    initializeQuery.println(topic); //Sends terminal message to Broker so that he can disconnect and terminate the Thread
                    disconnect(); //Disconnecting from the Broker
                    break;
                }

                /**
                 * The topic we're interested in does not exists.
                 */
                if (reply.getType() == "USER_TOPIC_DOES_NOT_EXIST"){
                    // Pick a different topic and ask the broker again.
                    System.out.println("Topic does not exist ! Type the name of an available group-chat/topic (type 'quit' to disconnect): ");

                }
                /**
                 * The topic we're interested in is managed by a different broker.
                 */
                else if (reply.getType() == "USER_TOPIC_LOOKUP_REDIRECT"){
                    // Get host and port for the correct broker.
                    System.out.println("Redirecting to the right broker.");
                    disconnect();
                    port = reply.getContent().getPort();
                    connect(port);
                    return port;
                }
                /**
                 * We're already connected to the correct broker.
                 */
                else if (reply.getType() == "USER_TOPIC_LOOKUP_SUCCESS"){
                    return port;
                }

            }

        } catch(UnknownHostException unknownHost){
            System.err.println("You are trying to connect to an unknown host!");
        } catch(IOException ioException){
            ioException.printStackTrace();
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        return port;
    }



    @Override
    public List<Broker> getBrokers() {
        return null;
    }


    @Override
    public void updateNodes() {

    }


    public void connect() { //Connects to a random Broker and initializes sockets, readers/writers and I/O streams
        try {
            if(requestSocket == null) {
                Random randGen = new Random();
                int random = randGen.nextInt(3);
                if(random == 0) {
                    requestSocket = new Socket(ip, FIRSTBROKER);
                }else if(random == 1) {
                    requestSocket = new Socket(ip, SECONDBROKER);
                }else {
                    requestSocket = new Socket(ip, THIRDBROKER);
                }
            }
            keyboard = new BufferedReader(new InputStreamReader(System.in));

            output = new ObjectOutputStream(requestSocket.getOutputStream());

            //System.out.println("mpika");
            //input = new ObjectInputStream(requestSocket.getInputStream());

            //System.out.println("mpika ");
            // input = new InputStreamReader(requestSocket.getInputStream());

            //out = new BufferedReader(input);

            initializeQuery = new PrintWriter(requestSocket.getOutputStream(), true);

            //inB = new ObjectInputStream(requestSocket.getInputStream());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect(int port) { //Connects to a Broker and initializes sockets, readers/writers and I/O streams
        try {
            requestSocket = new Socket(ip, port);



            output = new ObjectOutputStream(requestSocket.getOutputStream());
            input = new ObjectInputStream(requestSocket.getInputStream());
            //input = new InputStreamReader(requestSocket.getInputStream());
            //out = new BufferedReader(input);
            initializeQuery = new PrintWriter(requestSocket.getOutputStream(), true);
            //inB = new ObjectInputStream(requestSocket.getInputStream());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Disconnects from a Broker and closes sockets, readers/writers and I/O streams/
    public void disconnect() {
        // Note you only need to close the outer wrapper as the underlying streams are closed when you close the wrapper.
        // Note you want to close the outermost wrapper so that everything gets flushed.
        // Note that closing a socket will also close the socket's InputStream and OutputStream.
        // Closing the input stream closes the socket. You need to use shutdownInput() on socket to just close the input stream.
        // Closing the socket will also close the socket's input stream and output stream.
        // Close the socket after closing the streams.
        try {
            if (output != null) {
                output.close();
            }
            if (input != null) {
                input.close();
            }
            if (requestSocket != null) {
                requestSocket.close();
            }
            initializeQuery.close();
            //inB.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static Socket getSocket() {
        return requestSocket;
    }
}
