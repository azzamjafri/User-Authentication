package com.gpch.login.repository;
import com.gpch.login.model.Events;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EventRepository extends JpaRepository<Events, Integer>{
//     Events findById(@Param(value = "id")int userId);
}
