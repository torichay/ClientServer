package reflection.classes;


import reflection.classes.annotations.InjectThisField;
import reflection.classes.annotations.SupportsInjection;

@SupportsInjection
public class SomeClass {

    @InjectThisField
    private SomeClassToInjection someClassToInjection;

    public void test() {
        try {
            System.out.println("class instance: " + someClassToInjection.toString());
        } catch (NullPointerException e) {
            System.out.println("There is not the class you are looking for!");
        }
    }

    public SomeClassToInjection getSomeClassToInjection() {
        return someClassToInjection;
    }
}
