package es.udc.gii.collectaball.robot.sensor;

import es.udc.gii.callectaball.object.ColoredObject;
import es.udc.gii.collectaball.scenario.Scenario;
import es.udc.gii.callectaball.object.ScenarioObject;
import javax.vecmath.Vector2d;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author Mastropiero
 */
public class OpticalSensor extends DoubleSensor {


    private static final double SCOPE_THRESHOLD = 1.0e-6;

    private String color;
    /**
     * Posición relativa
     */
    private double position;
    private double range;
    private double scope;

    public OpticalSensor(String color, double position, double range, double scope) {
        this.color = color;
        this.position = position;
        this.range = range;
        this.scope = scope;
    }

    public OpticalSensor() {
    }

    public String getColor() {
        return color;
    }

    public double getPosition() {
        return position;
    }

    public double getRange() {
        return range;
    }

    public double getScope() {
        return scope;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public void setScope(double scope) {
        this.scope = scope;
    }

    @Override
    public double scenarioSensorization(Scenario scenario) {

        //devuelve la distancia al objeto más cercano que puede sensar:
        //por color
        double distance = 1000;
        double angle;
        double auxDistance;

        //Recorro la lista de todos los objetos del escenario:
        for (Object obj : scenario.getScenarioObjects().values()) {

            ScenarioObject so = (ScenarioObject) obj;
            //Compruebo si tienen color y si puedo detectarlo:
            if (so instanceof ColoredObject
                    && ((ColoredObject) so).getColor().equalsIgnoreCase(color)
                    && ((ColoredObject) so).getDetectable()) {
                //Calculo la distancia entre este sensor y el objeto:
                //Necesito saber la posición del "dueño" del sensor en el escenario
                double robotX, robotY;
                robotX = this.getRobot().getX();
                robotY = this.getRobot().getY();

                //Necesito la posición del objeto en el escenario
                double objX, objY;
                objX = so.getX();
                objY = so.getY();

                //Distancia entre los objetos:
                auxDistance = Math.sqrt(Math.pow((double) objX - robotX, 2.0)
                        + Math.pow((double) objY - robotY, 2.0));

                double robotOrientation = this.getRobot().getRobotOrientation();
                //La orientación del sensor es relativa a la orientación del robot:
                double sensorOrientation = robotOrientation + position;
                angle = getAngle(robotX, robotY, objX, objY, sensorOrientation);

                if (isInScope(angle)
                        && auxDistance < range) {
                    distance = Math.min(distance, auxDistance);
                }

            }

        }

        return distance;

    }

    private boolean isInScope(double angle) {
        boolean inScope;

        inScope = (angle + scope / 2 >= SCOPE_THRESHOLD
                && angle - scope / 2 <= SCOPE_THRESHOLD);

        return inScope;
    }

    private double getAngle(double robotX, double robotY,
            double objX, double objY, double sensorOrientation) {

        //Dirección del sonar
        Vector2d directionVector = new Vector2d(
                Math.cos(sensorOrientation),
                Math.sin(sensorOrientation));

        Vector2d toObjectVector = getDistanceVector(robotX, robotY, objX, objY);

        double angle = directionVector.angle(toObjectVector);

        Vector2d p = new Vector2d(-toObjectVector.y, toObjectVector.x);

        if (p.angle(directionVector) < Math.PI / 2.0) {
            angle = -angle;
        }

        return angle;
    }

    private Vector2d getDistanceVector(double x1, double y1, double x2, double y2) {

        double distanceX;
        double distanceY;

        distanceX = x2 - x1;
        distanceY = y2 - y1;

        return new Vector2d(distanceX, distanceY);

    }

    @Override
    public void configure(Configuration configuration) {
        super.configure(configuration);

        this.color = configuration.getString("color");
        this.position = Math.toRadians(configuration.getDouble("position"));
        this.range = configuration.getDouble("range");
        this.scope = Math.toRadians(configuration.getDouble("scope"));

    }

}
