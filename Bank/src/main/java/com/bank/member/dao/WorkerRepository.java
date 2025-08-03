package com.bank.member.dao;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.member.bean.Worker;



public interface WorkerRepository extends JpaRepository<Worker, Integer> {
	Optional<Worker> findBywAccount(String wAccount);
}
