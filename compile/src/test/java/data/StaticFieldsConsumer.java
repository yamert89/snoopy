package data;

public class StaticFieldsConsumer {
    public static String getFinalStringFromGetter() {
        return new ReplaceSQLExample().getSQL4();
    }
}
