package yamert89.snoopy.compile;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ClassMetadata {
    private final boolean isTarget;
    private final String targetFieldsPrefix;
    private final List<ClassField> classFields;

    public static ClassMetadata notTargetInstance() {
        return new ClassMetadata(false, null, null);
    }

    public static ClassMetadata targetInstanceWithClassFields(List<ClassField> classFields) {
        return new ClassMetadata(true, null, classFields);
    }

    public ClassMetadata(String targetFieldsPrefix, List<ClassField> classFields) {
        isTarget = true;
        this.targetFieldsPrefix = targetFieldsPrefix;
        this.classFields = classFields;
    }

    private ClassMetadata(boolean isTarget, String targetFieldsPrefix, List<ClassField> classFields) {
        this.isTarget = isTarget;
        this.targetFieldsPrefix = targetFieldsPrefix;
        this.classFields = classFields;
    }

    public boolean isTarget() {
        return isTarget;
    }

    public String getTargetFieldsPrefix() {
        return targetFieldsPrefix;
    }

    public List<ClassField> getClassFields() {
        return new LinkedList<>(classFields);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ClassMetadata that = (ClassMetadata) object;
        return isTarget == that.isTarget && Objects.equals(targetFieldsPrefix, that.targetFieldsPrefix) && Objects.equals(classFields, that.classFields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isTarget, targetFieldsPrefix, classFields);
    }


}
