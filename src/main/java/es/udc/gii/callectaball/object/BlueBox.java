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

/**
 * Represents the box where the robot will deliver the ball
 * @author GII
 */
public class BlueBox extends LoadableObject implements ColoredObject {
    
    private int widht;
    
    private int height;
    
    private double reward;
    
    public BlueBox(String id) {
        super(id);
        this.widht = 50;
        this.height = 50;
        this.reward = 1;
    }
    
    public BlueBox(String id, int width, int height) {
        super(id);
        this.widht = width;
        this.height = height;
        this.reward = 1;
    }

    public int getWidht() {
        return widht;
    }

    public void setWidht(int widht) {
        this.widht = widht;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + this.widht;
        hash = 71 * hash + this.height;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BlueBox other = (BlueBox) obj;
        if (this.widht != other.widht) {
            return false;
        }
        if (this.height != other.height) {
            return false;
        }
        if (this.reward != other.reward) {
            return false;
        }
        return true;
    }

    

    @Override
    public String getColor() {
        return "#0000FF";
    }

    @Override
    public boolean getDetectable() {
        return Boolean.TRUE;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }
    
}
