package reflection.classes.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class AnnotationProcessor {

    String classpath;

    public AnnotationProcessor(String classpath) {
        this.classpath = classpath;
    }

    public void injectDependencies(Object processedObject) {
        if (!isSupportInjection(processedObject)) {
            return;
        }

        Field[] declaredFields = processedObject.getClass().getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (int j = 0; j < annotations.length; j++) {
                Annotation annotation = annotations[j];
                if (annotation.annotationType().equals(InjectThisField.class)) {
                    injectDependenciesIntoField(field, processedObject);
                }
            }
        }
    }

    private boolean isSupportInjection(Object processedObject) {
        Class<?> aClass = processedObject.getClass();
        Annotation[] annotations = aClass.getAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            Annotation annotation = annotations[i];
            if (annotation.annotationType().equals(SupportsInjection.class)) {
                return true;
            }
        }
        return false;
    }

    private void injectDependenciesIntoField(Field field, Object processedObject) {
        Class fieldClass = field.getType();

        Object objectFromHolder = DependenciesHolder.getObjects().get(fieldClass);
        if (objectFromHolder == null) {
            try {
                Object newInstance = createNewInstance(fieldClass);
                DependenciesHolder.getObjects().put(fieldClass, newInstance);
                objectFromHolder = newInstance;
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }

        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            field.set(processedObject, objectFromHolder);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        field.setAccessible(accessible);
    }

    private Object createNewInstance(Class fieldClass) throws ReflectiveOperationException {
        Constructor constructor = fieldClass.getConstructors()[0];
        return constructor.newInstance();
    }
}