package data;

import yamert89.snoopy.meta.ReplaceSQLField;

public class Getters {
    private String regularField;
    @ReplaceSQLField(name = "SQL1")
    private final String SQL1 = "sql1";
    @ReplaceSQLField(name = "SQL2")
    private String SQL2 = "sql2";

    public String getRegularField() {
        return regularField;
    }

    public String getSQL1() {
        return SQL1;
    }

    public String getSQL2() {
        return SQL2;
    }
}
