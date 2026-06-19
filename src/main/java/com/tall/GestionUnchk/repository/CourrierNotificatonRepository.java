package com.tall.GestionUnchk.repository;

import com.tall.GestionUnchk.entity.CourrierNotificaton;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourrierNotificatonRepository extends JpaRepository<CourrierNotificaton,Long> {

    List<CourrierNotificaton> findByUserId(Long userId);

    Long countByUserIdAndLuFalse(Long userId);
}
