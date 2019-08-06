package community.community.mapper;

import community.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question(title,description,createTime,modifiedTime,creator,tag) " +
            "values (#{title},#{description}," +
            "#{createTime},#{modifiedTime},#{creator},#{tag})")
    public void create(Question question);

    @Select("select * from question")
    List<Question> list();
}
