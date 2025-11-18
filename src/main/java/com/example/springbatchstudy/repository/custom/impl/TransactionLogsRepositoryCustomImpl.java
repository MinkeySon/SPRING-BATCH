package com.example.springbatchstudy.repository.custom.impl;

import com.example.springbatchstudy.domain.DirectionEnum;
import com.example.springbatchstudy.domain.QTransactionLogs;
import com.example.springbatchstudy.domain.TransactionLogs;
import com.example.springbatchstudy.repository.custom.TransactionLogsRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class TransactionLogsRepositoryCustomImpl implements TransactionLogsRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<TransactionLogs> findByDirection(DirectionEnum direction, Pageable pageable) {

        QTransactionLogs qTransactionLogs = QTransactionLogs.transactionLogs;

        List<TransactionLogs> content = jpaQueryFactory.select(qTransactionLogs)
                .from(qTransactionLogs)
                .where(qTransactionLogs.direction.eq(direction))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory.select(qTransactionLogs.count())
                .from(qTransactionLogs)
                .where(qTransactionLogs.direction.eq(direction))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<TransactionLogs> findByCreatedDate(LocalDateTime currentDate, Pageable pageable) {

        QTransactionLogs qTransactionLogs = QTransactionLogs.transactionLogs;

        List<TransactionLogs> content = jpaQueryFactory.select(qTransactionLogs)
                .from(qTransactionLogs)
                .where(qTransactionLogs.createdAt.before(currentDate))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory.select(qTransactionLogs.count())
                .from(qTransactionLogs)
                .where(qTransactionLogs.createdAt.before(currentDate))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
