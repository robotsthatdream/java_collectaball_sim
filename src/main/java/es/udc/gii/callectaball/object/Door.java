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

import java.awt.geom.Line2D;

/**
 *
 * @author GII
 */
public class Door extends StateObject implements ColoredObject {

    private int width;

    private int height;

    private String orientation;

    public final static double OPENED = 1.0;
    public final static double CLOSED = 0.0;
//    public final static String COLOR = "#FFFF00";
    public final static String COLOR = "#000000";
    private final static int THREESHOLD = 1;

    
    public Door(String id, double state, int width, int height, String orientation) {
        super(id, state);
        this.width = width;
        this.height = height;
        this.orientation = orientation;
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

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + this.width;
        hash = 61 * hash + this.height;
        hash = 61 * hash + orientation.hashCode();
        hash = 61 * hash + ((int) getState());
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
        final Door other = (Door) obj;

        if (this.width != other.width) {
            return false;
        }
        if (this.height != other.height) {
            return false;
        }

        if (!this.orientation.equalsIgnoreCase(other.orientation)) {
            return false;
        }
        return (getState() == other.getState());
    }

    @Override
    public String getColor() {
        return COLOR;
    }

    @Override
    public String headLog() {
        return super.headLog() + "\t" + getId() + "_state";
    }

    @Override
    public String lineLog() {
        return super.lineLog() + "\t" + getState();
    }

    @Override
    public boolean getDetectable() {
        return Boolean.TRUE;
    }

    public boolean isOpen() {
        return getState() >= 1.0e-6;
    }

    public String getOpenColor() {
        return "#ffffff";
    }

   
    public boolean cross(int initX, int initY, int endX, int endY) {
        Line2D trajectory = new Line2D.Double(initX, initY, endX, endY);

        boolean horizontalIntersect, verticalIntersect;
        if (getY() < initY) { //Robot por encima del muro
            Line2D sideUp = new Line2D.Double(getX() + width / 2, getY() + height / 2 + THREESHOLD, getX() - width / 2, getY() + height / 2 + THREESHOLD);
            horizontalIntersect = trajectory.intersectsLine(sideUp);
        } else {
            Line2D sideDown = new Line2D.Double(getX() + width / 2, getY() - height / 2 - THREESHOLD, getX() - width / 2, getY() - height / 2 - THREESHOLD);
             horizontalIntersect = trajectory.intersectsLine(sideDown);
        }

        if (getX() < initX) { //Robot a la derecha del muro
            Line2D sideRight = new Line2D.Double(getX() + width / 2 + THREESHOLD, getY() - height / 2, getX() + width / 2 + THREESHOLD, getY() + height / 2);
            verticalIntersect = trajectory.intersectsLine(sideRight);
        } else {
            Line2D sideLeft = new Line2D.Double(getX() - width / 2 - THREESHOLD, getY() - height / 2, getX() - width / 2 - THREESHOLD, getY() + height / 2);
            verticalIntersect = trajectory.intersectsLine(sideLeft);
        }

        return verticalIntersect || horizontalIntersect;

    }

}
