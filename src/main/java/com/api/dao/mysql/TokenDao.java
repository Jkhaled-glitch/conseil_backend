package com.api.dao.mysql;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.api.entities.mysql.Token;
import com.api.entities.mysql.User;


public interface TokenDao extends JpaRepository<Token, Long> {
  @Query(value = "SELECT COUNT(t) FROM Token t WHERE t.user = :user")
  Integer countByUser(@Param("user") User user);




  @Query(value = """
    select t from Token t inner join User u
    on t.user.username = u.username
    where u.username = :id and (t.expired = false or t.revoked = false)
    """)
List<Token> findAllValidTokenByUser(@Param("id") String id);





  @Query(value = """
      select t from Token t inner join User u
      on t.user.username = u.username
      where u.username = :id
      """)
  List<Token> findAllByUserId(@Param("id") String id);



  @Transactional
  @Modifying
  @Query(value = "DELETE FROM Token t WHERE t.user.username = :userId")
  void deleteAllTokensByUserId(@Param("userId") String userId);


  Optional<Token> findByToken(String token);
}
