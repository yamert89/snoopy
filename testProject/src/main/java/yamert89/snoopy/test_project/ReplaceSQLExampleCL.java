package yamert89.snoopy.test_project;

import yamert89.snoopy.meta.ReplaceSQL;

@ReplaceSQL(fieldsStartWith = "SQL")
public class ReplaceSQLExampleCL {

    public final String SQL1 = "sql1";
    public String SQL2 = "sql2";
    private String SQL3;
    public final String regularField = "regularField";
}
