package yamert89.snoopy;

import yamert89.snoopy.compile.Required;

public class SMapper {

    @Required
    Integer id;

    String name;

    public SMapper(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
