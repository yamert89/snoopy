package com.github.yamert89.snoopy.compile;

import java.util.Objects;

public class ClassField {
    private final String name;
    private final boolean isTarget;
    private boolean isInitialized;
    private final String newValue;

    public ClassField(String name, boolean isTarget, boolean isInitialized, String newValue) {
        this.name = name;
        this.isTarget = isTarget;
        this.isInitialized = isInitialized;
        this.newValue = newValue;
    }

    public static ClassField notTargetInstance(String name) {
        return new ClassField(name, false, false, null);
    }

    public void setInitialized(boolean initialized) {
        isInitialized = initialized;
    }

    public String getName() {
        return name;
    }

    public boolean isTarget() {
        return isTarget;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public String getNewValue() {
        return newValue;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ClassField that = (ClassField) object;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "ClassField{" +
                "name='" + name + '\'' +
                ", isTarget=" + isTarget +
                ", isInitialized=" + isInitialized +
                ", newValue='" + newValue + '\'' +
                '}';
    }
}
