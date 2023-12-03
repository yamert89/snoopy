package yamert89.snoopy.test_project;

import yamert89.snoopy.meta.ReplaceSQL;

@ReplaceSQL(fieldsStartWith = "inject")
public class ReplaceSQLExample2CL {

    public static final String SQL1 = "sql1";
    public static final String inject2 = "sql2";
    public static final String regularField = "regularField";
}
