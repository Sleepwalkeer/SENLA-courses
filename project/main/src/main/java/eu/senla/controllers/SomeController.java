package eu.senla.controllers;

import eu.senla.annotations.Autowired;
import eu.senla.annotations.Component;
import eu.senla.services.SomeService;

@Component
public class SomeController {

    private final SomeService someService;

    @Autowired
    public SomeController(SomeService someService) {
        this.someService = someService;
    }

    public String execute() {
        return someService.execute();
    }
}