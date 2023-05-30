package com.mmasata.eagenerator.service.freemarker.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PomRepository {

    private String type;
    private String name;
    private String id;
    private String url;
}
