package data;

import service.TestFilter;
import service.TestFilter2;
import yamert89.snoopy.meta.ReplaceSQL;
import yamert89.snoopy.meta.ReplaceSQLField;

@ReplaceSQL(fieldsStartWith = "SQL", filter = TestFilter2.class)
public class Filters {
    @ReplaceSQLField(name = "SQL6", filter = TestFilter.class)
    String SQL;

    String SQL2;

}
