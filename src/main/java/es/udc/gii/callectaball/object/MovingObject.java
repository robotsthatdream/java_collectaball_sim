package es.udc.gii.callectaball.object;

import es.udc.gii.collectaball.util.RandomUtil;
import es.udc.gii.collectaball.util.config.Configurable;
import es.udc.gii.collectaball.scenario.Scenario;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 * Class to ease the position change of the scenario objects
 * @author GII
 */
public class MovingObject implements Configurable {

    private static double distance;

    public enum RelativePosition {

        LEFT, RIGHT, UP, DOWN
    }

    public enum Operation {

        LT, ET, GT, LET, GET
    };

    public static void moveObjectToDistanceAndRelativePosition(Scenario<ScenarioObject> scenario, List<RelativePositionTo> relativePositions, List<DistanceTo> distances, ScenarioObject so) {
        double xMax = scenario.getWidth() / 2 - 50;
        double xMin = -scenario.getWidth() / 2 + 50;
        double yMax = scenario.getHeight() / 2 - 50;
        double yMin = -scenario.getHeight() / 2 + 50;

        for (RelativePositionTo relativePositionTo : relativePositions) {
            ScenarioObject target = relativePositionTo.getTarget();
            switch (relativePositionTo.getRelativePosition()) {
                case LEFT:
                    double leftX = target.getX() - relativePositionTo.getMargin();
                    xMax = leftX < xMax ? leftX : xMax;
                    break;
                case RIGHT:
                    double rightX = target.getX() + relativePositionTo.getMargin();
                    xMin = rightX > xMin ? rightX : xMin;
                    break;
                case UP:
                    double upY = target.getY() - relativePositionTo.getMargin();
                    yMax = upY < yMax ? upY : yMax;
                    break;
                case DOWN:
                    double downY = target.getY() + relativePositionTo.getMargin();
                    yMin = downY > yMin ? downY : yMin;
                    break;
            }
        }
        double x = RandomUtil.nextDouble() * (xMax - xMin) + xMin;
        double y = RandomUtil.nextDouble() * (yMax - yMin) + yMin;
        boolean ok = false || distances.isEmpty();
        do {
            for (DistanceTo distanceTo : distances) {
                x = RandomUtil.nextDouble() * (xMax - xMin) + xMin;
                y = RandomUtil.nextDouble() * (yMax - yMin) + yMin;
                double d = Math.sqrt(Math.pow(distanceTo.getTarget().getX() - x, 2) + Math.pow(distanceTo.getTarget().getY() - y, 2));
                if (!getCondition(d, distanceTo.getDistance(), distanceTo.getOperation())) {
                    ok = false;
                    break;
                }
                ok = true;
            }

        } while (!ok);

        so.setX(x);
        so.setY(y);

    }

    public static void moveObjectToRandomPosition(Scenario<ScenarioObject> scenario, ScenarioObject so) {

        double x = (RandomUtil.nextDouble() * 2.0 - 1.0) * (scenario.getWidth() / 2 - 50);
        double y = (RandomUtil.nextDouble() * 2.0 - 1.0) * (scenario.getHeight() / 2 - 50);

        so.setX(x);
        so.setY(y);
    }

    public static void moveObject(Scenario<ScenarioObject> scenario, ScenarioObject so, ScenarioObject so2, double distance, Operation operation) {

        double x = (RandomUtil.nextDouble() * 2.0 - 1.0) * (scenario.getWidth() / 2 - 50);
        double y = (RandomUtil.nextDouble() * 2.0 - 1.0) * (scenario.getHeight() / 2 - 50);

        double d = Math.sqrt(x * x + y * y);

        while (getCondition(d, distance, operation)) {
            x = (RandomUtil.nextDouble() * 2.0 - 1.0) * (scenario.getWidth() / 2 - 50);
            y = (RandomUtil.nextDouble() * 2.0 - 1.0) * (scenario.getHeight() / 2 - 50);
            d = Math.sqrt(x * x + y * y);
        }

        so.setX(x);
        so.setY(y);
    }

    public static void moveObject(Scenario<ScenarioObject> scenario, ScenarioObject so, ScenarioObject so2, Operation operation) {
        moveObject(scenario, so, so2, distance, operation);
    }

    @Override
    public void configure(Configuration configuration) {
        this.distance = configuration.getDouble("distance");
    }

    private static boolean getCondition(double a1, double a2, Operation op) {
        switch (op) {
            case LT:
                return a1 < a2;
            case GT:
                return a1 > a2;
            case ET:
                return a1 == a2;
            case LET:
                return a1 <= a2;
            case GET:
                return a1 >= a2;
            default:
                return false;
        }
    }

    public static void moveObjetctToDistance(Scenario<ScenarioObject> scenario, ScenarioObject object, double distance, double x, double y) {
        double angle, newX, newY;
        do {
            angle = RandomUtil.nextDouble() * 2 * Math.PI;
            newX = x + distance * Math.cos(angle);
            newY = y + distance * Math.sin(angle);

        } while (!(newX > -scenario.getWidth() / 2 && newX < scenario.getWidth() / 2 && newY > -scenario.getHeight() / 2 && newY < scenario.getHeight() / 2));

        object.setX(newX);
        object.setY(newY);

    }

    public static class RelativePositionTo {

        ScenarioObject target;
        RelativePosition relativePosition;
        double margin;

        public RelativePositionTo(ScenarioObject target, RelativePosition relativePosition, double margin) {
            this.target = target;
            this.relativePosition = relativePosition;
            this.margin = margin;
        }

        public RelativePosition getRelativePosition() {
            return relativePosition;
        }

        public ScenarioObject getTarget() {
            return target;
        }

        public double getMargin() {
            return margin;
        }

    }

    public static class DistanceTo {

        ScenarioObject target;
        Operation operation;
        double distance;

        public DistanceTo(ScenarioObject target, Operation operation, double distance) {
            this.target = target;
            this.operation = operation;
            this.distance = distance;
        }

        public Operation getOperation() {
            return operation;
        }

        public ScenarioObject getTarget() {
            return target;
        }

        public double getDistance() {
            return distance;
        }

    }
}
