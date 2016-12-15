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
package es.udc.gii.collectaball.robot.sensor;

import es.udc.gii.collectaball.scenario.Scenario;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author GII
 */
public class BallCollectorSatisfactionSensor extends SatisfactionSensor {

    private double maxDistance;
    private double maxAngle;
    
    
    @Override
    public double scenarioSensorization(Scenario scenario) {
        double minDistance = Double.POSITIVE_INFINITY;
        double angle = 0;
        
        return satisfactionFunction(angle, minDistance);

    }

    private double satisfactionFunction(double angle, double distance) {
        double d, a;
        if (distance >= maxDistance) {
            return 0.0;
        } else {
            d = 4*normalizeDistanceValue(distance);
            a = 4*normalizeAngleValue(angle);
        }
        
        double satisfaction = Math.exp(-d) + Math.exp(-Math.abs(a));

        return satisfaction;
    }
    
    
     private double normalizeDistanceValue(double value) {
        
        double m = 1.0/maxDistance;
        
        return m*(value);
    }
     
      private double normalizeAngleValue(double value) {
        
        double m = 1.0/maxAngle;
        
        return m*(Math.abs(value));
    }

    @Override
    public void configure(Configuration configuration) {
        super.configure(configuration);
        
        this.maxDistance = configuration.getDouble("maxDistance",500);
        this.maxAngle = configuration.getDouble("maxAngle",360);
        
    }
      
      
    


}
