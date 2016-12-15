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

import es.udc.gii.collectaball.util.socket.SocketReader;
import java.util.HashMap;
import java.util.Map;


/**
 * Represents an actuator for a robot to be used with MDB, that is, that read its value by sockets
 * @author GII
 */
public abstract class MDBRobotActuator extends RobotActuator{

    private Map<String,SocketReader> mdbActuators;
    
    public MDBRobotActuator() {
        mdbActuators = new HashMap<>();
    }

    public Map<String, SocketReader> getMdbActuators() {
        return mdbActuators;
    }

    public void setMdbActuators(Map<String, SocketReader> mdbActuators) {
        this.mdbActuators = mdbActuators;
    }


}
