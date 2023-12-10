package yamert89.snoopy.compile;

import java.util.Objects;

public class MappedField {
    private final String fieldName;

    public MappedField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MappedField that = (MappedField) object;
        return Objects.equals(fieldName, that.fieldName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldName);
    }

    @Override
    public String toString() {
        return "MappedField{" +
                "fieldName='" + fieldName + '\'' +
                '}';
    }
}
