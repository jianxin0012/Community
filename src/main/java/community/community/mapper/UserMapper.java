package community.community.mapper;

import community.community.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("insert into user (account_id,name,token,create_time,modified_time,avatar) values(#{accountId},#{name},#{token},#{createTime},#{modifiedTime},#{avatar})")
    void insert(User user);
    @Select("select * from user where token=#{token}")
    User findByToken(@Param("token") String token);

    @Select("select * from user where id=#{id}")
    User findById(@Param("id") Integer id);

    @Select("select * from user where account_id=#{accountId}")
    User findByAccountId(@Param("accountId")String accountId);

    @Update("update user set name=#{name},token=#{token},modified_time=#{modifiedTime},avatar=#{avatar} where account_id=#{accountId}")
    void update(User user);
}
