package yamert89.snoopy;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;
import yamert89.snoopy.compile.ClassModifier;
import yamert89.snoopy.compile.DefaultClassModifier;
import yamert89.snoopy.compile.FileScanner;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ModifyClasses extends DefaultTask {

    @TaskAction
    public void execute() throws IOException {
        Project project = getProject();
        SnoopyPluginExtension extension = project.getExtensions().findByType(SnoopyPluginExtension.class);
        if (extension == null) throw new IllegalStateException("SnoopyPluginExtension not found");
        String classDir = extension.getClassDir().getOrElse("classes/java/main");
        String resourcesDir = extension.getResourcesDir().getOrElse("resources/main");
        String baseDir = project.getLayout().getBuildDirectory().getAsFile().get().getPath();
        classDir = extension.getClassDir().isPresent() ? classDir : (baseDir + "/" + classDir);
        resourcesDir = extension.getResourcesDir().isPresent() ? resourcesDir : (baseDir + "/" + resourcesDir);
        System.out.println("classDir: " + classDir);
        System.out.println("resourcesDir: " + resourcesDir);
        ClassModifier classModifier = new DefaultClassModifier();

        List<File> classFiles = new FileScanner(classDir, "class").scan();
        List<File> sqlFiles = new FileScanner(resourcesDir, "sql").scan();
        classModifier.modify(classFiles, sqlFiles);
    }
}
