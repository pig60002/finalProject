package com.bank.member.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bank.member.bean.WorkerLog;

public interface WorkerLogRepository extends JpaRepository<WorkerLog, Integer> {
	
	@Query("SELECT wl FROM WorkerLog wl WHERE wl.worker.wId = :workerId")
	List<WorkerLog> findByWorker_Id(@Param("workerId") Integer workerId);
	
    @Query("SELECT wl FROM WorkerLog wl " +
            "JOIN wl.worker w " +
            "WHERE (:action IS NULL OR wl.action LIKE %:action%) " +
            "AND (:account IS NULL OR w.wAccount LIKE %:account%)")
     Page<WorkerLog> findByActionAndWorkerAccount(
         @Param("action") String action,
         @Param("account") String account,
         Pageable pageable
     );
    
    @Query("SELECT wl FROM WorkerLog wl WHERE wl.worker.wId = :workerId ORDER BY wl.time DESC")
    List<WorkerLog> findLogsByWorkerWIdOrderByTimeDesc(@Param("workerId") Integer workerId);
}
