package yamert89.snoopy.compile;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class ClassMetadata {
    private final boolean isTarget;
    private final String targetFieldsPrefix;
    private final Set<MappedField> mappedFields;

    public static ClassMetadata notTargetInstance() {
        return new ClassMetadata();
    }

    public static ClassMetadata defaultTargetInstance() {
        return new ClassMetadata(true, "SQL", Collections.emptySet());
    }

    public static ClassMetadata targetInstanceWithPrefix(String targetFieldsPrefix) {
        return new ClassMetadata(true, targetFieldsPrefix, Collections.emptySet());
    }

    public static ClassMetadata targetInstanceWithMappedFields(Set<MappedField> mappedFields) {
        return new ClassMetadata(true, null, mappedFields);
    }

    private ClassMetadata() {
        isTarget = false;
        targetFieldsPrefix = null;
        mappedFields = Collections.emptySet();
    }

    private ClassMetadata(boolean isTarget, String targetFieldsPrefix, Set<MappedField> mappedFields) {
        this.isTarget = isTarget;
        this.targetFieldsPrefix = targetFieldsPrefix;
        this.mappedFields = mappedFields;
    }

    public boolean isTarget() {
        return isTarget;
    }

    public String getTargetFieldsPrefix() {
        return targetFieldsPrefix;
    }

    public Set<MappedField> getMappedFields() {
        return mappedFields;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ClassMetadata that = (ClassMetadata) object;
        return isTarget == that.isTarget && Objects.equals(targetFieldsPrefix, that.targetFieldsPrefix) && Objects.equals(mappedFields, that.mappedFields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isTarget, targetFieldsPrefix, mappedFields);
    }

    @Override
    public String toString() {
        return "ClassMetadata{" +
                "isTarget=" + isTarget +
                ", targetFieldsPrefix='" + targetFieldsPrefix + '\'' +
                ", mappedFields=" + mappedFields +
                '}';
    }
}
