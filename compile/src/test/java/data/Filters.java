package data;

import com.github.yamert89.snoopy.meta.InjectSQL;
import com.github.yamert89.snoopy.meta.InjectSQLField;
import service.TestFilter;
import service.TestFilter2;

@InjectSQL(fieldsStartWith = "SQL", filter = TestFilter2.class)
public class Filters {
    @InjectSQLField(name = "SQL6", filter = TestFilter.class)
    String SQL;

    String SQL2;

}
