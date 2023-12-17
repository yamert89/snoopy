package yamert89.snoopy.compile;

import java.util.Objects;

public record ClassField(String name, boolean isTarget, String newValue) {

    public static ClassField notTargetInstance(String name) {
        return new ClassField(name, false, null);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ClassField that = (ClassField) object;
        return isTarget == that.isTarget && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isTarget);
    }
}
