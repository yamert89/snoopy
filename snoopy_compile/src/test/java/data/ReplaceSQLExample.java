package data;

import yamert89.snoopy.compile.meta.ReplaceSQL;

@ReplaceSQL(fieldsStartWith = "SQL")
public class ReplaceSQLExample {

    public String SQL1 = "sql1";
    public String SQL2 = "sql2";
    public String regularField = "regularField";
}
