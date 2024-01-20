package service;

import yamert89.snoopy.meta.Filter;

public class TestFilter2 implements Filter {
    @Override
    public String apply(String sql) {
        return sql.replaceAll("= 1", "= ?");
    }
}
