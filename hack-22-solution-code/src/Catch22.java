import agent.AgentJarMaker;
import agent.AgentLoader;

import java.io.File;
import java.lang.management.ManagementFactory;

class Catch22 extends Yossarian {
    static Yossarian loophole() throws Throwable {
        String agentPath = "agent.jar";
        File agentFile = new File(agentPath);
        if (!agentFile.exists()) {
            AgentJarMaker.makeJar(agentPath);
        }
        loadAgent(agentFile.getAbsolutePath());

        return new Catch22();
    }

    private static void loadAgent(String jarFilePath) throws Exception {
        String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
        String pid = nameOfRunningVM.substring(0, nameOfRunningVM.indexOf('@'));
        AgentLoader.loadAgent(pid, jarFilePath);
    }
}


