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

import es.udc.gii.callectaball.object.ScenarioObject;
import es.udc.gii.collectaball.util.config.Configurable;
import es.udc.gii.callectaball.object.ScenarioLogger;
import es.udc.gii.collectaball.view.paint.ScenarioToPaint;
import java.util.HashMap;
import java.util.Map;
import javax.vecmath.Vector2d;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author GII
 * @param <O>
 */
public abstract class Scenario<O extends ScenarioObject> implements Configurable {

    /**
     * Lista de objetos que pertenecen al escenario, actuan sobre él o pueden
     * verse afectados por las acciones que otros hagan sobre el escenario.
     */
    private Map<String, O> scenarioObjects;

    private int width = 800;

    private int height = 600;

    private boolean toroidal = Boolean.FALSE;

    private ScenarioToPaint scenarioToPaint;

    private ScenarioLogger scenarioLogger;

    public Scenario() {
    }

    /**
     * Método encargado de inicializar los componentes del escenario. Habrá
     * escenarios que no tengan nada que inicializar.
     */
    public abstract void initComponents();

    public void addScenarioObject(String id, O scenarioObject) {
        if (this.scenarioObjects == null) {
            this.scenarioObjects = new HashMap<>();
        }
        this.scenarioObjects.put(id, scenarioObject);
        scenarioObject.setScenario(this);
    }

    public O removeScenarioObject(String id) {
        return this.scenarioObjects.remove(id);
    }

    public void setScenarioObjects(HashMap<String, O> scenarioObjects) {
        this.scenarioObjects = scenarioObjects;
    }

    public Map<String, O> getScenarioObjects() {
        return this.scenarioObjects;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public ScenarioToPaint getScenarioToPaint() {
        return scenarioToPaint;
    }

    public void setScenarioToPaint(ScenarioToPaint scenarioToPaint) {
        this.scenarioToPaint = scenarioToPaint;
    }

    public double[] checkScenarioPosition(double prevX, double prevY, double newX, double newY) {

        double[] correctedPosition = new double[2];
        double correctedX, correctedY, x, y;
        double divX, divY;

        //En este caso está dentro del mundo y no tenemos que corregir:
        if (newX <= this.width / 2 && newX >= -this.width / 2
                && newY <= this.height / 2 && newY >= -this.height / 2) {
            correctedPosition[0] = newX;
            correctedPosition[1] = newY;
        } else {
            //Si nos hemos salido del mundo, la nueva posición depende de si el
            //mundo es toroidal o no:
            correctedX = newX;
            correctedY = newY;
            if (!toroidal) {
                divX = newX - prevX;
                divY = newY - prevY;

                x = 0;
                if (Math.abs(divX) >= 1.0e-6) {
                    x = (int) ((Math.signum(newX) * this.width / 2 - prevX) / divX);
                }
                y = 0;
                if (Math.abs(divY) >= 1.0e-6) {
                    y = (int) ((Math.signum(newY) * this.height / 2 - prevY) / divY);
                }

                if ((x >= 0 && x <= 1 && y >= 0 && y <= 1 && x < y) || y < 0 || y > 1) {
                    correctedX = (int) (Math.signum(newX) * this.width / 2);
                    correctedY = x * (newY - prevY) + prevY;
                } else {
                    correctedY = (int) (Math.signum(newY) * this.height / 2);
                    correctedX = y * (newX - prevX) + prevX;
                }

            } else {

                if (newX >= this.width / 2) {
                    correctedX = newX - this.width;
                } else if (newX <= -this.width / 2) {
                    correctedX = newX + this.width;
                }

                if (newY >= this.height / 2) {
                    correctedY = newY - this.height;
                } else if (newY <= -this.height / 2) {
                    correctedY = newY + this.height;
                }

            }

            correctedPosition[0] = correctedX;
            correctedPosition[1] = correctedY;
        }

        return correctedPosition;

    }

    public abstract void notifyChange(ScenarioObject scenarioObject);

    public void cycleFinished(ScenarioObject scenarioObject) {

    }

    public void cycleInit(ScenarioObject scenarioObject) {

    }

    public ScenarioLogger getScenarioLogger() {
        return scenarioLogger;
    }

    public void setScenarioLogger(ScenarioLogger scenarioLogger) {
        this.scenarioLogger = scenarioLogger;
    }

    public void setToroidal(boolean toroidal) {
        this.toroidal = toroidal;
    }

    public boolean isToroidal() {
        return toroidal;
    }

    @Override
    public void configure(Configuration configuration) {
        this.toroidal = configuration.getBoolean("toroidal", Boolean.FALSE);

        Configuration logSubset = configuration.subset("log");
        if (logSubset != null && !logSubset.isEmpty()) {
            String path = logSubset.getString("path");
            String name = logSubset.getString("name");
            int cycles = logSubset.getInt("cycles");

            setScenarioLogger(new ScenarioLogger(this, path, name, cycles));
        }

        this.height = configuration.getInt("height",600);
        this.width = configuration.getInt("width",800);
        
    }

    public Vector2d getDistanceVector(double x1, double y1, double x2, double y2) {

        Vector2d distanceVector = null;
        double distanceX, distanceY;

        distanceX = x2 - x1;
        distanceY = y2 - y1;

        if (this.toroidal) {

            if (distanceX > this.width / 2.0) {
                distanceX = -(this.width - distanceX);
            } else if (distanceX < -this.width / 2.0) {
                distanceX = this.width + distanceX;
            }

            if (distanceY > this.height / 2.0) {
                distanceY = -(this.height - distanceY);
            } else if (distanceY < -this.height / 2.0) {
                distanceY = this.height + distanceY;
            }
        }

        distanceVector = new Vector2d(distanceX, distanceY);
        return distanceVector;
    }

    public boolean xInBorder(ScenarioObject scenarioObject, double xThreeshold, double yThreeshold) {
        if (scenarioObject.getX() - xThreeshold <= (-this.width / 2)
                || scenarioObject.getX() + xThreeshold >= (this.width / 2)) {
            return true;
        } else {
            return false;
        }
        
    }
        public boolean yInBorder(ScenarioObject scenarioObject, double xThreeshold, double yThreeshold) {
        if (scenarioObject.getY() - yThreeshold <= (-this.height / 2)
                || scenarioObject.getY() + yThreeshold >= (this.height / 2)) {
            return true;
        } else {
            return false;
        }
    }
    public boolean isInBorder(ScenarioObject scenarioObject, double xThreeshold, double yThreeshold) {
        if (scenarioObject.getX() - xThreeshold <= (-this.width / 2)
                || scenarioObject.getX() + xThreeshold >= (this.width / 2)
                || scenarioObject.getY() - yThreeshold <= (-this.height / 2)
                || scenarioObject.getY() + yThreeshold >= (this.height / 2)) {
            return true;
        } else {
            return false;
        }
    }

}
