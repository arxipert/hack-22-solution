package agent;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class AgentLoader {

    private  AgentLoader() {}

    private static boolean isUsed = false;
    public static void loadAgent(String pid, String agentFilePath) throws Exception {

        if (!isUsed) {
            File toolsJar = getToolsLibrary();
            URLClassLoader child = new URLClassLoader(new URL[]{toolsJar.toURI().toURL()}, toolsJar.getClass().getClassLoader());
            Class<?> classToUse = Class.forName("com.sun.tools.attach.VirtualMachine", true, child);
            Method attachVm = classToUse.getMethod("attach", String.class);
            Method loadAgent = classToUse.getMethod("loadAgent", String.class);
            Method detachVm = classToUse.getMethod("detach");
            Object virtualMachine = attachVm.invoke(null, pid);
            loadAgent.invoke(virtualMachine, agentFilePath);
            detachVm.invoke(virtualMachine);
            isUsed = true;
        }
    }

    private static File getToolsLibrary() {
        String javaHome = System.getenv("JAVA_HOME");
        String toolsPath = javaHome + "/lib/tools.jar";
        return new File(toolsPath);
    }
}
