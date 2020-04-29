package com.bts.essentials.model;

import java.util.List;

public interface Role {
    List<Permission> getPermissions();
}
