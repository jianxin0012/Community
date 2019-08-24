package community.community.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CommentExtMapper {
    @Update("update comment set like_count=like_count+1 where id=#{id}")
    void incLikeCount(@Param("id") Long id);
}
