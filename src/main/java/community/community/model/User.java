package community.community.model;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String accountId;
    private String name;
    private String token;
    private Long create_time;
    private Long modified_time;
    private String avatar;
}
