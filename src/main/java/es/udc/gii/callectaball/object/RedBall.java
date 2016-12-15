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

import java.util.Objects;

/**
 * Represents a ball that can be loaded by the robot
 * @author GII
 */
public class RedBall extends LoadableObject implements ColoredObject {
        
    private final String color;
    
    private int widht;
    
    private int height;
    
    private boolean loaded;
    
    public RedBall(String id) {
        super(id);
        this.color = "#FF0000";
        this.widht = 25;
        this.height = 25;
        this.loaded = Boolean.FALSE;
    }
    
    public RedBall(String id, String color, int width, int height, boolean loaded) {
        super(id);
        this.color = color;
        this.widht = width;
        this.height = height;
        this.loaded = loaded;
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

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + Objects.hashCode(this.color);
        hash = 61 * hash + this.widht;
        hash = 61 * hash + this.height;
        hash = 61 * hash + (this.loaded ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        
        boolean equal = Boolean.TRUE;
        if (obj == null) {
            return Boolean.FALSE;
        }
        if (getClass() != obj.getClass()) {
            equal = Boolean.FALSE;
        }
        final RedBall other = (RedBall) obj;
        if (!Objects.equals(this.color, other.color)) {
            equal = Boolean.FALSE;
        }
        if (this.widht != other.widht) {
            equal = Boolean.FALSE;
        }
        if (this.height != other.height) {
            equal = Boolean.FALSE;
        }
        return (this.loaded == other.loaded) && equal;
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public boolean getDetectable() {
        return !this.loaded;
    }

    @Override
    public String headLog() {
        return super.headLog() + "\t" + getId() + "_loaded";
    }
    
    
    @Override
    public String lineLog() {
        return super.lineLog() + "\t" + (loaded ? "1" : "0");
    }

    
  
    
}
