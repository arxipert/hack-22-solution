package agent;

import javax.xml.bind.DatatypeConverter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;

public class Agent {
    private Agent () {}

    public static void premain(String args, Instrumentation instrumentation) {
        ClassLogger transformer = new ClassLogger();
        instrumentation.addTransformer(transformer);
        String path;
        try {
            path = Class.forName("Yossarian")
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath()
                    +"Yossarian.class";

        byte [] targetClassFile = changeFalseHex(path);
        instrumentation.redefineClasses(new ClassDefinition(Class.forName("Yossarian"), targetClassFile));

        } catch (Exception e) {
            e.printStackTrace();
        }
}

    public static void agentmain(String args, Instrumentation instrumentation){
        premain(args, instrumentation);
    }

    private static byte[] changeFalseHex (String path){
        File f = new File(path);
        byte[] targetClassFile = new byte[(int)f.length()];

        try (DataInputStream dis = new DataInputStream(new FileInputStream(f))) {
        dis.readFully(targetClassFile);
        } catch (Exception e) {e.printStackTrace();}

        String hex = DatatypeConverter.printHexBinary(targetClassFile);
        int falseIndex = hex.lastIndexOf("0203");
        StringBuilder builder = new StringBuilder(hex);
        builder.replace(falseIndex, falseIndex+4, "0204");
        return DatatypeConverter.parseHexBinary(builder.toString());
    }
}