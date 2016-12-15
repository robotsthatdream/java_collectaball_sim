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

import es.udc.gii.collectaball.view.paint.ColorUtils;
import es.udc.gii.collectaball.view.paint.ObjectToPaint;
import java.awt.Graphics2D;

/**
 *
 * @author GII
 */
public class DoorToPaint extends ObjectToPaint<Door> {

    public DoorToPaint(Door yd) {
        super(yd);
    }

    @Override
    public void paint(Graphics2D g, int x, int y) {

        Door yd = this.getScenarioObject();
        String color = yd.isOpen() ? yd.getOpenColor() : yd.getColor();
        if (!yd.isOpen()) {
            g.setColor(ColorUtils.hex2Rgb(color));
            g.fillRect((int) this.getX() + x - yd.getWidth() / 2, y - (int) this.getY() - yd.getHeight() / 2, yd.getWidth(), yd.getHeight());
        }
    }

}
