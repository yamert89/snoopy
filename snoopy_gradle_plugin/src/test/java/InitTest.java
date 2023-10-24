import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

import static org.gradle.internal.impldep.org.hamcrest.MatcherAssert.assertThat;

public class InitTest {
    @Test
    public void init(){
        Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply("yamert89.snoopy");
        assertThat("init2 ", project.getTasks().findByName("modifyClasses") != null);
    }
}
