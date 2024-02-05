package data;

import com.github.yamert89.snoopy.meta.InjectSQLField;

public class ReplaceSQLFieldExample {

    @InjectSQLField(name = "SQL2")
    public final String SQL = "sql2";
    public static final String regularField = "regularField";
}
