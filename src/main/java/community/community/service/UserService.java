package community.community.service;

import community.community.mapper.UserMapper;
import community.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;


    public void createOrUpdate(User user) {
        User DBUser=userMapper.findByAccountId(user.getAccountId());
        if (DBUser==null){
            //插入
            user.setCreateTime(System.currentTimeMillis());
            user.setModifiedTime(user.getCreateTime());
            userMapper.insert(user);
        }else {
            //更新
            DBUser.setName(user.getName());
            DBUser.setToken(user.getToken());
            DBUser.setAvatar(user.getAvatar());
            DBUser.setModifiedTime(System.currentTimeMillis());
            userMapper.update(user);
        }
    }
}
