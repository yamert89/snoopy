package yamert89.snoopy;

import org.gradle.api.provider.Property;

public interface SnoopyPluginExtension {
    Property<String> getClassDir();
    Property<String> getResourcesDir();
}
