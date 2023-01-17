package eu.senla;


import eu.senla.controllers.SomeController;

public class Application {
    public static void main(String[] args) throws Exception {
        var context = new AnnotationConfigApplicationContext(Application.class);
        SomeController bean = context.getBean(SomeController.class);
        System.out.println(bean.execute());
    }
}
