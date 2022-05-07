import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.io.*;
import java.net.*;

/*
 * Node is the interface for the classes Publisher, Broker, Consumer.
 * All instances of the classes that extend Node have access to all the
 * ports/ip that each Publisher/Broker uses.
 */

public interface Node {
    public static final String ip = "LocalHost";
    public static final int FIRSTPUBLISHER = 4321;
    public static final int SECONDPUBLISHER = 3421;
    public static final int THIRDPUBLISHER = 5000;
    public static final int FIRSTBROKER = 4000;
    public static final int SECONDBROKER = 5555;
    public static final int THIRDBROKER = 5984;
    public static List<Broker> brokers = new ArrayList<>();

    public int init(int port) throws UnknownHostException, IOException, NoSuchAlgorithmException, ClassNotFoundException;

    public List<Broker> getBrokers();

    public void connect();

    public void disconnect();

    public void updateNodes();
}