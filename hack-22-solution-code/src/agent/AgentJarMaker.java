package agent;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class AgentJarMaker {
     private static Manifest manifest = new Manifest();

    public static void makeJar(String relPath) throws IOException {
        addAttribute("Manifest-Version", "1.0");
        addAttribute("Agent-Class", Agent.class.getName());
        addAttribute("Premain-Class", Agent.class.getName());
        addAttribute("Can-Redefine-Classes", "true");
        addAttribute("Can-Retransform-Classes", "true");
        addAttribute("Can-Set-Native-Method-Prefix", "true");
        addAttribute("Implementation-Title", ClassLogger.class.getName());
        addAttribute("Implementation-Version", "1.0");

        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(relPath), manifest);
        addClass(Agent.class, jarOutputStream);
        addClass(ClassLogger.class, jarOutputStream);
        jarOutputStream.close();
    }

    private static void addClass(Class c, JarOutputStream jarOutputStream) throws IOException {
        String path = c.getName().replace('.', '/') + ".class";
        jarOutputStream.putNextEntry(new JarEntry(path));
        jarOutputStream.write(toByteArray(c.getClassLoader().getResourceAsStream(path)));
        jarOutputStream.closeEntry();
    }

    private static void addAttribute (String attrName, String attrValue){
        manifest.getMainAttributes().put(new Attributes.Name(attrName), attrValue);
    }

    private static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[0x1000];
        while (true) {
            int r = in.read(buf);
            if (r == -1) {
                break;
            }
            out.write(buf, 0, r);
        }
        return out.toByteArray();
    }
}
