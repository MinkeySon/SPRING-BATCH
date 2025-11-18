package com.example.springbatchstudy.repository.custom;

import com.example.springbatchstudy.domain.DirectionEnum;
import com.example.springbatchstudy.domain.TransactionLogs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionLogsRepositoryCustom {

    Page<TransactionLogs> findByDirection(DirectionEnum direction, Pageable pageable);

    Page<TransactionLogs> findByCreatedDate(LocalDateTime currentDate, Pageable pageable);

}
