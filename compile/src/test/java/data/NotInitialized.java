package data;

import yamert89.snoopy.meta.ReplaceSQL;

@ReplaceSQL(fieldsStartWith = "SQL")
public class NotInitialized {
    private String SQL1;
    private String SQL2 = "sql2";
    private String SQL3;
    private final String regularField = "regularField";
    private final String SQL4 = "sql4";
    private String SQL5;

}
