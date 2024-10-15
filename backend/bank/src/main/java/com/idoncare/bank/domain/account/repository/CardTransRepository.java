package com.idoncare.bank.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.idoncare.bank.domain.account.entity.CardTrans;

public interface CardTransRepository extends JpaRepository<CardTrans, Long> {
}
