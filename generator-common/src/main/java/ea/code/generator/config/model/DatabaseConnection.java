package ea.code.generator.config.model;

import lombok.Data;

@Data
public class DatabaseConnection {

    private String url;
    private String user;
    private String password;

}
