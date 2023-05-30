package com.mmasata.eagenerator.context.model;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class Pom {

    private String artifactId;
    private String groupId;
    private String name;

    private Integer javaVersion;

    private List<PomRepository> distributionManagement = Collections.emptyList();
    private List<PomRepository> repositories = Collections.emptyList();
    private List<PomRepository> pluginRepositories = Collections.emptyList();
}
