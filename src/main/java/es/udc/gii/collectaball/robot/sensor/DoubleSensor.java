package es.udc.gii.collectaball.robot.sensor;

import es.udc.gii.collectaball.scenario.Scenario;
import es.udc.gii.collectaball.util.socket.SocketWriter;
import org.apache.commons.configuration.Configuration;
import org.slf4j.LoggerFactory;

/**
 *
 * @author GII
 */
public abstract class DoubleSensor extends MDBRobotSensor {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DoubleSensor.class);

    private SocketWriter doubleMDBSocket;

    private double value;

    @Override
    public void sense(Scenario scenario) {
        value = scenarioSensorization(scenario);

        if (value > doubleMDBSocket.getMaxValue()) {
            value = doubleMDBSocket.getMaxValue();
        } else if (value < doubleMDBSocket.getMinValue()) {
            value = doubleMDBSocket.getMinValue();
        }
        doubleMDBSocket.writeDouble(value);
    }

    public abstract double scenarioSensorization(Scenario scenario);

    @Override
    public void configure(Configuration configuration) {

        String idSensorization = configuration.getString("id");
        doubleMDBSocket = new SocketWriter(idSensorization);
        getMdbSensors().put(doubleMDBSocket.getId(), doubleMDBSocket);
    }

    public double getValue() {
        return value;
    }

    public SocketWriter getMDBSocket() {
        return doubleMDBSocket;
    }

    public String headLog() {
        return getId() + "_value";
    }

    public String lineLog() {
        return value + "";
    }

}
