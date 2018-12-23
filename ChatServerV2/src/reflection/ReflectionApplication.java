package reflection;

import reflection.classes.SomeClass;
import reflection.classes.annotations.AnnotationProcessor2;

public class ReflectionApplication {

    public static void main(String[] args) {
        // --- wait for smart NPE message
        SomeClass someClass = new SomeClass();
        someClass.test();

        // --- use annotation processor
        AnnotationProcessor2 annotationProcessor =
                new AnnotationProcessor2(
                        "reflection/classes");
        annotationProcessor.injectDependencies(someClass);

        // --- test after magic is done
        someClass.test();
    }
}
