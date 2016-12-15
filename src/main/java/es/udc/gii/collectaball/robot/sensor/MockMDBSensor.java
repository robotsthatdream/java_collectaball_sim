package es.udc.gii.collectaball.robot.sensor;

import es.udc.gii.collectaball.scenario.Scenario;
import es.udc.gii.collectaball.util.socket.SocketWriter;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author Mastropiero
 */
public class MockMDBSensor extends MDBRobotSensor {

    @Override
    public void sense(Scenario scenario) {
        for (SocketWriter rss : getMdbSensors().values()) {
            rss.writeDouble(0.0);
        }
    }

    @Override
    public void configure(Configuration configuration) {

    }

    @Override
    public String headLog() {
        return "";
    }

    @Override
    public String lineLog() {
        return "";
    }

}
