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
package es.udc.gii.collectaball.util;

import org.apache.commons.math.random.MersenneTwister;

/**
 *
 * @author GII
 */
public class RandomUtil {
    
    
    private static MersenneTwister generator;
    
    private RandomUtil() {
    
    }
    
    public static void init() {
        if (generator == null) {
            generator = new MersenneTwister(System.currentTimeMillis());
        }
    }
    
    public static double nextGaussian() {
        return generator.nextGaussian();
    }
    
    public static double nextDouble() {
        return generator.nextDouble();
    }
    
    public static int nextInt() {
        return generator.nextInt();
    }
    
    public static int nextInt(int n) {
        return generator.nextInt(n);
    }
    
    public static boolean nextBoolean() {
        return generator.nextBoolean();
        
    }
    
    public static float nextFloat() {
        return generator.nextFloat();
    }
    
    public static long nextLong() {
        return generator.nextLong();
    }
    
    public static void nextBytes(byte[] bytes) {
        generator.nextBytes(bytes);
    }
    
}
