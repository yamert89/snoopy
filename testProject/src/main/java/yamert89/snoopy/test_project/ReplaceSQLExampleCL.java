package yamert89.snoopy.test_project;

import yamert89.snoopy.compile.meta.ReplaceSql;

@ReplaceSql(fieldsStartWith = "SQL")
public class ReplaceSQLExampleCL {

    public static final String SQL1 = "sql1";
    public static final String SQL2 = "sql2";
    public static final String regularField = "regularField";
}
