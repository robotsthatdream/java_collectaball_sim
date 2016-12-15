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
 * Button that can be pushed and when its state chages, triggers the state change of another object.
 * @author GII
 */
public class GreenButton extends StateObject implements ColoredObject {

    private int widht;

    private int height;

    public final static double PUSHED = 1.0;
    public final static double NOT_PUSHED = 0.0;
    public final static String COLOR = "#00CC00";

    //
    private StateObject targetObject = null;

    public GreenButton(String id, double state, int width, int height) {
        super(id, state);
        this.widht = width;
        this.height = height;
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

    public void setTargetObject(StateObject targetObject) {
        this.targetObject = targetObject;
    }

    public StateObject getTargetObject() {
        return targetObject;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + this.widht;
        hash = 61 * hash + this.height;
        hash = 61 * hash + ((int) getState());
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
        final GreenButton other = (GreenButton) obj;

        if (this.widht != other.widht) {
            equal = Boolean.FALSE;
        }
        if (this.height != other.height) {
            equal = Boolean.FALSE;
        }
        return (getState() == other.getState()) && equal;
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

    public boolean isPushed() {
        return getState() >= 1.0e-6;
    }

    public String getPushedColor() {
        return "#008B00";
    }

    public void push() {
        setState(PUSHED);
        if (targetObject != null) {
            targetObject.setState(PUSHED);
        }
    }

    public void unPush() {
        setState(NOT_PUSHED);
        if (targetObject != null) {
            targetObject.setState(NOT_PUSHED);
        }
    }

}
