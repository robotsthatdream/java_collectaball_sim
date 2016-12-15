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
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author pilar
 */
public class RobotSimulatorFrame extends JFrame {

    private es.udc.gii.collectaball.view.RobotSimulatorCanvas jcSimulatorCanvas;
    private javax.swing.JPanel jpContentPane;

    public RobotSimulatorFrame() {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RobotSimulator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        initComponents();
    }

    private void initComponents() {

        jpContentPane = new javax.swing.JPanel();
        jcSimulatorCanvas = new es.udc.gii.collectaball.view.RobotSimulatorCanvas();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Simulador Robot");

        jpContentPane.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jpContentPaneLayout = new javax.swing.GroupLayout(jpContentPane);
        jpContentPane.setLayout(jpContentPaneLayout);
        jpContentPaneLayout.setHorizontalGroup(
                jpContentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jcSimulatorCanvas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jpContentPaneLayout.setVerticalGroup(
                jpContentPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jcSimulatorCanvas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jpContentPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jpContentPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
        );

        pack();
    }// </editor-fold>   

    public void setScenario(ScenarioToPaint scenario) {

        scenario.setCanvasToBePainted(jcSimulatorCanvas);
        
        this.jcSimulatorCanvas.setScenarioToPaint(scenario);
        //Ponemos el tamaño del frame y el tamaño del canvas según el tamaño del escenario:
        this.setSize(scenario.getScenario().getWidth(), scenario.getScenario().getHeight());
        this.setPreferredSize(new Dimension(scenario.getScenario().getWidth() + 20, scenario.getScenario().getHeight() + 40));
        this.jpContentPane.setSize(scenario.getScenario().getWidth(), scenario.getScenario().getHeight());
        this.jpContentPane.setPreferredSize(new Dimension(scenario.getScenario().getWidth(), scenario.getScenario().getHeight()));
        this.jcSimulatorCanvas.setSize(scenario.getScenario().getWidth(), scenario.getScenario().getHeight());
        this.jcSimulatorCanvas.setPreferredSize(new Dimension(scenario.getScenario().getWidth(), scenario.getScenario().getHeight()));
        this.pack();
        this.setResizable(Boolean.TRUE);
        this.setVisible(Boolean.TRUE);
        
        

    }

}
