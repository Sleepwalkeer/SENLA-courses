package eu.senla;

import org.reflections.Reflections;

public class AnnotationConfigApplicationContext {
    BeanController beanController = new BeanController();

    AnnotationConfigApplicationContext(Class<?> klass) throws Exception {
        initializeContext(klass);
    }
    public <T> T getBean(Class<T> klass) {
        return (T) beanController.getObjectMap().get(klass);
    }

    private void initializeContext(Class<?> klass) throws Exception {
        beanController.createIocContainer(new Reflections(klass.getPackageName()));
    }
}

