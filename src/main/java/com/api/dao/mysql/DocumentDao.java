package com.api.dao.mysql;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.api.entities.mysql.Document;

@Repository
public interface DocumentDao extends JpaRepository<Document, Long> {

}



