package eu.senla;

import eu.senla.annotations.*;
import org.reflections.Reflections;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

public class BeanController {
    private final Queue<Class<?>> componentClassQueue = new LinkedList<>();
    private final Set<Class<?>> componentClassSet = new HashSet<>();
    private final Map<Class<?>, Object> objectMap = new HashMap<>();

    public Map<Class<?>, Object> getObjectMap() {
        return objectMap;
    }


    public void createIocContainer(Reflections reflections) throws Exception {
        setComponentClasses(reflections);
        instantiateBeans(reflections);
    }

    public void setComponentClasses(Reflections reflections) {
        Set<Class<?>> beans = reflections.getTypesAnnotatedWith(Component.class);
        componentClassQueue.addAll(beans);
        componentClassSet.addAll(beans);
    }

    private Object createInstance(Constructor<?> constructor) throws
            InvocationTargetException, InstantiationException, IllegalAccessException {
        return constructor.newInstance();
    }

    public void instantiateBeans(Reflections reflections) throws
            InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, IOException {
        int iterations = 0;
        while (!componentClassQueue.isEmpty() && iterations < 50) {
            iterations++;
            Class<?> klass = componentClassQueue.poll();


            Class<?> classType;
            Object newInstance = null;

            Constructor<?> annotatedConstructor = Finder.findAnnotatedConstructor(klass);
            if (annotatedConstructor != null) {
                classType = annotatedConstructor.getParameterTypes()[0];
                if (beanExists(classType, reflections)) {
                    newInstance = annotatedConstructor.newInstance(objectMap.get(getImplementationClass(classType, reflections)));
                } else {
                    componentClassQueue.add(klass);
                }
            }

            Method annotatedMethod = Finder.findAnnotatedMethod(klass);
            if (annotatedMethod != null) {
                classType = annotatedMethod.getParameterTypes()[0];
                if (beanExists(classType, reflections)) {
                    newInstance = createInstance(klass.getConstructor());
                    annotatedMethod.invoke(newInstance, objectMap.get(getImplementationClass(classType, reflections)));
                } else {
                    componentClassQueue.add(klass);
                }

            }

            Field autowiredAnnotatedField = Finder.findAnnotatedField(klass);
            if (autowiredAnnotatedField != null) {
                classType = autowiredAnnotatedField.getType();
                if (beanExists(classType, reflections)) {
                    newInstance = createInstance(klass.getConstructor());
                    autowiredAnnotatedField.set(newInstance, objectMap.get(getImplementationClass(classType, reflections)));
                } else {
                    componentClassQueue.add(klass);
                }
            }

            Field valueAnnotatedField = Finder.findValueAnnotatedField(klass);
            if (valueAnnotatedField != null) {
                newInstance = createInstance(klass.getConstructor());
                valueAnnotationHandler(newInstance, valueAnnotatedField);
            }

            if (noAnnotationsFound(annotatedConstructor, annotatedMethod, autowiredAnnotatedField, valueAnnotatedField)) {
                newInstance = createInstance(klass.getConstructor());
            }

            if (newInstance != null) {
                objectMap.put(klass, newInstance);
            }
        }

        if (iterations == 50) {
            throw new InstantiationException("Beans cannot be instantiated");
        }
    }

    private boolean noAnnotationsFound(Constructor<?> constructor, Method method, Field autowiredField, Field valueField) {
        return (constructor != null && method != null && autowiredField != null && valueField != null);
    }

    private boolean beanExists(Class<?> klass, Reflections reflections) {
        if (objectMap.containsKey(klass)) {
            return true;
        } else {
            return reflections.getSubTypesOf(klass).stream().anyMatch(objectMap::containsKey);
        }
    }

    private Class<?> getImplementationClass(Class<?> classType, Reflections reflections) throws InstantiationException {
        List<Class<?>> implementations = new ArrayList<>();
        if (objectMap.containsKey(classType)) {
            return classType;
        } else {
            for (Class<?> aClass : reflections.getSubTypesOf(classType)) {
                if (componentClassSet.contains(aClass)) {
                    implementations.add(aClass);
                }
            }
            return switch (implementations.size()) {
                case 0 -> throw new InstantiationException("No beans were found");
                case 1 -> (implementations.get(0));
                default -> throw new InstantiationException("There are more than one implementation available");
            };
        }
    }

    private void valueAnnotationHandler(Object object, Field field) throws IOException, IllegalAccessException {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "application.properties";

        Properties appProps = new Properties();
        appProps.load(new FileInputStream(appConfigPath));
        Value annotation = field.getAnnotation(Value.class);
        String annotationValue = annotation.value();
        if (annotationValue.contains("${") && annotationValue.contains("}")) {
            annotationValue = annotationValue.replaceAll("[{}$]", "");
            String propertyValue = appProps.getProperty(annotationValue);
            field.set(object, propertyValue);
        } else {
            field.set(object, annotationValue);
        }
    }
}
