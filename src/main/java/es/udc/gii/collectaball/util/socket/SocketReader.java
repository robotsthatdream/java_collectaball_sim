package es.udc.gii.collectaball.util.socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GII
 */


public class SocketReader extends SocketCommunicator {

    private DataInputStream dataInputStream;

    public SocketReader(String id) {
        super(id);
    }


    @Override
    public void init(int port, double minValue, double maxValue, double minNormValue, double maxNormValue) {
        super.init(port, minValue, maxValue, minNormValue, maxNormValue);
        try {
            dataInputStream = new DataInputStream(getSocket().getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(SocketReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public double readDouble() {
        try {
            return dataInputStream.readDouble();
        } catch (IOException ex) {
            Logger.getLogger(SocketReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
}
