package es.udc.gii.collectaball.robot.sensor;

import es.udc.gii.collectaball.robot.RobotComponent;
import es.udc.gii.collectaball.scenario.Scenario;



public abstract class RobotSensor extends RobotComponent {
    
    
    public abstract void sense(Scenario scenario);
    
}
