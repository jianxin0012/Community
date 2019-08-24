package community.community.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface NotificationExtMapper {
    @Update("update notification set status=1 where id=#{id}")
    void read(@Param("id") Long id);
}
