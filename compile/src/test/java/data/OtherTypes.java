package data;

import yamert89.snoopy.meta.ReplaceSQL;
import yamert89.snoopy.meta.ReplaceSQLField;

import java.util.ArrayList;
import java.util.List;

@ReplaceSQL(fieldsStartWith = "SQL")
public class OtherTypes {
    private List<String> list;
    private Object SQL3;
    @ReplaceSQLField(name = "SQL1")
    private String target;
    @ReplaceSQLField(name = "SQL2")
    private int intVal;
    private long SQL;
    @ReplaceSQLField(name = "SQL2")
    private String SQL1;

    public OtherTypes() {
        this.list = new ArrayList<>();
        this.SQL3 = new Object();
        this.target = "target";
        this.intVal = 0;
        this.SQL = 0L;
    }
}
