/*
 * Copyright (C) 2010 Grupo Integrado de Ingeniería
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.udc.gii.collectaball.scenario;

import es.udc.gii.callectaball.object.BlueBoxToPaint;
import es.udc.gii.collectaball.robot.BallCollectorRobotToPaint;
import es.udc.gii.collectaball.util.RandomUtil;
import es.udc.gii.callectaball.object.LoadableObject;
import es.udc.gii.callectaball.object.ScenarioObject;
import es.udc.gii.collectaball.view.paint.ObjectToPaint;
import java.util.List;

/**
 *
 * @author GII
 */
public abstract class BallScenario extends Scenario {

    protected static final double BUTTON_THRESHOLD = 25.0;
    protected static final double BALL_THRESHOLD = 25.0;
    protected static final double BOX_THRESHOLD = 25.0;

    public static final String ROBOT_ID = "CollectBallRobot";
    public static final String LOAD_SENSOR = "LoadSensor";
    public static final String BALL_ID = "RedBall";
    public static final String BOX_ID = "BlueBox";
    public static final String OPTICAL_SENSOR = "OpticalSensor";
    public static final String STATE_OPTICAL_SENSOR = "StateOpticalSensor";
    public static final String MOTOR_ID = "Motor";
    public static final String REWARD_SENSOR = "RewardSensor";
    public static final String CRASH_SENSOR = "CrashSensor";
    public static final String DOOR_ID = "Door";
    public static final String BUTTON_ID = "Button";
    public static final String WALL_ID = "Wall";

    enum MoveBallCriteria {

        RANDOM, FARTOBALL
    };

    public BallScenario() {
        super();
    }

    @Override
    public void initComponents() {

    }

    @Override
    public void cycleFinished(ScenarioObject scenarioObject) {

    }

    protected LoadableObject getBall(BallCollectorRobotToPaint robot) {
        List<ObjectToPaint> balls;
        balls = robot.getScenario().getObjectsById(CollectBallsScenario.BALL_ID);

        if (balls != null && !balls.isEmpty()) {

            for (ObjectToPaint otp : balls) {

                return (LoadableObject) otp.getScenarioObject();

            }

        }
        return null;
    }

    protected BlueBoxToPaint getBox(BallCollectorRobotToPaint robot) {
        List<BlueBoxToPaint> boxes;
        boxes = robot.getScenario().getObjectsById(
                CollectBallsScenario.BOX_ID);

        if (boxes != null && !boxes.isEmpty()) {

            for (ObjectToPaint otp : boxes) {

                return (BlueBoxToPaint) otp;

            }

        }
        return null;
    }
    
    protected LoadableObject checkForBallReached(BallCollectorRobotToPaint robot) {

        //Get robot position:
        double robotX, robotY;
        robotX = robot.getX();
        robotY = robot.getY();

        List<ObjectToPaint> balls;
        balls = robot.getScenario().getObjectsById(CollectBallsScenario.BALL_ID);

        double ballX, ballY;
        double distance;
        if (balls != null && !balls.isEmpty()) {

            for (ObjectToPaint otp : balls) {
                ballX = otp.getX();
                ballY = otp.getY();

                distance = Math.sqrt(
                        Math.pow((double) ballX - robotX, 2.0)
                        + Math.pow((double) ballY - robotY, 2.0));

                if (distance < BALL_THRESHOLD) {
                    return (LoadableObject) otp.getScenarioObject();
                }
            }

        }
        return null;
    }

    protected ScenarioObject checkForBoxReached(BallCollectorRobotToPaint robot) {

        //Get robot position:
        double robotX, robotY;
        robotX = robot.getX();
        robotY = robot.getY();

        List<ObjectToPaint> boxes;
        boxes = robot.getScenario().getObjectsById(
                CollectBallsScenario.BOX_ID);

        double boxX, boxY;
        double distance;
        if (boxes != null && !boxes.isEmpty()) {

            for (ObjectToPaint otp : boxes) {
                boxX = otp.getX();
                boxY = otp.getY();

                distance = Math.sqrt(
                        Math.pow((double) boxX - robotX, 2.0)
                        + Math.pow((double) boxY - robotY, 2.0));

                if (distance < BOX_THRESHOLD) {
                    return otp.getScenarioObject();
                }
            }
        }
        return null;
    }

    protected void moveScenarioObjetctToDistance(ScenarioObject object, double distance, double x, double y) {
        double angle, newX, newY;
        do {
            angle = RandomUtil.nextDouble() * 2 * Math.PI;
            newX = x + distance * Math.cos(angle);
            newY = y + distance * Math.sin(angle);

        } while (!(newX > -getWidth() / 2 && newX < getWidth() / 2 && newY > -getHeight() / 2 && newY < getHeight() / 2));

        object.setX(newX);
        object.setY(newY);

    }

    protected void moveScenarioObjectToVisibleZone(BallCollectorRobotToPaint robot, ScenarioObject object) {
        double robotOrientation = robot.getScenarioObject().getRobotOrientation();

        double xBall = (RandomUtil.nextDouble() * 2.0 - 1.0) * (getWidth() / 2 - 25);
        double yBall = (RandomUtil.nextDouble() * 2.0 - 1.0) * (getHeight() / 2 - 25);

        if (robotOrientation > 0 && robotOrientation < Math.PI / 2) {
            yBall = -yBall;
        } else if (robotOrientation >= Math.PI / 2 && robotOrientation < Math.PI) {
            yBall = -yBall;
            xBall = -xBall;
        } else if (robotOrientation >= Math.PI && robotOrientation < 3 * Math.PI / 2) {
            xBall = -xBall;
        }

        if (xBall > getWidth() / 2) {
            xBall = getWidth() / 2;
        } else if (xBall < -getWidth() / 2) {
            xBall = -getWidth() / 2;
        }
        if (yBall > getHeight() / 2) {
            yBall = getHeight() / 2;
        } else if (yBall < -getHeight() / 2) {
            yBall = -getHeight() / 2;
        }

        object.setX(xBall);
        object.setY(yBall);

    }

    protected void moveScenarioObjectToVisibleZoneFarTo(ScenarioObject object, double x, double y, double distance) {

        double xObject = x, yObject = y;

        while (Math.abs(xObject - x) < distance) {
            xObject = (RandomUtil.nextDouble() * 2.0 - 1.0) * 300.0;
        }

        while (Math.abs(yObject - x) < distance) {
            yObject = (RandomUtil.nextDouble() * 2.0 - 1.0) * 300.0;
        }

        object.setX(xObject);
        object.setY(yObject);

    }

    @Override
    public void notifyChange(ScenarioObject scenarioObject) {
        getScenarioToPaint().fireStateChanged();
    }

}
