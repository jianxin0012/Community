package community.community.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
@Mapper
public interface QuestionExtMapper {
    @Update("update question set view_count=view_count+1 where id=#{id}")
    void incView(@Param("id") Long id);

    @Update("update question set comment_count=comment_count+1 where id=#{id}")
    void incComment(@Param("id")Long id);

    @Update("update comment set comment_count=comment_count+1 where id=#{id}")
    void incSecComment(@Param("id")Long id);
}