package yamert89.snoopy;

import org.gradle.api.*;
import org.gradle.api.provider.Property;
import org.jetbrains.annotations.NotNull;

@NonNullApi
abstract public class SnoopyPlugin implements Plugin<Project> {
    @Override
    public void apply(@NotNull Project project) {
        System.out.println("SnoopyPlugin applied");
        project.getTasks().register("snoopyCompile", ModifyClasses.class);
    }
}
