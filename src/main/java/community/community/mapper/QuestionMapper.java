package community.community.mapper;

import community.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question(title,description,create_time,modified_time,creator,tag) " +
            "values (#{title},#{description}," +
            "#{createTime},#{modifiedTime},#{creator},#{tag})")
    public void create(Question question);

    @Select("select * from question limit #{offset},#{size}")
    List<Question> list(@Param("offset") Integer offset, @Param("size")Integer size);

    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator=#{userId} limit #{offset},#{size}")
    List<Question> listByUserId(@Param("userId") Integer userId, @Param("offset") Integer offset, @Param("size")Integer size);

    @Select("select count(1) from question where creator=#{userId}")
    Integer countByUserId(@Param("userId") Integer userId);

    @Select("select * from question where id=#{id}")
    Question getById(@Param("id") Integer id);

    @Update("update question set title=#{title},description=#{description},modified_time=#{modifiedTime},tag=#{tag} where id=#{id}")
    void update(Question question);
}
