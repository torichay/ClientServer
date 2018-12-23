package reflection.classes.annotations;

import java.util.HashMap;
import java.util.Map;

public class DependenciesHolder {

    private static Map<Class, Object> objects = new HashMap<>();

    public static Map<Class, Object> getObjects() {
        return objects;
    }
}
