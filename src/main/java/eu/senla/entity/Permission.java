package eu.senla.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

public enum Permission {
    PERMISSION_READ("read"),
    PERMISSION_WRITE("write");

    private final String permission;

    public String getPermission() {
        return permission;
    }
}
