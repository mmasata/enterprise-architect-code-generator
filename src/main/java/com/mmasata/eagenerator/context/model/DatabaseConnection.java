package com.mmasata.eagenerator.context.model;

import lombok.Data;

/**
 * A framework generator context model that stores DB connection information.
 */
@Data
public class DatabaseConnection {

    private String url;
    private String user;
    private String password;

}
