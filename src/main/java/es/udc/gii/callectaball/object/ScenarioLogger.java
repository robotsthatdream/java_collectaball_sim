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

import es.udc.gii.collectaball.scenario.Scenario;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for loggin the scenario state 
 * @author GII
 */
public class ScenarioLogger {

    private Scenario scenario;
    private PrintStream p;
    private File out;
    private int iteration;
    private String currentLine = null;
    private int cycles;

    public ScenarioLogger(Scenario scenario, String path2file, String logName, int cycles) {

        try {
            this.scenario = scenario;
            this.iteration = 0;
            this.cycles = cycles;
            if (!path2file.startsWith(File.separator)) {
                path2file = System.getProperty("user.dir") + File.separator + path2file;
            }

            File path = new File(path2file);
            path.mkdirs();

            out = new File(path, logName + "-" + getLogID(path, logName + "-" + getClass().getSimpleName()) + ".txt");
            p = new PrintStream(out, "UTF-8");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ScenarioLogger.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ScenarioLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void doLogAndFinishIteration() {
        doLog();
        finishIterationLog();
    }

    /**
     * Add to the current log line the properties of all the elements of the scenario
     */
    public void doLog() {
        if (currentLine == null) {
            doHead();
        }
        Set<String> keys = scenario.getScenarioObjects().keySet();
        SortedSet<String> sortedKeys = new TreeSet<>(keys);
        for (String k : sortedKeys) {
            Object scenarioObject = scenario.getScenarioObjects().get(k);
            currentLine += "\t" + ((ScenarioObject) scenarioObject).lineLog();
        }
    }

    public void doHead() {
        currentLine = "#";

        for (int i = 0; i < cycles; i++) {
            Set<String> keys = scenario.getScenarioObjects().keySet();
            SortedSet<String> sortedKeys = new TreeSet<>(keys);
            for (String k : sortedKeys) {
                Object scenarioObject = scenario.getScenarioObjects().get(k);
                currentLine += "\t" + ((ScenarioObject) scenarioObject).headLog();
            }
        }
        finishIterationLog();

    }

    /**
     * Write the current log line in the file
     */
    public void finishIterationLog() {
        if (!currentLine.startsWith("#")) {
            String[] split = currentLine.split("\t");
            List<Double> values = new ArrayList<>();
            for (String s : split) {
                try {
                    double d = Double.valueOf(s);
                    values.add(d);
                } catch (Exception e) {
                    values.add(0.0);
                }
            }
        }
        p.println(currentLine);
        iteration++;
        currentLine = "" + iteration;
    }

    private String getLogID(File path2file, final String logName) {
        String logID = "";
        File[] files = path2file.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File folder, String filename) {
                return filename.contains(logName);
            }
        });

        logID = "_" + Integer.toString(files.length + 1);

        return logID;
    }

    @Override
    public String toString() {
        return "ScenarioLogger";
    }

}
