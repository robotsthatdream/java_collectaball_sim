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
package es.udc.gii.callectaball.object;

import es.udc.gii.collectaball.scenario.Scenario;

/**
 * Represents an scenario object
 * @author GII
 */
public abstract class ScenarioObject {
    
    private Scenario scenario;
    
    private String id;
    
    private double x;
    
    private double y;
    
    private boolean changePosition;
    
    public ScenarioObject(String id) {
        this.id = id;
        this.x = 0;
        this.y = 0;
        this.changePosition = Boolean.FALSE;
    }
    
    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }
    
    public Scenario getScenario() {
        return this.scenario;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isChangePosition() {
        return changePosition;
    }

    public void setChangePosition(boolean changePosition) {
        this.changePosition = changePosition;
    }
    
    public String headLog() {
        return getId() + "_x\t" + getId() + "_y";
    }
    
    public String lineLog() {
        return x + "\t" + y;
    }
    
    
    
    
    
}
