package yamert89.snoopy;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import yamert89.snoopy.compile.ClassModifier;
import yamert89.snoopy.compile.ClassScanner;
import yamert89.snoopy.compile.DefaultClassModifier;

import java.io.IOException;

public class ModifyClasses extends DefaultTask {

    @TaskAction
    public void execute() {
        String basePackage = getProject().getExtensions().findByType(SnoopyPluginExtension.class).getBasePackage().getOrElse("");
        String rootPath = getProject().getExtensions().findByType(SnoopyPluginExtension.class).getRootPath().getOrElse("");
        System.out.println("Task snoopyCompile started with basePackage: " + basePackage);
        ClassModifier classModifier = new DefaultClassModifier();

        classModifier.modify(rootPath, basePackage);
    }
}
