<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>
    <groupId>${groupId}</groupId>
    <artifactId>${artifactId}</artifactId>
    <version>${version}</version>
    <name>${name}</name>

    <properties>
        <java.version>${javaVersion}</java.version>
        <maven.compiler.source>${javaVersion}</maven.compiler.source>
        <maven.compiler.target>${javaVersion}</maven.compiler.target>
    </properties>


    <dependencies>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.26</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
            <version>6.0.8</version>
        </dependency>

        <dependency>
            <groupId>io.projectreactor</groupId>
            <artifactId>reactor-core</artifactId>
            <version>3.5.0</version>
        </dependency>

    </dependencies>

    <distributionManagement>
        <#list distributionManagement as distMng>
            <${distMng.type}>
                <id>${distMng.id}</id>
                <url>${distMng.url}</url>
            </${distMng.type}>
        </#list>
    </distributionManagement>

    <repositories>
        <#list repositories as repo>
            <repository>
            <id>${repo.id}</id>
            <name>${repo.name}</name>
            <url>${repo.url}</url>
            </repository>
        </#list>
    </repositories>


    <pluginRepositories>
        <#list pluginRepositories as pluginRepo>
            <pluginRepository>
                <id>${pluginRepo.id}</id>
                <name>${pluginRepo.name}</name>
                <url>${pluginRepo.url}</url>
            </pluginRepository>
        </#list>
    </pluginRepositories>

</project>