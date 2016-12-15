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

import es.udc.gii.collectaball.robot.actuator.MDBRobotActuator;
import es.udc.gii.collectaball.robot.sensor.MDBRobotSensor;
import java.util.List;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author GII
 */
public abstract class MDBRobot extends Robot<MDBRobotActuator, MDBRobotSensor> {

    public MDBRobot(String id) {
        super(id);
    }

    public void configureMDB(Configuration configuration) {
        Configuration sensorsConf = configuration.subset("sensors");
        List<String> sensorClasses = sensorsConf.getList("sensor.id");
        for (int i = 0; i < sensorClasses.size(); i++) {
            Configuration subset = sensorsConf.subset("sensor" + "(" + i + ")");
            String id = subset.getString("id");
            for (MDBRobotSensor rs : getSensors().values()) {
                for (String s : rs.getMdbSensors().keySet()) {
                    if (s.equalsIgnoreCase(id)) {
                        int port = subset.getInt("port");
                        double minVal = subset.getDouble("minVal");
                        double maxVal = subset.getDouble("maxVal");
                        double minNormVal = subset.getDouble("normMinVal");
                        double maxNormVal = subset.getDouble("normMaxVal");
                        rs.getMdbSensors().get(id).init(port, minVal, maxVal, minNormVal, maxNormVal);
                        break;
                    }
                }
            }
        }

        Configuration actuatorConf = configuration.subset("actuators");
        List<String> actuatorClasses = actuatorConf.getList("actuator.id");

        for (int i = 0; i < actuatorClasses.size(); i++) {
            Configuration subset = actuatorConf.subset("actuator" + "(" + i + ")");
            String id = subset.getString("id");
            for (MDBRobotActuator ra : getActuators().values()) {
                for (String s : ra.getMdbActuators().keySet()) {
                    if (s.equalsIgnoreCase(id)) {
                        int port = subset.getInt("port");
                        double minVal = subset.getDouble("minVal");
                        double maxVal = subset.getDouble("maxVal");
                        double minNormVal = subset.getDouble("normMinVal");
                        double maxNormVal = subset.getDouble("normMaxVal");
                        ra.getMdbActuators().get(id).init(port, minVal, maxVal, minNormVal, maxNormVal);
                        break;
                    }
                }
            }
        }

    }

    protected void initActuators() {
        //The actuators initialization is done by reading the MDB config file in configureMDB
    }

    protected void initSensors() {
        //The sensors initialization is done by reading the MDB config file in configureMDB
    }
}
