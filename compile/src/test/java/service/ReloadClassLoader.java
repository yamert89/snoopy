package service;

import java.io.IOException;
import java.io.InputStream;

public class ReloadClassLoader extends ClassLoader {

    public Class<?> loadClass(Class<?> cl) {
        try {
            String path = cl.getName().replace(".", "/") + ".class";
            InputStream is = cl.getClassLoader().getResourceAsStream(path);
            byte[] buf = new byte[10000];
            int len = is.read(buf);
            is.close();
            return defineClass(cl.getName(), buf, 0, len);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
