package service;

import com.github.yamert89.snoopy.meta.Filter;

public class TestFilter implements Filter {
    @Override
    public String apply(String sql) {
        return sql.replaceAll("= [1,2]", "= ?");
    }
}
