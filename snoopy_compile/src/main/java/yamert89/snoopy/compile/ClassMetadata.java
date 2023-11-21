package yamert89.snoopy.compile;

import java.util.Objects;

public class ClassMetadata {
    private final boolean isTarget;
    private final String targetFieldsPrefix;

    public ClassMetadata() {
        isTarget = false;
        targetFieldsPrefix = null;
    }

    public ClassMetadata(boolean isTarget, String targetFieldsPrefix) {
        this.isTarget = isTarget;
        this.targetFieldsPrefix = targetFieldsPrefix;
    }

    public boolean isTarget() {
        return isTarget;
    }

    public String getTargetFieldsPrefix() {
        return targetFieldsPrefix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassMetadata that = (ClassMetadata) o;
        return isTarget == that.isTarget && Objects.equals(targetFieldsPrefix, that.targetFieldsPrefix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isTarget, targetFieldsPrefix);
    }

    @Override
    public String toString() {
        return "ClassMetadata{" +
                "isTarget=" + isTarget +
                ", targetFieldsPrefix='" + targetFieldsPrefix + '\'' +
                '}';
    }
}
