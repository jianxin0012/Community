package community.community.model;


public class User {
    private Integer id;
    private String accountId;
    private String name;
    private String token;
    private Long create_time;
    private Long modified_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public Long getModified_time() {
        return modified_time;
    }

    public void setModified_time(Long modified_time) {
        this.modified_time = modified_time;
    }
}
