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

/**
 *
 * @author GII
 */
public abstract class MotorActuator extends MDBRobotActuator {

    private SocketReader angularMDBSocket;
    private SocketReader linearMDBSocket;

    private double angularSpeed;
    private double linearSpeed;

    @Override
    public void act(Scenario scenario) {
        angularSpeed = angularMDBSocket.readDouble();
        linearSpeed = linearMDBSocket.readDouble();

        scenarioActuation(angularSpeed, linearSpeed);
    }

    public abstract void scenarioActuation(double angularSpeed, double linearSpeed);

    @Override
    public void configure(Configuration configuration) {
        Configuration angularSocketSubset = configuration.subset("angularMDBSocket");
        String idAngular = angularSocketSubset.getString("id");
        angularMDBSocket = new SocketReader(idAngular);
        getMdbActuators().put(angularMDBSocket.getId(), angularMDBSocket);

        Configuration linearSocketSubset = configuration.subset("linearMDBSocket");
        String idLinear = linearSocketSubset.getString("id");
        linearMDBSocket = new SocketReader(idLinear);
        getMdbActuators().put(linearMDBSocket.getId(), linearMDBSocket);
    }

    @Override
    public String headLog() {
        return getId() + "_angularSpeed\t" + getId() + "_linearSpeed";
    }

    @Override
    public String lineLog() {
        return angularSpeed + "\t" + linearSpeed;
    }
    
    
    

}
