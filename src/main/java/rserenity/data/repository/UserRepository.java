package rserenity.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rserenity.data.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    public UserEntity findUserEntityByUsername(String username);
}
