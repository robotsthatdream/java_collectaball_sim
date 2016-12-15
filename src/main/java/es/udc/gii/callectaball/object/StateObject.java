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
 *
 * Represents an object that has an state. For versatility the type that represents the state is a double
 * @author GII
 */public abstract class StateObject extends ScenarioObject {

    private double state;
    
    public StateObject(String id, double initState) {
        super(id);
        this.state = initState;
    }

    public double getState() {
        return state;
    }

    public void setState(double state) {
        this.state = state;
    }
    
    
}
