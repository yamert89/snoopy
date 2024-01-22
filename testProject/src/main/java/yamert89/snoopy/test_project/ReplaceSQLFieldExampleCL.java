package yamert89.snoopy.test_project;

import yamert89.snoopy.meta.InjectSQLField;

public class ReplaceSQLFieldExampleCL {

    @InjectSQLField(name = "SQL2")
    public static final String SQL2 = "sql2";
    public static final String regularField = "regularField";
}
