package yamert89.snoopy.test_project;


import yamert89.snoopy.runtime.meta.Required;

public class SMapper {

    @Required
    Integer id;

    String name;

    public SMapper(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
