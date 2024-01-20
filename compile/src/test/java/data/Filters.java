package data;

import service.TestFilter;
import yamert89.snoopy.meta.ReplaceSQLField;

public class Filters {
    @ReplaceSQLField(name = "SQL6", filter = TestFilter.class)
    String SQL;

}
