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
import es.udc.gii.collectaball.robot.MDBBallCollectorRobot;
import es.udc.gii.callectaball.object.MovingObject;
import es.udc.gii.callectaball.object.RedBall;
import es.udc.gii.collectaball.util.RandomUtil;
import es.udc.gii.callectaball.object.LoadableObject;
import es.udc.gii.callectaball.object.ScenarioObject;
import es.udc.gii.collectaball.robot.sensor.CrashSensor;
import es.udc.gii.collectaball.robot.sensor.LoadSensor;
import es.udc.gii.collectaball.robot.sensor.RewardSensor;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author GII
 */
public class CollectBallsScenario extends BallScenario {

    private double lastXPosition;
    private double lastYPosition;

    public CollectBallsScenario() {
        super();

    }

    @Override
    public void cycleFinished(ScenarioObject scenarioObject) {

        MDBBallCollectorRobot robot = (MDBBallCollectorRobot) scenarioObject;
        LoadSensor ls = (LoadSensor) robot.getSensors().get(CollectBallsScenario.LOAD_SENSOR);
        LoadableObject ball = ls.getLoad();
        RewardSensor rewardSensor = (RewardSensor) robot.getSensors().get(BallScenario.REWARD_SENSOR);
        BallCollectorRobotToPaint ccrtp = robot.getBallCollectorRobotToPaint();
        boolean crash = false;
        if (xInBorder(scenarioObject, 25, 25)) {
            robot.setX(lastXPosition);
            crash = true;
        }
        if (yInBorder(scenarioObject, 25, 25)) {
            robot.setY(lastYPosition);
            crash = true;
        }

        CrashSensor crashSensor = (CrashSensor) robot.getSensors().get(BallScenario.CRASH_SENSOR);
//
        crashSensor.setCrash(crash ? 1.0 : 0.0);
        crashSensor.sense(this);

        if (rewardSensor.getReward() > 0.0) {
            ls.unload();
            ((RedBall) ball).setLoaded(false);
            MovingObject.moveObjectToRandomPosition(this, robot);
            robot.setRobotOrientation(RandomUtil.nextDouble() * Math.PI * 2 - Math.PI);

        }
        getScenarioToPaint().fireStateChanged();

    }

    @Override
    public void notifyChange(ScenarioObject scenarioObject) {

        if (scenarioObject instanceof MDBBallCollectorRobot) {

            MDBBallCollectorRobot robot = (MDBBallCollectorRobot) scenarioObject;
            LoadSensor ls = (LoadSensor) robot.getSensors().get(CollectBallsScenario.LOAD_SENSOR);
            BallCollectorRobotToPaint ccrtp = robot.getBallCollectorRobotToPaint();
            RewardSensor rewardSensor = (RewardSensor) robot.getSensors().get(BallScenario.REWARD_SENSOR);
            if (ls.getLoad() == null) {
                LoadableObject reached = checkForBallReached(ccrtp);
                if (reached != null) {
                    ls.load(reached);
                    ((RedBall) reached).setLoaded(Boolean.TRUE);

                }
                rewardSensor.setReward(0.0);
            } else {
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

    @Override
    public void cycleInit(ScenarioObject scenarioObject) {
        MDBBallCollectorRobot robot = (MDBBallCollectorRobot) scenarioObject;
        lastXPosition = robot.getX();
        lastYPosition = robot.getY();
    }

    @Override
    public void configure(Configuration configuration) {
        super.configure(configuration);
    }

}
