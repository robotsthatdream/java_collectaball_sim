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
package es.udc.gii.collectaball.view;

import es.udc.gii.collectaball.view.paint.ScenarioToPaint;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author GII
 */
public class RobotSimulatorCanvas extends Canvas {

    private transient ScenarioToPaint scenarioToPaint;

    public RobotSimulatorCanvas() {
        //Nunca puede ganar el focus
        this.setFocusable(Boolean.FALSE);
        this.setBackground(Color.WHITE);
    }

    public RobotSimulatorCanvas(ScenarioToPaint scenarioToPaint) {
        this.scenarioToPaint = scenarioToPaint;
        this.setFocusable(Boolean.FALSE);
    }

    public void setScenarioToPaint(ScenarioToPaint scenarioToPaint) {
        this.scenarioToPaint = scenarioToPaint;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2;
        g2 = (Graphics2D) g;

        //Pint los ejes del escenario:
        g2.setColor(Color.GRAY);

        //Eje de ordenadas:
        int x1, y1, x2, y2;
        int width = scenarioToPaint.getScenario().getWidth();
        int height = scenarioToPaint.getScenario().getHeight();

        x1 = width / 2;
        y1 = 0;
        x2 = x1;
        y2 = height;
        g2.drawLine(x1, y1, x2, y2);

        //Eje de abcisas:
        x1 = 0;
        y1 = height / 2;
        x2 = width;
        y2 = y1;
        g2.drawLine(x1, y1, x2, y2);

        //TODO - se puede hacer en un único bucle:
        //Divisiones principales, cada 100 píxeles, de tamaño 10 pixeles:
        //En el eje de ordenadas -> hacia la derecha positivos, desde width/2 hasta width:
        for (int w = width / 2; w <= width; w += 100) {

            x1 = w;
            y1 = height / 2 - 5;
            x2 = w;
            y2 = height / 2 + 5;
            g.drawLine(x1, y1, x2, y2);

        }
        //Hacia la izquierda los negativos:
        for (int w = width / 2; w >= 0; w -= 100) {

            x1 = w;
            y1 = height / 2 - 5;
            x2 = w;
            y2 = height / 2 + 5;
            g.drawLine(x1, y1, x2, y2);

        }
        
        //TODO - Se puede hacer en un único bucle
        //Divisiones principales, cada 100 píxeles, de tamaño 10 pixeles:
        //En el eje de abscisas -> hacia arriba negativos, desde height/2 hasta 0:
        for (int h = height / 2; h >= 0; h -= 100) {

            x1 = width / 2 - 5;
            y1 = h;
            x2 = width / 2 + 5;
            y2 = h;
            g.drawLine(x1, y1, x2, y2);

        }
        //Hacia abajo los positivos:
        for (int h = height / 2; h <= height; h += 100) {

            x1 = width / 2 - 5;
            y1 = h;
            x2 = width / 2 + 5;
            y2 = h;
            g.drawLine(x1, y1, x2, y2);

        }

        this.scenarioToPaint.paint(g2);

    }

}
