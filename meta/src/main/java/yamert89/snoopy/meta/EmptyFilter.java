package yamert89.snoopy.meta;

public class EmptyFilter implements Filter {
    @Override
    public String apply(String sql) {
        return sql;
    }
}
