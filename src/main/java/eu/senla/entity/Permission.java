package eu.senla.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    PERMISSION_READ("read"),
    PERMISSION_WRITE("write");

    public String getPermission() {
        return permission;
    }

    private final String permission;
}
