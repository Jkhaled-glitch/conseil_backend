package com.api.dao.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.entities.mysql.Point;

@Repository
public interface PointDao extends JpaRepository<Point, Long> {

   


}
