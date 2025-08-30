package com.bank.fund.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 排程配置類
 * 啟用 Spring 的排程功能
 */
@Configuration
@EnableScheduling
public class SchedulerConfig {
    
    // 這個配置類目前只需要 @EnableScheduling 註解
    // 如果需要自定義執行緒池或其他排程設定，可以在這裡添加
    
}