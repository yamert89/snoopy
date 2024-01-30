package data;

import yamert89.snoopy.meta.InjectSQL;
import yamert89.snoopy.meta.InjectSQLField;

@InjectSQL(fieldsStartWith = "SQL")
public class Getters {
    private String regularField;

    private final String SQL1 = "sql1";
    @InjectSQLField(name = "SQL2")
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
