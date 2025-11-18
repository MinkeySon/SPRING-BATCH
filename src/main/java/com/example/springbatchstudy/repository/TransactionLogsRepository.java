package com.example.springbatchstudy.repository;

import com.example.springbatchstudy.domain.TransactionLogs;
import com.example.springbatchstudy.repository.custom.TransactionLogsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionLogsRepository extends JpaRepository<TransactionLogs, Long>, TransactionLogsRepositoryCustom {
}
