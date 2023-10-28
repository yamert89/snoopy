package yamert89.snoopy;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import yamert89.snoopy.compile.ClassScanner;

import java.io.IOException;

public class ModifyClasses extends DefaultTask {

    @TaskAction
    public void execute() {
        Object basePackage = getProject().getProperties().get("rootPackage");
        ClassScanner scanner = new ClassScanner(basePackage == null ? "" : (String) basePackage);
        try {
            scanner.scanAndHandle();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
