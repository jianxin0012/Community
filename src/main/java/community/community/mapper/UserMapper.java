package community.community.mapper;

import community.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    @Insert("insert into user (account_id,name,token,create_time,modified_time) values(#{accountId},#{name},#{token},#{create_time},#{modified_time})")
    void insert(User user);
}
