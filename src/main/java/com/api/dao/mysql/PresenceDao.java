package com.api.dao.mysql;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.api.entities.mysql.Presence;

@Repository
public interface PresenceDao extends JpaRepository<Presence, Long> {


}
