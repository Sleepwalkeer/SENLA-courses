package eu.senla.testContainer;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer> {
    private static final String DOCKER_IMAGE = "postgres:latest";
    private static final String DATABASE_NAME = "TestDb";
    private static PostgresTestContainer container;
    private PostgresTestContainer() {
        super(DOCKER_IMAGE);
    }

    public static PostgresTestContainer getInstance() {
        if (container == null) {
            container = new PostgresTestContainer()
                    .withDatabaseName(DATABASE_NAME);
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("db_url", container.getJdbcUrl());
        System.setProperty("db_username", container.getUsername());
        System.setProperty("db_password", container.getPassword());
    }

    @Override
    public void stop() {

    }
}