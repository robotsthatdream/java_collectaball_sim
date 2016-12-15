package es.udc.gii.collectaball.robot.sensor;

import es.udc.gii.callectaball.object.Wall;
import es.udc.gii.callectaball.object.Door;
import es.udc.gii.callectaball.object.ColoredObject;
import es.udc.gii.collectaball.scenario.Scenario;
import es.udc.gii.callectaball.object.ScenarioObject;
import es.udc.gii.collectaball.util.socket.SocketWriter;
import javax.vecmath.Vector2d;
import org.apache.commons.configuration.Configuration;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mastropiero
 */
public class DistanceSinCosSensor extends MDBRobotSensor {
    
    

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DistanceSinCosSensor.class);

    private static final double SCOPE_THRESHOLD = 1.0e-6;

    private String color;
    /**
     * Posición relativa
     */
    private double position;
    private double range;
    private double scope;
    private SocketWriter sinAngleMDBSensor;
    private SocketWriter cosAngleMDBSensor;
    private SocketWriter distanceMDBSensor;

    private double[] value;

    public DistanceSinCosSensor() {
    }

    
    
    public DistanceSinCosSensor(String color, double position, double range, double scope) {
        this.color = color;
        this.position = position;
        this.range = range;
        this.scope = scope;
    }

    
    
    @Override
    public void sense(Scenario scenario) {
        value = scenarioSensorization(scenario);

        if (value[0] > sinAngleMDBSensor.getMaxValue()) {
            value[0] = sinAngleMDBSensor.getMaxValue();
        } else if (value[0] < sinAngleMDBSensor.getMinValue()) {
            value[0] = sinAngleMDBSensor.getMinValue();
        }

        if (value[1] > cosAngleMDBSensor.getMaxValue()) {
            value[1] = cosAngleMDBSensor.getMaxValue();
        } else if (value[1] < cosAngleMDBSensor.getMinValue()) {
            value[1] = cosAngleMDBSensor.getMinValue();
        }

        sinAngleMDBSensor.writeDouble(value[0]);
        cosAngleMDBSensor.writeDouble(value[1]);
        distanceMDBSensor.writeDouble(value[2]);
    }

    public double[] scenarioSensorization(Scenario scenario) {

        //devuelve la distancia al objeto más cercano que puede sensar:
        //por color
        double[] result = new double[3];

        double maxDistance = (distanceMDBSensor != null ? 
                distanceMDBSensor.getMaxValue() : range);
        double distance = maxDistance;
        double objX = 0, objY = 0;
        boolean found = false;
        double robotX, robotY;
        robotX = this.getRobot().getX();
        robotY = this.getRobot().getY();
        //Recorro la lista de todos los objetos del escenario:
        for (Object obj : scenario.getScenarioObjects().values()) {

            ScenarioObject so = (ScenarioObject) obj;
            //Compruebo si tienen color y si puedo detectarlo:
            if (so instanceof ColoredObject
                    && ((ColoredObject) so).getColor().equalsIgnoreCase(color)) {

                if (((ColoredObject) so).getDetectable()) {

                    //Calculo la distancia entre este sensor y el objeto:
                    //Necesito saber la posición del "dueño" del sensor en el escenario
                    //Necesito la posición del objeto en el escenario
                    objX = so.getX();
                    objY = so.getY();

                    double robotOrientation = this.getRobot().getRobotOrientation();
                    //La orientación del sensor es relativa a la orientación del robot:
                    double sensorOrientation = robotOrientation + position;
                    double[] angleAndDisntace = getAngleAndDistance(robotX, robotY, objX, objY, sensorOrientation);
                    result[0] = Math.sin(angleAndDisntace[1]);
                    result[1] = Math.cos(angleAndDisntace[1]);

                    if (isInScope(angleAndDisntace[1])
                            && angleAndDisntace[0] < range) {
                        distance = Math.min(distance, angleAndDisntace[0]);
                        found = true;
                    }
                } else {
                    result[0] = Math.sin(0.0);
                    result[1] = Math.cos(0.0);
                    result[2] = 0;
                    return result;

                }
            }

        }
        if (found) {
            for (Object obj : scenario.getScenarioObjects().values()) {

                ScenarioObject so = (ScenarioObject) obj;

                if (so instanceof Wall) {
                    Wall w = (Wall) so;
                    if (w.cross((int) robotX, (int) robotY, (int) objX, (int) objY)) {
                        result[0] = Math.sin(Math.PI / 2);
                        result[1] = Math.cos(Math.PI / 2);
                        distance = maxDistance;
                        break;
                    }
                } else if (so instanceof Door) {
                    Door w = (Door) so;
                    if (!w.isOpen() && w.cross((int) robotX, (int) robotY, (int) objX, (int) objY)) {
                        result[0] = Math.sin(Math.PI / 2);
                        result[1] = Math.cos(Math.PI / 2);
                        distance = maxDistance;
                        break;
                    }
                }
            }
        }

        if (distance >= maxDistance) {
            result[0] = Math.sin(Math.PI / 2);
            result[1] = Math.cos(Math.PI / 2);
        }

        result[2] = distance >= maxDistance ? maxDistance : distance;
        return result;
    }

    private boolean isInScope(double angle) {
        boolean inScope;

        inScope = (angle + scope / 2 >= SCOPE_THRESHOLD
                && angle - scope / 2 <= SCOPE_THRESHOLD);

        return inScope;
    }

    private double[] getAngleAndDistance(double robotX, double robotY,
            double objX, double objY, double sensorOrientation) {

        //Dirección del sonar
        Vector2d directionVector = new Vector2d(
                Math.cos(sensorOrientation),
                Math.sin(sensorOrientation));

        Vector2d toObjectVector = getRobot().getScenario().getDistanceVector(robotX, robotY, objX, objY);

        double angle = directionVector.angle(toObjectVector);

        Vector2d p = new Vector2d(-toObjectVector.y, toObjectVector.x);

        if (p.angle(directionVector) < Math.PI / 2.0) {
            angle = -angle;
        }

        double[] angleAndDistance = new double[]{toObjectVector.length(), angle};

        return angleAndDistance;
    }

    @Override
    public void configure(Configuration configuration) {

        String idSinAngle = configuration.getString("sinAngleId");
        sinAngleMDBSensor = new SocketWriter(idSinAngle);
        getMdbSensors().put(sinAngleMDBSensor.getId(), sinAngleMDBSensor);
        String idCosAngle = configuration.getString("cosAngleId");
        cosAngleMDBSensor = new SocketWriter(idCosAngle);
        getMdbSensors().put(cosAngleMDBSensor.getId(), cosAngleMDBSensor);

        String idDistance = configuration.getString("distanceId");
        distanceMDBSensor = new SocketWriter(idDistance);
        getMdbSensors().put(distanceMDBSensor.getId(), distanceMDBSensor);

        this.color = configuration.getString("color");
        this.position = Math.toRadians(configuration.getDouble("position"));
        this.range = configuration.getDouble("range");
        this.scope = Math.toRadians(configuration.getDouble("scope"));
    }

    public double[] getValue() {
        return value;
    }

    public double getRange() {
        return range;
    }

    public String getColor() {
        return color;
    }

    public double getScope() {
        return scope;
    }

    public double getPosition() {
        return position;
    }

    @Override
    public String headLog() {
        return getId() + "_sinAngle\t" + getId() + "_cosAngle\t" + getId() + "_distance";
    }

    @Override
    public String lineLog() {
        if (value == null) {
            return "-\t-\t-";
        }
        return value[0] + "\t" + value[1] + "\t" + value[2];
    }

}
