package data;

import yamert89.snoopy.compile.Mapper;
import yamert89.snoopy.compile.ReplaceSql;

@ReplaceSql(fieldsStartWith = "SQL")
public class Simple {

    @Mapper(SMapper.class)
    public static final String SQL1 = "sql1";
    public static final String SQL2 = "sql2";
}
