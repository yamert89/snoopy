package yamert89.snoopy.test_project;

import yamert89.snoopy.meta.InjectSQL;

@InjectSQL(fieldsStartWith = "rep")
public class Cl2 {
    private final String rep1 = "init";

    public String getRep1() {
        return rep1;
    }
}
