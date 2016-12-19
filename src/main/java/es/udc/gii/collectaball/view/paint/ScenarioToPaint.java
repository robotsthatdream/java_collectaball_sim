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
package es.udc.gii.collectaball.view.paint;

import es.udc.gii.collectaball.util.config.Configurable;
import es.udc.gii.callectaball.object.BlueBox;
import es.udc.gii.callectaball.object.BlueBoxToPaint;
import es.udc.gii.collectaball.robot.BallCollectorRobotToPaint;
import es.udc.gii.collectaball.scenario.BallScenario;
import es.udc.gii.collectaball.scenario.CollectBallsScenario;
import es.udc.gii.callectaball.object.GreenButton;
import es.udc.gii.callectaball.object.GreenButtonToPaint;
import es.udc.gii.callectaball.object.RedBall;
import es.udc.gii.callectaball.object.RedBallToPaint;
import es.udc.gii.callectaball.object.Wall;
import es.udc.gii.callectaball.object.WallToPaint;
import es.udc.gii.callectaball.object.Door;
import es.udc.gii.callectaball.object.DoorToPaint;
import es.udc.gii.collectaball.scenario.Scenario;
import es.udc.gii.callectaball.object.StateObject;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.Configuration;

/**
 * Decora al escenario no lo extiende, lo encapsula para pintarlo
 *
 * @author GII
 * @param <O>
 */
public class ScenarioToPaint<O extends ObjectToPaint> implements Configurable {

    private Canvas canvasToBePainted;

    private Scenario scenario;

    private Map<String, O> objectsToPaint;

    public ScenarioToPaint(Canvas canvasToBePainted, Scenario scenario) {
        this.canvasToBePainted = canvasToBePainted;
        this.scenario = scenario;
        scenario.setScenarioToPaint(this);
    }

    public void paint(Graphics2D g) {
        int iteration = 0;
        O lastToPaint = null;
        for (O otp : objectsToPaint.values()) {
            if (otp instanceof BallCollectorRobotToPaint) {
                lastToPaint = otp;
                iteration = ((BallCollectorRobotToPaint) otp).getScenarioObject().getRobotIteration();
            } else {
                otp.paint(g, scenario.getWidth() / 2, scenario.getHeight() / 2);
            }
        }
        if (lastToPaint != null) {
            lastToPaint.paint(g, scenario.getWidth() / 2, scenario.getHeight() / 2);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Iteration = " + String.format("%05d", iteration), 600, 30);
        }

    }

    public void fireStateChanged() {
        if (canvasToBePainted != null) {
            canvasToBePainted.repaint();
        }
    }

    public Canvas getCanvasToBePainted() {
        return canvasToBePainted;
    }

    public O getScenarioObject(String id) {
        return this.objectsToPaint.get(id);
    }

    public Scenario getScenario() {
        return this.scenario;
    }

    public void addScenarioObject(String id, O scenarioObject) {
        if (this.objectsToPaint == null) {
            this.objectsToPaint = new HashMap<>();
        }
        this.objectsToPaint.put(id, scenarioObject);
        scenarioObject.setScenario(this);
        //Y añadimos al decorado:
        scenario.addScenarioObject(id, scenarioObject.getScenarioObject());
    }

    public List<O> getObjectsById(String id) {
        List<O> objects = new ArrayList<>();

        for (String str : objectsToPaint.keySet()) {

            if (str.contains(id)) {
                objects.add(objectsToPaint.get(str));
            }
        }

        return objects;

    }

    @Override
    public void configure(Configuration configuration) {
        Configuration ballSubset = configuration.subset("ball");

        RedBall rc;
        RedBallToPaint rctp;
        double gbX = ballSubset.getDouble("x", -100);
        double gbY = ballSubset.getDouble("y", 100);
        boolean gbChangePosition = ballSubset.getBoolean("changePosition");

        rc = new RedBall(CollectBallsScenario.BALL_ID + "(0)");

        rctp = new RedBallToPaint(rc);
        rctp.setX(gbX);
        rctp.setY(gbY);
        rctp.setChangePosition(gbChangePosition);
        addScenarioObject(CollectBallsScenario.BALL_ID + "(0)", (O) rctp);

        Configuration boxSubset = configuration.subset("box");

        if (boxSubset != null && !boxSubset.isEmpty()) {
            BlueBoxToPaint bbtp;
            BlueBox bb;
            gbX = boxSubset.getInt("x", 100);
            gbY = boxSubset.getInt("y", -100);
            gbChangePosition = boxSubset.getBoolean("changePosition", Boolean.FALSE);
            bb = new BlueBox(CollectBallsScenario.BOX_ID + "(0)");

            bbtp = new BlueBoxToPaint(bb);
            bbtp.setX(gbX);
            bbtp.setY(gbY);
            bbtp.setChangePosition(gbChangePosition);
            addScenarioObject(CollectBallsScenario.BOX_ID + "(0)", (O) bbtp);

        }

        Configuration wallWithDoorSubset = configuration.subset("wallWithDoor");
        if (wallWithDoorSubset != null && !wallWithDoorSubset.isEmpty()) {
            String orientation = wallWithDoorSubset.getString("orientation");
            int coordinate = wallWithDoorSubset.getInt("coordinate");
            int thick = wallWithDoorSubset.getInt("thick");
            int doorSize = wallWithDoorSubset.getInt("doorSize");
            int doorCenter = wallWithDoorSubset.getInt("doorCenter");

            if (orientation.equalsIgnoreCase("horizontal")) {
                //Pintamos la puerta
                int y = coordinate;
                int xDoor = doorCenter;
                Door yd = new Door(BallScenario.DOOR_ID + "(0)", Door.CLOSED, doorSize, thick, orientation);
                DoorToPaint ydtp = new DoorToPaint(yd);
                ydtp.setX(xDoor);
                ydtp.setY(y);
                addScenarioObject(yd.getId(), (O) ydtp);

                //Pintamos la pared a la izquierda de la puerta
                //Centro de la pared:
                int leftWallWidth = xDoor + scenario.getWidth() / 2 - doorSize / 2;
                int xLeftWall = -scenario.getWidth() / 2 + leftWallWidth / 2;
                Wall leftWall = new Wall(BallScenario.WALL_ID + "(0)", leftWallWidth, thick, orientation);
                WallToPaint leftWTP = new WallToPaint(leftWall);
                leftWTP.setX(xLeftWall);
                leftWTP.setY(y);
                addScenarioObject(leftWall.getId(), (O) leftWTP);

                //Pintamos la pared a la derecha de la puerta
                //Centro de la pared:
                int rwallSize = scenario.getWidth() / 2 - xDoor - doorSize / 2;
                int xRightWall = scenario.getWidth() / 2 - rwallSize / 2;
                Wall rightWall = new Wall(BallScenario.WALL_ID + "(1)", rwallSize, thick, orientation);
                WallToPaint rightWTP = new WallToPaint(rightWall);
                rightWTP.setX(xRightWall);
                rightWTP.setY(y);
                addScenarioObject(rightWall.getId(), (O) rightWTP);

            } else { //vertical
                //Pintamos la puerta
                int x = coordinate;
                int yDoor = doorCenter;
                Door yd = new Door(BallScenario.DOOR_ID + "(0)", Door.CLOSED, thick, doorSize, orientation);
                DoorToPaint ydtp = new DoorToPaint(yd);
                ydtp.setY(doorCenter);
                ydtp.setX(x);
                addScenarioObject(yd.getId(), (O) ydtp);

                //Pintamos la pared a la izquierda de la puerta
                //Centro de la pared:
                int upWallHeigh = yDoor + scenario.getHeight() / 2 - doorSize / 2;
                int yUpWall = -scenario.getHeight() / 2 + upWallHeigh / 2;
                Wall upWall = new Wall(BallScenario.WALL_ID + "(0)", thick, upWallHeigh, orientation);
                WallToPaint upWTP = new WallToPaint(upWall);
                upWTP.setX(x);
                upWTP.setY(yUpWall);
                addScenarioObject(upWall.getId(), (O) upWTP);

                //Pintamos la pared a la derecha de la puerta
                //Centro de la pared:
                int downWallSize = scenario.getHeight() / 2 - yDoor - doorSize / 2;
                int yDownWall = scenario.getHeight() / 2 - downWallSize / 2;
                Wall downWall = new Wall(BallScenario.WALL_ID + "(1)", thick, downWallSize, orientation);
                WallToPaint downWTP = new WallToPaint(downWall);
                downWTP.setX(x);
                downWTP.setY(yDownWall);
                addScenarioObject(downWall.getId(), (O) downWTP);
            }

        }
        Configuration buttonSubset = configuration.subset("button");
        if (buttonSubset != null && !buttonSubset.isEmpty()) {
            GreenButtonToPaint gbtp;
            GreenButton gb;
            gbX = buttonSubset.getInt("x", 100);
            gbY = buttonSubset.getInt("y", -100);
            gbChangePosition = buttonSubset.getBoolean("changePosition", Boolean.FALSE);

            gb = new GreenButton(CollectBallsScenario.BUTTON_ID + "(0)", GreenButton.NOT_PUSHED, 50, 50);

            gbtp = new GreenButtonToPaint(gb);
            gbtp.setX(gbX);
            gbtp.setY(gbY);
            gbtp.setChangePosition(gbChangePosition);
            if (buttonSubset.containsKey("target")) {
                String targetId = buttonSubset.getString("target");

                StateObject scenarioObject = (StateObject) (getScenarioObject(targetId)).getScenarioObject();
                gb.setTargetObject(scenarioObject);
            }
            addScenarioObject(CollectBallsScenario.BUTTON_ID + "(0)", (O) gbtp);

        }

    }

    public void setCanvasToBePainted(Canvas jcSimulatorCanvas) {
        this.canvasToBePainted = jcSimulatorCanvas;
    }
}
