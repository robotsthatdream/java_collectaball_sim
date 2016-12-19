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

import es.udc.gii.collectaball.util.RandomUtil;
import es.udc.gii.collectaball.robot.BallCollectorRobotToPaint;
import es.udc.gii.collectaball.scenario.BallScenario;
import es.udc.gii.collectaball.scenario.CollectBallWithDoorScenario;
import es.udc.gii.collectaball.robot.MDBBallCollectorRobot;
import es.udc.gii.collectaball.scenario.Scenario;
import es.udc.gii.collectaball.view.paint.ScenarioToPaint;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

/**
 *
 * @author GII
 */
public class RobotSimulator extends Thread{

    private static String robotConfigurationPath;
    private static String mdbConfigurationPath;

    private static Boolean X_MODE = Boolean.TRUE;

    public enum Mode {

        MDB, STANDALONE
    };
    private static Mode mode;

    public RobotSimulator() {
        
    }

    /**
     * Creates new form RobotSimulator
     */
    
    
    public void run() {
        try {

            //Primero creamos los componentes del escenario:            
            Scenario scenario;
            Configuration configuration = new XMLConfiguration(robotConfigurationPath);
            Configuration scenarioConfig = configuration.subset("scenario");
            Configuration robotConfig = configuration.subset("robot");
            if (mode == Mode.MDB) {
                scenario = (Scenario) Class.forName(scenarioConfig.getString("class")).newInstance();
            } else {
                scenario = new CollectBallWithDoorScenario();
            }
            scenario.configure(scenarioConfig);
            ScenarioToPaint scenarioToPaint = new ScenarioToPaint(null, scenario);
            

            MDBBallCollectorRobot ccr;
//            if (mode == Mode.MDB) {
                ccr = new MDBBallCollectorRobot("robot");
//            } else {
//                ccr = new MDBBallCollectorRobot("robot");
//            }
            Thread thread = new Thread(ccr, "BallCollectorRobot");

            if (mode == Mode.MDB) {
                try {
                    ccr.configure(robotConfig);
                    ccr.configureMDB(new XMLConfiguration(mdbConfigurationPath));
                } catch (ConfigurationException ex) {
                    System.out.println("Error loading MDB configuration files");
                    Logger.getLogger(RobotSimulator.class.getName()).log(Level.SEVERE, null, ex);
                    System.exit(0);
                } 
            }

            BallCollectorRobotToPaint robot = new BallCollectorRobotToPaint(ccr);
            ccr.setBallCollectorRobotToPaint(robot);
            scenarioToPaint.addScenarioObject(BallScenario.ROBOT_ID, robot);
            scenarioToPaint.configure(scenarioConfig.subset("scenarioToPaint"));

            if (X_MODE) {
                RobotSimulatorFrame rsf = new RobotSimulatorFrame();
                rsf.setScenario(scenarioToPaint);
            }

            thread.start();
        } catch (ConfigurationException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(RobotSimulator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        String error = null;
        String arg0 = (args.length > 0 ? args[0] : "");
        int argIndex = 1;
        int numMDBArgs = 3;
        
        RandomUtil.init();

        if (arg0.equalsIgnoreCase("-x")) {
            X_MODE = Boolean.TRUE;
            arg0 = (args.length > 1 ? args[1] : "");
            argIndex = 2;
            numMDBArgs = 4;
        } else {
            X_MODE = Boolean.FALSE;
        }

        if (arg0.equalsIgnoreCase("-mdb")) {
            mode = Mode.MDB;
        } else {
            mode = Mode.STANDALONE;
        }
        if (mode == Mode.MDB) {
            if (args.length == numMDBArgs) {
                robotConfigurationPath = args[argIndex];
                mdbConfigurationPath = args[argIndex + 1];
            } else {
                error = "Usage for mdb configuration: [-x] -mdb RobotConfigFile.xml MDBConfigFile.xml";
            }
        } else {
            robotConfigurationPath = args[1];
        }

        if (error == null) {
            RobotSimulator rs = new RobotSimulator();
            rs.start();
        } else {
            System.out.println(error);
        }

    }

    public static void setMdbConfigurationPath(String mdbConfigurationPath) {
        RobotSimulator.mdbConfigurationPath = mdbConfigurationPath;
    }

    public static void setRobotConfigurationPath(String robotConfigurationPath) {
        RobotSimulator.robotConfigurationPath = robotConfigurationPath;
    }

    public static void setX_MODE(Boolean X_MODE) {
        RobotSimulator.X_MODE = X_MODE;
    }

    public static void setMode(Mode mode) {
        RobotSimulator.mode = mode;
    }
    
    
    

}
