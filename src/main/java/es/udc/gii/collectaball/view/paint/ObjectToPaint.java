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
package es.udc.gii.collectaball.view.paint;

import es.udc.gii.callectaball.object.ScenarioObject;
import java.awt.Graphics2D;

/**
 * Represents the visual properties of an object. Implementation following the decorator pattern
 * @author GII
 * @param <O>
 */
public abstract class ObjectToPaint<O extends ScenarioObject> {
    
    private O scenarioObject;
    
    private ScenarioToPaint scenario;
        
    public ObjectToPaint(O scenarioObject) {
        this.scenarioObject = scenarioObject;
    }
    
    /**
     * 
     * @param g
     * @param x posición central del objeto según el escenario donde se va a pintar
     * @param y posición central del objeto según el escenario donde se va a pintar
     */
    public abstract void paint(Graphics2D g, int x, int y);

    public ScenarioToPaint getScenario() {
        return scenario;
    }
    
    public O getScenarioObject() {
        return scenarioObject;
    }
    
    public void setScenario(ScenarioToPaint scenario) {
        this.scenario = scenario;
    }

    public double getX() {
        return this.scenarioObject.getX();
    }

    public void setX(double x) {
        this.scenarioObject.setX(x);
    }

    public double getY() {
        return this.scenarioObject.getY();
    }

    public void setY(double y) {
        this.scenarioObject.setY(y);
    }
    
    public void setChangePosition(boolean changePosition) {
        this.scenarioObject.setChangePosition(changePosition);
    }
    
    public boolean isChangePosition() {
        return this.scenarioObject.isChangePosition();
    }
    
    
}
