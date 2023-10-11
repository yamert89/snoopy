package yamert89.snoopy;

import yamert89.snoopy.compile.ClassPatcherImpl;
import yamert89.snoopy.compile.ClassScanner;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        new ClassScanner("yamert89/snoopy", "c:/PetProjects/snoopy/snoopy_compile/build/classes/java/main/")
                .scanAndHandle();

    }

    public static void f(){
        String sql1 = "";
        /*
          mapper = new SMapper(1, "name1")
        * buildSql(Simple.SQL, mapper)
        *
        * */
    }
}
