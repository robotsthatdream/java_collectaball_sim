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

import es.udc.gii.collectaball.util.config.Configurable;
import es.udc.gii.collectaball.robot.actuator.AngleMotorActuator;
import es.udc.gii.collectaball.robot.actuator.RobotActuator;
import es.udc.gii.callectaball.object.ScenarioObject;
import es.udc.gii.collectaball.robot.sensor.RobotSensor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author GII
 * @param <A> actuator implementation
 * @param <S> sensor implementation
 */
public abstract class Robot<A extends RobotActuator, S extends RobotSensor> extends ScenarioObject implements Configurable, Runnable {

    private Map<String, A> actuators;
    private Map<String, S> sensors;
    private List<String> loop;

    public Robot(String id) {
        super(id);
        actuators = new HashMap<>();
        sensors = new HashMap<>();
        loop = new ArrayList<>();
    }

    @Override
    public void configure(Configuration configuration) {
        double x = configuration.getDouble("x",0);
        double y = configuration.getDouble("y",0);
        
        setX(x);
        setY(y);
        Configuration sensorsConf = configuration.subset("sensors");
        List<String> sensorClasses = sensorsConf.getList("sensor.id");

        for (int i = 0; i < sensorClasses.size(); i++) {
            Configuration subset = sensorsConf.subset("sensor" + "(" + i + ")");
            String id = subset.getString("id");
            String clazzStr = subset.getString("class");
            try {
                S robotSensor = (S) Class.forName(clazzStr).newInstance();
                robotSensor.setId(id);
                sensors.put(id, robotSensor);
                robotSensor.setRobot(this);
                robotSensor.configure(subset.subset("config"));
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
                Logger.getLogger(Robot.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        Configuration actuatorConf = configuration.subset("actuators");
        List<String> actuatorClasses = actuatorConf.getList("actuator.id");

        for (int i = 0; i < actuatorClasses.size(); i++) {
            Configuration subset = actuatorConf.subset("actuator" + "(" + i + ")");
            String id = subset.getString("id");
            String clazzStr = subset.getString("class");
            try {
                A robotActuator = (A) Class.forName(clazzStr).newInstance();
                robotActuator.setId(id);
                actuators.put(id, robotActuator);
                robotActuator.setRobot(this);
                robotActuator.configure(subset.subset("config"));
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
                Logger.getLogger(Robot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Configuration loopConf = configuration.subset("loop");
        List<String> loopIds = loopConf.getList("component" + "." + "id");
        for (int i = 0; i < loopIds.size(); i++) {
            Configuration subset = loopConf.subset("component" + "(" + i + ")");
            String id = subset.getString("id");
            getLoop().add(id);
        }

        
        
    }

    public Map<String, S> getSensors() {
        return sensors;
    }

    public Map<String, A> getActuators() {
        return actuators;
    }

    public List<String> getLoop() {
        return loop;
    }

    public double getRobotOrientation() {

        double robotOrientation = 0.0;

        RobotActuator ra;
        for (Object obj : this.getActuators().values()) {
            ra = (RobotActuator) obj;

            if (ra instanceof AngleMotorActuator) {
                robotOrientation = ((AngleMotorActuator) ra).getAngularSpeed();
                break;
            }
        }

        return robotOrientation;

    }

    public void setRobotOrientation(double orientation) {
        RobotActuator ra;
        for (Object obj : this.getActuators().values()) {
            ra = (RobotActuator) obj;

            if (ra instanceof AngleMotorActuator) {
                ((AngleMotorActuator) ra).setAngularSpeed(orientation);
                break;
            }
        }
        getScenario().getScenarioToPaint().fireStateChanged();
    }

    @Override
    public String headLog() {
        String headLog = super.headLog();
        Set<String> sensorKeys = sensors.keySet();
        SortedSet<String> sortedSensorKeys = new TreeSet<>(sensorKeys);
        for (String k : sortedSensorKeys) {
            headLog += "\t" + sensors.get(k).headLog();
        }

        Set<String> actuatorKeys = actuators.keySet();
        SortedSet<String> sortedActuatorKeys = new TreeSet<>(actuatorKeys);
        for (String k : sortedActuatorKeys) {
            headLog += "\t" + actuators.get(k).headLog();
        }

        return headLog;
    }

    @Override
    public String lineLog() {
        String lineLog = super.lineLog();
        Set<String> sensorKeys = sensors.keySet();
        SortedSet<String> sortedSensorKeys = new TreeSet<>(sensorKeys);
        for (String k : sortedSensorKeys) {
            lineLog += "\t" + sensors.get(k).lineLog();
        }

        Set<String> actuatorKeys = actuators.keySet();
        SortedSet<String> sortedActuatorKeys = new TreeSet<>(actuatorKeys);
        for (String k : sortedActuatorKeys) {
            lineLog += "\t" + actuators.get(k).lineLog();
        }

        return lineLog;
    }

    
    public abstract int getRobotIteration();
    
    
}
