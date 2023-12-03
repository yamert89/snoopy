package yamert89.snoopy;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;
import yamert89.snoopy.compile.ClassModifier;
import yamert89.snoopy.compile.DefaultClassModifier;

public class ModifyClasses extends DefaultTask {

    @TaskAction
    public void execute() {
        Project project = getProject();
        SnoopyPluginExtension extension = project.getExtensions().findByType(SnoopyPluginExtension.class);
        String basePackage = extension.getBasePackage().getOrElse("");
        String classDir = extension.getClassDir().getOrElse("classes/java/main");
        String resourcesDir = extension.getResourcesDir().getOrElse("resources/main");
        String baseDir = project.getLayout().getBuildDirectory().getAsFile().get().getPath();
        System.out.println("Task snoopyCompile started with basePackage: " + basePackage);
        ClassModifier classModifier = new DefaultClassModifier();

        classModifier.modify(baseDir + "/" + classDir, baseDir + "/" + resourcesDir);
    }
}
