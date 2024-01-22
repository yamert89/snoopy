package data;

import service.TestFilter;
import service.TestFilter2;
import yamert89.snoopy.meta.InjectSQL;
import yamert89.snoopy.meta.InjectSQLField;

@InjectSQL(fieldsStartWith = "SQL", filter = TestFilter2.class)
public class Filters {
    @InjectSQLField(name = "SQL6", filter = TestFilter.class)
    String SQL;

    String SQL2;

}
