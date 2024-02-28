package kdt_library.library.service;

import kdt_library.library.mapper.UserMapper;
import kdt_library.library.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<UserVo> getUserList() {
        return userMapper.getUserList();
    }

    public UserVo getUserById(String id) {
        return userMapper.getUserById(id);
    }


    public void signup(UserVo userVo) { // 회원 가입

        // password는 암호화해서 DB에 저장
        userVo.setPw(passwordEncoder.encode(userVo.getPw()));
        userMapper.insertUser(userVo);

    }

    public void edit(UserVo userVo) { // 회원 정보 수정
        // password는 암호화해서 DB에 저장
        userVo.setPw(passwordEncoder.encode(userVo.getPw()));
        userMapper.updateUser(userVo);
    }
    public void withdraw(String id) { // 회원 탈퇴
        userMapper.deleteUser(id);
    }

    public PasswordEncoder passwordEncoder() {
        return this.passwordEncoder;
    }
}