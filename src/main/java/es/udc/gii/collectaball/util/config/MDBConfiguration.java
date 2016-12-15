/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.udc.gii.collectaball.util.config;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

/**
 *
 * This class provides an object representation of a configuration file in XML.
 *
 * @author Borja Santos-Diez Vazquez
 */
public class MDBConfiguration extends XMLConfiguration {
    
    private static MDBConfiguration instance = null;
    private static String MDB_CONFIG_FILE;
    
    private MDBConfiguration() {
        super();
        
        try {
            this.load(MDB_CONFIG_FILE);
        } catch (ConfigurationException ex) {
            Logger.getLogger(MDBConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setConfigurationFilePath(String path) {
        MDB_CONFIG_FILE = path;
    }

    /**
     *
     * This class implements the Singleton design pattern. Through this method the instance
     * can be retrieved.
     *
     * @return The unique instance of this class.
     */

    public static MDBConfiguration getInstance() {
        
        if(instance == null) {
            instance = new MDBConfiguration();
        }
        
        return instance;
    }

    /**
     *
     * Simplifies the access to the configuration parameters. From a key of a
     * tag of the configuration file, returns the content directly as an object.
     * <p>
     * Useful when specifying classes in the configuration file or similar.
     *
     * @param arg Name of the tag to be retrieved.
     * @return An object resulting of the method {@link Class#forName} applied to the
     * string that specifies the class name. Later cast may be necessary.
     */

    public Object getObject(String arg) {
        Object o = null;
        try {
            o = Class.forName(this.getString(arg)).newInstance();
        } catch (InstantiationException ex) {
            //TODO - Cambiar por ConfigurationException
            Logger.getLogger(MDBConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            //TODO - Cambiar por ConfigurationException
            Logger.getLogger(MDBConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            //TODO - Cambiar por ConfigurationException
            Logger.getLogger(MDBConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return o;
        
    }
}
