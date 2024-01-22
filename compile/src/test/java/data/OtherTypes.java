package data;

import yamert89.snoopy.meta.InjectSQL;
import yamert89.snoopy.meta.InjectSQLField;

import java.util.ArrayList;
import java.util.List;

@InjectSQL(fieldsStartWith = "SQL")
public class OtherTypes {
    private List<String> list;
    private Object SQL3;
    @InjectSQLField(name = "SQL1")
    private String target;
    @InjectSQLField(name = "SQL2")
    private int intVal;
    private long SQL;
    @InjectSQLField(name = "SQL2")
    private String SQL1;

    public OtherTypes() {
        this.list = new ArrayList<>();
        this.SQL3 = new Object();
        this.target = "target";
        this.intVal = 0;
        this.SQL = 0L;
    }
}
