package yamert89.snoopy.test_project;

import yamert89.snoopy.compile.meta.Mapper;

public class MapperExampleCL {

    @Mapper(SMapper.class)
    public static final String SQL1 = "sql1";
    public static final String regularField = "regularField";
}
