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
package es.udc.gii.collectaball.robot.actuator;

import es.udc.gii.collectaball.scenario.Scenario;
import es.udc.gii.collectaball.util.socket.SocketReader;
import org.apache.commons.configuration.Configuration;
import org.slf4j.LoggerFactory;

/**
 *
 * @author GII
 */
public class AngleMotorActuator extends MDBRobotActuator {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AngleMotorActuator.class);

    private SocketReader angularMDBSocket;

    private double absoluteRobotAngle = 3.0/4.0 * Math.PI;
    private double linearSpeed;

    @Override
    public void act(Scenario scenario) {
        double angSpeed = angularMDBSocket.readDouble();
        angSpeed = Math.toRadians(angSpeed);

        scenarioActuation(angSpeed, linearSpeed, scenario);
        getRobot().getScenario().notifyChange(getRobot());

    }

    public void scenarioActuation(double angSpeed, double linearSpeed, Scenario scenario) {
        double x = this.getRobot().getX();
        double y = this.getRobot().getY();

        double normalizedAS = (absoluteRobotAngle + angSpeed) % (2.0 * Math.PI);
        if (normalizedAS < 0.0) {
            normalizedAS = 2.0 * Math.PI + normalizedAS;
        }  

        x += linearSpeed * Math.cos(normalizedAS);
        y += linearSpeed * Math.sin(normalizedAS);


        this.getRobot().setX(x);
        this.getRobot().setY(y);

        this.setAngularSpeed(normalizedAS);
    }

    @Override
    public void configure(Configuration configuration) {
        Configuration angularSocketSubset = configuration.subset("angularMDBSocket");
        String idAngular = angularSocketSubset.getString("id");
        angularMDBSocket = new SocketReader(idAngular);
        getMdbActuators().put(angularMDBSocket.getId(), angularMDBSocket);

        linearSpeed = configuration.getDouble("linearSpeed");
    }

    public double getAngularSpeed() {
        return this.absoluteRobotAngle;
    }

    public void setAngularSpeed(double angularSpeed) {
        this.absoluteRobotAngle = angularSpeed % (2.0 * Math.PI);
    }

    public double getLinearSpeed() {
        return linearSpeed;
    }

    @Override
    public String headLog() {
        return getId() + "_linearSpeed\t" + getId() + "_absoluteRobotAngle";
    }
    
    
    @Override
    public String lineLog() {
        return linearSpeed + "\t" + absoluteRobotAngle;
    }
    
    
    

}
