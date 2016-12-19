package es.udc.gii.collectaball.robot.sensor;

import es.udc.gii.callectaball.object.LoadableObject;
import es.udc.gii.collectaball.scenario.Scenario;

/**
 *
 * @author GII
 */


public class LoadSensor extends DoubleSensor {

    private LoadableObject load; 
    
    @Override
    public double scenarioSensorization(Scenario scenario) {
        return load == null ? 0.0 : 1.0;
    }
    
    public void load(LoadableObject load) {
        this.load = load;
    }
    
    public LoadableObject unload() {
        LoadableObject toReturn = load;
        this.load = null;
        return toReturn;
    }

    public LoadableObject getLoad() {
        return load;
    }
    
}
