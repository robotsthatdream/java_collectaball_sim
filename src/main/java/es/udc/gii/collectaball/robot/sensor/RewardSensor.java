package es.udc.gii.collectaball.robot.sensor;

import es.udc.gii.collectaball.scenario.Scenario;
import es.udc.gii.collectaball.util.socket.SocketWriter;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author GII
 */
public class RewardSensor extends MDBRobotSensor {

    private double reward;
    private SocketWriter rewardMDBSensor;

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    @Override
    public void sense(Scenario scenario) {
        rewardMDBSensor.writeDouble(reward);
    }

    @Override
    public void configure(Configuration configuration) {
        this.reward = configuration.getDouble("reward");

        String idMDBSensor = configuration.getString("id");
        rewardMDBSensor = new SocketWriter(idMDBSensor);
        getMdbSensors().put(rewardMDBSensor.getId(), rewardMDBSensor);
    }

    @Override
    public String lineLog() {
        return reward + "";
    }

    @Override
    public String headLog() {
        return getId() + "_reward";
    }

    
    
    
}
