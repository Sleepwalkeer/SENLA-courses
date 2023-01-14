package eu.senla.services;

import eu.senla.annotations.Autowired;
import eu.senla.annotations.Component;
import eu.senla.dao.SomeDao;

@Component
public class SomeServiceImpl implements SomeService {

    private SomeDao someDao;

    @Autowired
    private void setSomeDao(SomeDao someDao) {
        this.someDao = someDao;
    }

    @Override
    public String execute() {
        return someDao.execute();
    }
}
