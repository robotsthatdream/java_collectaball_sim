package es.udc.gii.collectaball.robot.sensor;

import es.udc.gii.collectaball.scenario.Scenario;
import es.udc.gii.collectaball.util.socket.SocketWriter;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author GII
 */
public class CrashSensor extends MDBRobotSensor {

    private double crash;
    private SocketWriter crashMDBSensor;

    public double getCrash() {
        return crash;
    }

    public void setCrash(double crash) {
        this.crash = crash;
    }

    @Override
    public void sense(Scenario scenario) {
        crashMDBSensor.writeDouble(crash);
    }

    @Override
    public void configure(Configuration configuration) {
        this.crash = 0.0;

        String idMDBSensor = configuration.getString("id");
        crashMDBSensor = new SocketWriter(idMDBSensor);
        getMdbSensors().put(crashMDBSensor.getId(), crashMDBSensor);
    }

    @Override
    public String lineLog() {
        return crash + "";
    }

    @Override
    public String headLog() {
        return getId() + "_crash";
    }

    
    
    
}
