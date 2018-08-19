package com.bts.essentials.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by wagan8r on 7/11/18.
 */
@Data
@AllArgsConstructor
public class BasicUser {
    private String email;
    private String firstName;
    private String lastName;

    public BasicUser() {
    }
}
