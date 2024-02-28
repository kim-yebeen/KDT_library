package kdt_library.library.mapper;


import kdt_library.library.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<UserVo> getUserList(); // User 테이블 가져오기
    void insertUser(UserVo userVo); // 회원 가입

    UserVo getUserById(String id);
    void updateUser(UserVo userVo); // 회원 정보 수정
    void deleteUser(String id); // 회원 탈퇴
}