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
package es.udc.gii.collectaball.robot;

/**
 *
 * @author GII
 */
public class MDBBallCollectorRobot extends MDBRobot {

    private int robotIteration;
    private BallCollectorRobotToPaint ballCollectorRobotToPaint;

    public MDBBallCollectorRobot(String id) {
        super(id);
    }

    @Override
    public void run() {
        boolean logger = getScenario().getScenarioLogger() != null;
        robotIteration = 0;
        while (robotIteration < 20000) { //FIXME read number of iterations from file config
            getScenario().cycleInit(this);
            if (logger) {
                getScenario().getScenarioLogger().doLog();
            }
            for (String s : getLoop()) {
                if (getSensors().containsKey(s)) {
                    getSensors().get(s).sense(getScenario());
                } else if (getActuators().containsKey(s)) {
                    getActuators().get(s).act(getScenario());
                }
            }
            if (logger) {
                getScenario().getScenarioLogger().doLog();
            }
            getScenario().cycleFinished(this);
            if (logger) {
                getScenario().getScenarioLogger().doLogAndFinishIteration();
            }
            robotIteration++;
        }
    }


    @Override
    public int getRobotIteration() {
        return robotIteration;
    }

    public BallCollectorRobotToPaint getBallCollectorRobotToPaint() {
        return ballCollectorRobotToPaint;
    }

    public void setBallCollectorRobotToPaint(BallCollectorRobotToPaint ballCollectorRobotToPaint) {
        this.ballCollectorRobotToPaint = ballCollectorRobotToPaint;
    }

}
