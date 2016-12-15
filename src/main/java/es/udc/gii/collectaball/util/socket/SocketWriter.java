package es.udc.gii.collectaball.util.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mastropiero
 */


public class SocketWriter extends SocketCommunicator {

    private DataOutputStream dataOutputStream;

    public SocketWriter(String id) {
        super(id);
    }
    
    
    
    @Override
    public void init(int port, double minValue, double maxValue, double minNormValue, double maxNormValue) {
        super.init(port, minValue, maxValue, minNormValue, maxNormValue);
        try {
            dataOutputStream = new DataOutputStream(getSocket().getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(SocketWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   public void writeDouble(double d) {
        try {
            dataOutputStream.writeDouble(d);
        } catch (IOException ex) {
            Logger.getLogger(SocketWriter.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en socket " + getId());
        }
   }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }
    
   
    
}
