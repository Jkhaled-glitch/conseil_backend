package com.api.dao.mysql;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.entities.mysql.Conseil;

@Repository
public interface ConseilDao extends JpaRepository<Conseil, Long> {

    Page<Conseil> findAllByOrderByIdDesc(Pageable pageable);


}


