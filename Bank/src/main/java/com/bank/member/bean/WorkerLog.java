package com.bank.member.bean;


import jakarta.persistence.*;
import java.util.Date;

import org.springframework.stereotype.Component;

@Entity
@Table(name = "worker_log")
@Component
public class WorkerLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 自動遞增的 id
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)  // 外鍵對應 worker 表
    @JoinColumn(name = "worker_id", referencedColumnName = "w_id")
    private Worker worker;

    @Column(name = "action")
    private String action;

    @Column(name = "message")
    private String message;

    @Column(nullable = false,name = "time")
    @Temporal(TemporalType.TIMESTAMP)  // 紀錄時間戳
    private Date time;

    // 建構子
    public WorkerLog() {
    }

    public WorkerLog(Worker worker, String action, String message) {
        this.worker = worker;
        this.action = action;
        this.message = message;
        this.time = new Date();  // 默認時間為當前時間
    }

    // Getter 和 Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
