package eu.senla.dao;

import eu.senla.annotations.Autowired;
import eu.senla.annotations.Component;
import eu.senla.utils.ParameterHolder;

@Component
public class SomeDaoImpl implements SomeDao{
    @Autowired
    private ParameterHolder parameterHolder;

    @Override
    public String execute() {
        return parameterHolder.getSomeValue();
    }
}