package reflection.classes.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class AnnotationProcessor2 {

    String classpath;

    public AnnotationProcessor2(String classpath) {
        this.classpath = classpath;
    }

    public void injectDependencies(Object processedObject) {
        if (!isSupportInjection(processedObject)) {
            return;
        }
        processInjecting(processedObject);
    }

    private boolean isSupportInjection(Object processedObject) {
        Annotation[] annotations = processedObject.getClass().getAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            Annotation annotation = annotations[i];
            if (annotation.annotationType().equals(SupportsInjection.class)) {
                return true;
            }
        }
        return false;
    }

    private void processInjecting(Object processedObject) {
        Field[] declaredFields = processedObject.getClass().getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];
            boolean injectionNeeded = isInjectionNeeded(field);
            if (injectionNeeded) {
                injectDependenciesIntoField(field, processedObject);
            }
        }
    }

    private boolean isInjectionNeeded(Field field) {
        Annotation[] annotations = field.getDeclaredAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            Annotation annotation = annotations[i];
            if (annotation.annotationType().equals(InjectThisField.class)) {
                return true;
            }
        }
        return false;
    }

    private void injectDependenciesIntoField(Field field, Object processedObject) {
        Class<?> type = field.getType();

        Object existedInstance = DependenciesHolder.getObjects().get(type);
        if (existedInstance == null) {
            Object instance = createInstance(type);
            DependenciesHolder.getObjects().put(type, instance);
            existedInstance = instance;
        }

        try {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            field.set(processedObject, existedInstance);
            field.setAccessible(accessible);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Object createInstance(Class<?> type) {
        try {
            return type.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}