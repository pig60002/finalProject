package com.bank.member.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bank.member.bean.WorkerLog;

public interface WorkerLogRepository extends JpaRepository<WorkerLog, Integer> {
	
	@Query("SELECT wl FROM WorkerLog wl WHERE wl.worker.wId = :workerId")
	List<WorkerLog> findByWorker_Id(@Param("workerId") Integer workerId);
}
