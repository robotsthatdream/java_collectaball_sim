package es.udc.gii.collectaball.robot.sensor;

import es.udc.gii.collectaball.util.socket.SocketWriter;
import java.util.HashMap;
import java.util.Map;



public abstract class MDBRobotSensor extends RobotSensor {
   
    private Map<String,SocketWriter> mdbSensors;

    public MDBRobotSensor() {
        mdbSensors = new HashMap<>();
    }

    public Map<String, SocketWriter> getMdbSensors() {
        return mdbSensors;
    }

    public void setMdbSensors(Map<String, SocketWriter> mdbSensors) {
        this.mdbSensors = mdbSensors;
    }

    
    
    
}
