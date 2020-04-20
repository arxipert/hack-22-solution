package agent;

import java.lang.instrument.ClassFileTransformer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.ProtectionDomain;

public class ClassLogger implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer)  {
        try {
            Path path = Paths.get("classes/" + className + ".class");
            Files.write(path, classfileBuffer);
        } catch (Throwable ignored) { // ignored, don’t do this at home kids
        } finally { return classfileBuffer; }
    }
}