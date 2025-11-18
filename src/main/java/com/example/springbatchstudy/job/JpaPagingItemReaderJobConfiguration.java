package com.example.springbatchstudy.job;

import com.example.springbatchstudy.domain.DirectionEnum;
import com.example.springbatchstudy.domain.TransactionLogs;
import com.example.springbatchstudy.repository.TransactionLogsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class JpaPagingItemReaderJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final TransactionLogsRepository transactionLogsRepository;
    private final StepBuilderFactory stepBuilderFactory;

    private int chunkSize = 10;

    @Bean
    public Job jpaPagingItemReaderJob() {
        return jobBuilderFactory.get("jpaPagingItemReaderJob")
                .start(jpaPagingItemReaderStep())
                .build();
    }

    @Bean
    public Step jpaPagingItemReaderStep(){
        return stepBuilderFactory.get("jpaPagingItemReaderStep")

                /**
                 * <ItemReader 반환 데이터 타입, ItemWriter 전달받을 데이터 타입>처리할 청크
                 */
                .<TransactionLogs, TransactionLogs>chunk(chunkSize)
                .reader(transactionLogsReader())
                .writer(writer())
                .build();
    }

    @Bean
    public RepositoryItemReader<TransactionLogs> transactionLogsReader(){
        return new RepositoryItemReaderBuilder<TransactionLogs>()
                .name("transactionLogsReader")
                .repository(transactionLogsRepository)
                /**
                 * 실제 커스텀 메서드 명
                 */
                .methodName("findByDirection")
                .arguments(Arrays.asList(DirectionEnum.RECEIVE))
                .pageSize(chunkSize)
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemWriter<TransactionLogs> writer() {
        return items -> {
            log.info(">>>> Chunk of {} items", items.size());
            for (TransactionLogs item : items) {
                log.info("Read item: {}", item.getId());
            }
        };
    }
}
