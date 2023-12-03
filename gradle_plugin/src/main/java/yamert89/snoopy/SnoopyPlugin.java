package yamert89.snoopy;

import org.gradle.api.NonNullApi;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.jvm.toolchain.JavaLanguageVersion;
import org.jetbrains.annotations.NotNull;

@NonNullApi
abstract public class SnoopyPlugin implements Plugin<Project> {
       @Override
    public void apply(@NotNull Project project) {
        System.out.println("SnoopyPlugin applied");
        project.getTasks().register("snoopyCompile", ModifyClasses.class);
        project.getExtensions().create("snoopy", SnoopyPluginExtension.class);

        JavaPluginExtension java = project.getExtensions().getByType(JavaPluginExtension.class);
        java.getToolchain().getLanguageVersion().set(JavaLanguageVersion.of(17));
    }
}
