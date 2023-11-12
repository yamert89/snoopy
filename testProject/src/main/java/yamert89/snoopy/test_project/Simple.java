package yamert89.snoopy.test_project;

import yamert89.snoopy.compile.meta.Mapper;
import yamert89.snoopy.compile.meta.ReplaceSql;

@ReplaceSql(fieldsStartWith = "SQL")
public class Simple {

    @Mapper(SMapper.class)
    public static final String SQL1 = "sql1";
    public static final String SQL2 = "sql2";
}
