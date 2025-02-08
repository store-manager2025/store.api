package com.project.storemanager_api.repository;

import com.project.storemanager_api.domain.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Mapper
@Repository
public interface UserRepository {

    // 회원 정보 생성
    void saveUser(User user);

    // 중복 체크용 조회 메서드
    Optional<User> findByEmail(String email);

}
