package com.cigi.pickthem.repositories;

import com.cigi.pickthem.domain.entities.UserEntity;
import com.cigi.pickthem.domain.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    List<UserEntity> findTop3ByOrderByTotalPointsDesc();

    List<UserEntity> findByRoleOrderByTotalPointsDesc(Role role);

}
