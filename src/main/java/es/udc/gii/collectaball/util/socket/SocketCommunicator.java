package es.udc.gii.collectaball.util.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mastropiero
 */


public abstract class SocketCommunicator {
 
    private String id;
    private ServerSocket serverSocket;
    private Socket socket;
    private int port;
    private double minValue, maxValue;
    private double minNormValue, maxNormValue; 

    public SocketCommunicator(String id) {
        this.id = id;
    }
    
    
    public void init(int port, double minValue, double maxValue, double minNormValue, double maxNormValue) {
        try {
            this.port = port;
            this.serverSocket = new ServerSocket(this.port);
            this.socket = serverSocket.accept();
            this.minNormValue = minNormValue;
            this.maxNormValue = maxNormValue;
            this.minValue = minValue;
            this.maxValue = maxValue;
            System.out.println("Robot " + port);
        } catch (IOException ex) {
            Logger.getLogger(SocketCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    public int getPort() {
        return port;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public Socket getSocket() {
        return socket;
    }

    public double getMaxNormValue() {
        return maxNormValue;
    }

    public String getId() {
        return id;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public double getMinNormValue() {
        return minNormValue;
    }

    public double getMinValue() {
        return minValue;
    }

    
}
