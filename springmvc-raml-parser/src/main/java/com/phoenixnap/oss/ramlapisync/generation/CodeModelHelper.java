package com.phoenixnap.oss.ramlapisync.generation;

import com.sun.codemodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

/**
 * The class provides static helper methods for JCodeModel related tasks.
 *
 * @author armin.weisser
 * @since 0.3.2
 */
public abstract class CodeModelHelper {

    /**
     *
     * @param codeModel
     * @param simpleClassName
     * @return the first class in any package that matches the simple class name.
     */
    public static JClass findFirstClassBySimpleName(JCodeModel codeModel, String simpleClassName) {
        Iterator<JPackage> packages = codeModel.packages();
        while(packages.hasNext()) {
            JPackage jPackage = packages.next();
            Iterator<JDefinedClass> classes = jPackage.classes();
            while(classes.hasNext()) {
                JDefinedClass aClass = classes.next();
                if(aClass.name().equals(simpleClassName)) {
                    return aClass;
                }
            }
        }
        throw new InvalidModelException("No unique class found for simple class name " + simpleClassName);
    }

    public static JExtMethod ext(JMethod jMethod, JCodeModel jCodeModel) {
        return new JExtMethod(jMethod, jCodeModel);
    }

    public static String getVersion() {
        try {
            Properties prop = new Properties();
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("parser.properties");
            prop.load(in);
            in.close();
            return prop.getProperty("parser.version");
        } catch (IOException e) {
            return "???";
        }
    }

    /**
     * Helper class because JMethod does not expose it's JCodeModel.
     */
    public static class JExtMethod {

        private final JMethod jMethod;
        private final JCodeModel owner;

        public JExtMethod(JMethod jMethod, JCodeModel jCodeModel) {
            this.jMethod = jMethod;
            this.owner = jCodeModel;
        }

        public JMethod get() {
            return jMethod;
        }

        public JCodeModel owner() {
            return owner;
        }
    }
}
