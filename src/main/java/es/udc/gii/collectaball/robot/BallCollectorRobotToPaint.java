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
package es.udc.gii.collectaball.robot;

import es.udc.gii.collectaball.scenario.CollectBallsScenario;
import es.udc.gii.collectaball.robot.sensor.LoadSensor;
import es.udc.gii.collectaball.view.paint.ObjectToPaint;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

/**
 *
 * @author GII
 */
public class BallCollectorRobotToPaint extends ObjectToPaint<MDBBallCollectorRobot> {

    private static final int HEIGHT = 70;

    private static final int WIDTH = 70;

    public BallCollectorRobotToPaint(MDBBallCollectorRobot r) {
        super(r);
    }

    @Override
    public void paint(Graphics2D g, int x, int y) {

        double x1, y1;
        x1 = this.getX() + x;
        y1 = y - this.getY();

        g.setColor(new Color(255, 255, 255, 200));
        g.fillOval((int) x1 - WIDTH / 2, (int) y1 - HEIGHT / 2, WIDTH, HEIGHT);

        g.setColor(Color.BLACK);
        Stroke widthStroke = new BasicStroke(3.0f);
        g.setStroke(widthStroke);
        g.drawOval((int) x1 - WIDTH / 2, (int) y1 - HEIGHT / 2, WIDTH, HEIGHT);

        g.setColor(Color.RED);

        double angularSpeed = this.getScenarioObject().getRobotOrientation();

        double x2, y2;
        x2 = (x1 + Math.cos(-angularSpeed) * (WIDTH / 2));
        y2 = (y1 + Math.sin(-angularSpeed) * (HEIGHT / 2));

        g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);

        LoadSensor ls = (LoadSensor) this.getScenarioObject().getSensors().get(CollectBallsScenario.LOAD_SENSOR);

        if (ls != null) {
            int lsWidth = 25;
            int lsHeight = 25;
            g.setColor(Color.RED);
            if (ls.scenarioSensorization(this.getScenarioObject().getScenario()) > 0.0) {
                g.fillOval((int) x1 - lsWidth / 2, (int) y1 - lsHeight / 2, lsWidth, lsHeight);
            } else {
                g.drawOval((int) x1 - lsWidth / 2, (int) y1 - lsHeight / 2, lsWidth, lsHeight);
            }
        }

    }

}
