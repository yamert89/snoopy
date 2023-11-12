package data;

import yamert89.snoopy.compile.meta.Mapper;

public class MapperExample {

    @Mapper(SMapper.class)
    public static final String SQL1 = "sql1";
    public static final String regularField = "regularField";
}
