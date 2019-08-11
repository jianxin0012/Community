package community.community.service;

import community.community.mapper.UserMapper;
import community.community.model.User;
import community.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;


    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size()==0){
            //插入
            user.setCreateTime(System.currentTimeMillis());
            user.setModifiedTime(user.getCreateTime());
            userMapper.insert(user);
        }else {
            //更新
            User DBUser=users.get(0);
            User updateUser=new User();
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            updateUser.setAvatar(user.getAvatar());
            updateUser.setModifiedTime(System.currentTimeMillis());
            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(DBUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
        }
    }
}
