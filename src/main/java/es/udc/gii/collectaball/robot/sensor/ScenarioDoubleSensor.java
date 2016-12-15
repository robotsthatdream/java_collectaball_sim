package es.udc.gii.collectaball.robot.sensor;

import es.udc.gii.collectaball.scenario.Scenario;
import es.udc.gii.collectaball.util.socket.SocketReader;

/**
 *
 * @author Mastropiero
 */


public class ScenarioDoubleSensor extends DoubleSensor{

    private SocketReader scenarioSocket;
    
    @Override
    public double scenarioSensorization(Scenario scenario) {
        return scenarioSocket.readDouble();
    }
    
    
    
}
