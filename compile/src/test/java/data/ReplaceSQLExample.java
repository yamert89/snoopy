package data;

import yamert89.snoopy.meta.InjectSQL;

@InjectSQL(fieldsStartWith = "SQL")
public class ReplaceSQLExample {
    public final String SQL1 = "sql1";
    public String SQL2 = "sql2";
    private String SQL3 = "sql3";
    public final String regularField = "regularField";
    private final String SQL4 = "sql4";
    private String SQL5;

}
