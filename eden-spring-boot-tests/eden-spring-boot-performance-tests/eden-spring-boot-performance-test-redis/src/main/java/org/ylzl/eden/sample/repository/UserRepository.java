package org.ylzl.eden.sample.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.ylzl.eden.sample.domain.User;
import org.ylzl.eden.spring.boot.data.redis.repository.RedisRepository;

import java.util.Date;
import java.util.List;

/**
 * 用户数据仓库
 *
 * @author gyl
 * @since 0.0.1
 */
@Repository
public interface UserRepository extends RedisRepository<User, Long> {

  List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Date dateTime);

  Page<User> findAllByLoginNot(Pageable pageable, String login);

  User findOneByActivationKey(String activationKey);

  User findOneByEmailIgnoreCase(String email);

  User findOneByLogin(String login);

  User findOneByResetKey(String resetKey);
}
