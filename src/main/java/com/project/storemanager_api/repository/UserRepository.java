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

    // 중복 체크용 조회 메서드
    Optional<User> findById(Long userId);

    void updateName(String name, Long userId);

    void updatePassword(String password, Long userId);

    void deleteUser(Long userId);

    // refresh token 업데이트 (새로운 refresh token 저장)
    void updateRefreshToken(String refreshToken, Long userId);

}
