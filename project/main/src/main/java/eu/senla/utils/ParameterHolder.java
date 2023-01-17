package eu.senla.utils;

import eu.senla.annotations.Component;
import eu.senla.annotations.Value;

@Component
public class ParameterHolder {

    @Value("${my.param.db}")
    private String someValue;

    public String getSomeValue() {
        return someValue;
    }
}
