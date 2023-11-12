package data;

import yamert89.snoopy.compile.meta.ReplaceSQL;

@ReplaceSQL(fieldsStartWith = "SQL")
public class ReplaceSQLExample {

    public static final String SQL1 = "sql1";
    public static final String SQL2 = "sql2";
    public static final String regularField = "regularField";
}
