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
package es.udc.gii.collectaball.model;

/**
 * Calculates the position an properties of the robot acording to its movement
 * @author GII
 */
public class ModelsCalculations {

    private static double distance;

    private static double angle;

    private static double xRelative;

    private static double yRelative;

    public static void calculateFromDistanceAndAngle(double distance0,
            double angle0, double motor, double avance) {

        if (distance0 == 0.0) {
            distance = distance0;
            angle = angle0;
            xRelative = 0.0;
            yRelative = 0.0;
        } else {

            double x0 = distance0 * Math.cos(angle0);
            double y0 = distance0 * Math.sin(angle0);

            double angleP = angle0 - motor;

            double x1 = distance0 * Math.cos(angleP);
            double y1 = distance0 * Math.sin(angleP);

            xRelative = x1 - avance;
            yRelative = y1;

            distance = Math.sqrt(xRelative * xRelative + yRelative * yRelative);
            angle = Math.atan2(yRelative, xRelative);
        }
    }

    public static void calculateFromXYRelative(double x, double y, double motor, double avance) {

        double distance0 = Math.sqrt(x * x + y * y);
        double angle0 = Math.atan2(y, x);

        calculateFromDistanceAndAngle(distance0, angle0, motor, avance);

    }

    public static void calculateFromDistanceSinCos(double distance0,
            double sin, double cos, double motor, double avance) {

        double angle0 = Math.atan2(sin, cos);

        calculateFromDistanceAndAngle(distance0, angle0, motor, avance);

    }

    public static double getDistance() {
        return distance;
    }

    public static double getAngle() {
        return angle;
    }

    public static double getxRelative() {
        return xRelative;
    }

    public static double getyRelative() {
        return yRelative;
    }

    public static double getCosAngle() {
        return Math.cos(angle);
    }

    public static double getSinAngle() {
        return Math.sin(angle);
    }

}
