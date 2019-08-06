package community.community.mapper;

import community.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question(title,description,create_time,modified_time,creator,tag) " +
            "values (#{title},#{description}," +
            "#{create_time},#{modified_time},#{creator},#{tag})")
    public void create(Question question);
}
