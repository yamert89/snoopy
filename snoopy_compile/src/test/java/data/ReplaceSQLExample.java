package data;

import yamert89.snoopy.compile.meta.ReplaceSQL;

@ReplaceSQL(fieldsStartWith = "SQL")
public class ReplaceSQLExample {

    public final String SQL1 = "sql1";
    final String SQL2 = "sql2";
    final String regularField = "regularField";
}
