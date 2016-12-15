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

import es.udc.gii.callectaball.object.BlueBox;
import es.udc.gii.collectaball.robot.BallCollectorRobotToPaint;
import es.udc.gii.callectaball.object.GreenButton;
import es.udc.gii.callectaball.object.GreenButtonToPaint;
import es.udc.gii.callectaball.object.MovingObject;
import es.udc.gii.callectaball.object.RedBall;
import es.udc.gii.callectaball.object.WallToPaint;
import es.udc.gii.callectaball.object.DoorToPaint;
import es.udc.gii.collectaball.util.RandomUtil;
import es.udc.gii.collectaball.robot.actuator.AngleMotorActuator;
import es.udc.gii.collectaball.robot.actuator.MDBRobotActuator;
import es.udc.gii.callectaball.object.LoadableObject;
import es.udc.gii.callectaball.object.ScenarioObject;
import es.udc.gii.collectaball.robot.MDBBallCollectorRobot;
import es.udc.gii.collectaball.robot.sensor.CrashSensor;
import es.udc.gii.collectaball.robot.sensor.LoadSensor;
import es.udc.gii.collectaball.robot.sensor.RewardSensor;
import es.udc.gii.collectaball.view.paint.ObjectToPaint;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author GII
 */
public class CollectBallWithDoorScenario extends BallScenario {

    private double lastRobotXPosition;
    private double lastRobotYPosition;

    public CollectBallWithDoorScenario() {
        super();
    }

    @Override
    public void cycleFinished(ScenarioObject scenarioObject) {

        MDBBallCollectorRobot robot = (MDBBallCollectorRobot) scenarioObject;
        LoadSensor ls = (LoadSensor) robot.getSensors().get(CollectBallWithDoorScenario.LOAD_SENSOR);
        LoadableObject ball = ls.getLoad();
        RewardSensor rewardSensor = (RewardSensor) robot.getSensors().get(BallScenario.REWARD_SENSOR);

        //check if the robot has gone beyond the border of the scenario
        boolean mustBeRepositioned = isInBorder(scenarioObject, 25, 25);

        List<ObjectToPaint> walls = robot.getBallCollectorRobotToPaint().getScenario().getObjectsById(CollectBallsScenario.WALL_ID);

        //check if the robot has crash into a wall
        for (ObjectToPaint otp : walls) {
            WallToPaint wtp = (WallToPaint) otp;
            mustBeRepositioned = mustBeRepositioned || wtp.getScenarioObject().cross((int) lastRobotXPosition, (int) lastRobotYPosition, (int) robot.getX(), (int) robot.getY());
        }

        //check if the robot has crash into a close door
        List<ObjectToPaint> doors = robot.getBallCollectorRobotToPaint().getScenario().getObjectsById(CollectBallsScenario.DOOR_ID);
        for (ObjectToPaint otp : doors) {
            DoorToPaint ydtp = (DoorToPaint) otp;
            mustBeRepositioned = mustBeRepositioned || (!ydtp.getScenarioObject().isOpen() && ydtp.getScenarioObject().cross((int) lastRobotXPosition, (int) lastRobotYPosition, (int) robot.getX(), (int) robot.getY()));
        }

        double crashed = 0.0; //Sensor value to indicate MDB if the robot has crashed or not
        if (mustBeRepositioned) { //The robot x/y position has to be reset
            robot.setX(lastRobotXPosition);
            robot.setY(lastRobotYPosition);
            crashed = 1.0;
        }
        CrashSensor crashSensor = (CrashSensor) robot.getSensors().get(BallScenario.CRASH_SENSOR);

        crashSensor.setCrash(crashed);
        crashSensor.sense(this); //Sensor sent

        //If a reward is obtained, the scenario must be restored to an initial position. Some object has to be modified
        if (rewardSensor != null && rewardSensor.getReward() > 0.0) {
            List<MovingObject.RelativePositionTo> robotRelativePositions = new ArrayList<>();
            List<MovingObject.RelativePositionTo> ballRelativePositions = new ArrayList<>();
            List<MovingObject.RelativePositionTo> buttonRelativePositions = new ArrayList<>();
            List<MovingObject.RelativePositionTo> boxRelativePositions = new ArrayList<>();
            List<MovingObject.DistanceTo> robotDistances = new ArrayList<>();
            List<MovingObject.DistanceTo> ballDistances = new ArrayList<>();
            List<MovingObject.DistanceTo> buttonDistances = new ArrayList<>();
            List<MovingObject.DistanceTo> boxDistances = new ArrayList<>();

            //First, the ball must be unload from the robot (it is being delivered to the box)
            ls.unload();
            ((RedBall) ball).setLoaded(false);

            //Controlling the position of the walls to restrict the reposition of the objects according to walls 
            for (ObjectToPaint otp : walls) {

                WallToPaint wtp = (WallToPaint) otp;
                MovingObject.RelativePosition robotRelPosToWall;
                MovingObject.RelativePosition ballRelPosToWall;
                MovingObject.RelativePosition buttonRelPosToWall;
                MovingObject.RelativePosition boxRelPosToWall;

                //Control to put the robot, the button and the box at one side of the wall and the ball at the other side
                if (wtp.getScenarioObject().getOrientation().equalsIgnoreCase("horizontal")) {
                    robotRelPosToWall = MovingObject.RelativePosition.DOWN;
                    buttonRelPosToWall = MovingObject.RelativePosition.UP;
                    boxRelPosToWall = MovingObject.RelativePosition.UP;
                    ballRelPosToWall = MovingObject.RelativePosition.UP;
                } else { //vertical
                    robotRelPosToWall = MovingObject.RelativePosition.LEFT;
                    buttonRelPosToWall = MovingObject.RelativePosition.LEFT;
                    boxRelPosToWall = MovingObject.RelativePosition.LEFT;
                    ballRelPosToWall = MovingObject.RelativePosition.RIGHT;
                }

                MovingObject.RelativePositionTo robotRPTWall = new MovingObject.RelativePositionTo(wtp.getScenarioObject(), robotRelPosToWall, 50);
                robotRelativePositions.add(robotRPTWall);

                MovingObject.RelativePositionTo ballRPTWall = new MovingObject.RelativePositionTo(wtp.getScenarioObject(), ballRelPosToWall, 50);
                ballRelativePositions.add(ballRPTWall);

                MovingObject.RelativePositionTo buttonRPTWall = new MovingObject.RelativePositionTo(wtp.getScenarioObject(), buttonRelPosToWall, 50);
                buttonRelativePositions.add(buttonRPTWall);

                MovingObject.RelativePositionTo boxRPTWall = new MovingObject.RelativePositionTo(wtp.getScenarioObject(), boxRelPosToWall, 50);
                boxRelativePositions.add(boxRPTWall);
            }

            //Button
            List<ObjectToPaint> buttons = robot.getBallCollectorRobotToPaint().getScenario().getObjectsById(CollectBallsScenario.BUTTON_ID);

            if (buttons != null && !buttons.isEmpty()) {
                //
                for (ObjectToPaint otp : buttons) {
                    //Unpush button
                    GreenButtonToPaint gbtp = (GreenButtonToPaint) otp;
                    gbtp.getScenarioObject().unPush();

                    if (otp.isChangePosition()) {
                        //If the button position must be changed
                        MovingObject.moveObjectToDistanceAndRelativePosition(this, buttonRelativePositions, buttonDistances, otp.getScenarioObject());
                    }
                    //Relative position of the robot to the button
                    MovingObject.DistanceTo robotToButton = new MovingObject.DistanceTo(gbtp.getScenarioObject(), MovingObject.Operation.GT, 50);
                    robotDistances.add(robotToButton);

                    //Relative position of the box to the button
                    MovingObject.DistanceTo boxToButton = new MovingObject.DistanceTo(gbtp.getScenarioObject(), MovingObject.Operation.GT, 100);
                    boxDistances.add(boxToButton);
                }
            }

            //Box
            List<ObjectToPaint> boxes = robot.getBallCollectorRobotToPaint().getScenario().getObjectsById(CollectBallsScenario.BOX_ID);
            for (ObjectToPaint box : boxes) {
                if (box.isChangePosition()) {
                    MovingObject.moveObjectToDistanceAndRelativePosition(this, boxRelativePositions, boxDistances, box.getScenarioObject());
                }
                //Relative position of the robot to the box
                MovingObject.DistanceTo robotToBox = new MovingObject.DistanceTo(box.getScenarioObject(), MovingObject.Operation.GT, 50);
                robotDistances.add(robotToBox);
            }

            //Robot movement
            MovingObject.moveObjectToDistanceAndRelativePosition(this, robotRelativePositions, robotDistances, robot);
            for (MDBRobotActuator mdbra : robot.getActuators().values()) {
                if (mdbra instanceof AngleMotorActuator) {
                    AngleMotorActuator ama = (AngleMotorActuator) mdbra;
                    ama.setAngularSpeed(RandomUtil.nextDouble() * Math.PI * 2);
                }
            }
            if (ball.isChangePosition()) {
                MovingObject.moveObjectToDistanceAndRelativePosition(this, ballRelativePositions, ballDistances, ball);
            }
        }

        getScenarioToPaint().fireStateChanged();
    }
   

    @Override
    public void notifyChange(ScenarioObject scenarioObject) {

        if (scenarioObject instanceof MDBBallCollectorRobot) {

            MDBBallCollectorRobot robot = (MDBBallCollectorRobot) scenarioObject;
            LoadSensor ls = (LoadSensor) robot.getSensors().get(BallScenario.LOAD_SENSOR);
            BallCollectorRobotToPaint ccrtp = robot.getBallCollectorRobotToPaint();
            RewardSensor rewardSensor = (RewardSensor) robot.getSensors().get(BallScenario.REWARD_SENSOR);

            //Check if the robot has reached the unpushed button
            GreenButton greenButton = checkForButtonReached(ccrtp);
            if (greenButton != null && !greenButton.isPushed()) {
                greenButton.push();
            }

            //Check if the robot has reach the ball and has to load it
            if (ls.getLoad() == null) {
                LoadableObject reached = checkForBallReached(ccrtp);
                if (reached != null) {
                    ls.load(reached);
                    ((RedBall) reached).setLoaded(Boolean.TRUE);
                }
                rewardSensor.setReward(0.0);
            } else { //If the robot had already the ball, check if it has reached the box
                double reward = 0.0;
                BlueBox blueBox = (BlueBox) checkForBoxReached(ccrtp);
                if (blueBox != null) {
                    reward = blueBox.getReward();
                }
                rewardSensor.setReward(reward);
            }
        }

        getScenarioToPaint().fireStateChanged();
    }

    protected GreenButton checkForButtonReached(BallCollectorRobotToPaint robot) {

        //Get robot position:
        double robotX, robotY;
        robotX = robot.getX();
        robotY = robot.getY();

        List<ObjectToPaint> buttons = robot.getScenario().getObjectsById(
                CollectBallsScenario.BUTTON_ID);

        double buttonX, buttonY;
        double distance;
        if (buttons != null && !buttons.isEmpty()) {

            for (ObjectToPaint otp : buttons) {
                buttonX = otp.getX();
                buttonY = otp.getY();

                distance = Math.sqrt(
                        Math.pow((double) buttonX - robotX, 2.0)
                        + Math.pow((double) buttonY - robotY, 2.0));

                if (distance < BUTTON_THRESHOLD) {
                    return (GreenButton) otp.getScenarioObject();
                }
            }

        }
        return null;
    }

    @Override
    public void cycleInit(ScenarioObject scenarioObject) {
        MDBBallCollectorRobot robot = (MDBBallCollectorRobot) scenarioObject;
        lastRobotXPosition = robot.getX();
        lastRobotYPosition = robot.getY();
    }

    @Override
    public void configure(Configuration configuration) {
        super.configure(configuration);
    }

    @Override
    public String toString() {
        return "CollectBallWithDoorScenario";
    }

}
