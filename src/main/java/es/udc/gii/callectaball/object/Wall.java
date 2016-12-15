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
public class Wall extends ScenarioObject implements ColoredObject {

    private int width;

    private int height;

    private String orientation;
    private final static int THREESHOLD = 1;

    public Wall(String id, int width, int height, String orientation) {
        super(id);
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
        hash = 71 * hash + this.width;
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
        final Wall other = (Wall) obj;
        if (this.width != other.width) {
            return false;
        }
        if (this.height != other.height) {
            return false;
        }
        if (!this.orientation.equalsIgnoreCase(orientation)) {
            return false;
        }
        return true;
    }

    @Override
    public String getColor() {
        return "#BEBEBE";
    }

    @Override
    public boolean getDetectable() {
        return Boolean.TRUE;
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
