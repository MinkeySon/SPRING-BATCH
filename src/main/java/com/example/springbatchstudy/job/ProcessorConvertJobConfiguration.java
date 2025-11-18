package com.example.springbatchstudy.job;

import com.example.springbatchstudy.domain.TransactionLogs;
import com.example.springbatchstudy.repository.TransactionLogsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ProcessorConvertJobConfiguration {
    public static final String JOB_NAME = "ProcessorConvertJob";
    public static final String BEAN_PREFIX = JOB_NAME + "_";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final TransactionLogsRepository transactionLogsRepository;

    @Value("${chunkSize:50}")
    private int chunkSize;

    @Bean(JOB_NAME)
    public Job job(){
        return jobBuilderFactory.get(JOB_NAME)
                .preventRestart()
                .start(step())
                .build();
    }

    @Bean(BEAN_PREFIX + "step")
    @JobScope
    public Step step(){
        return stepBuilderFactory.get(BEAN_PREFIX + "step")
                .<TransactionLogs, String>chunk(chunkSize)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public RepositoryItemReader<TransactionLogs> reader(){
        return new RepositoryItemReaderBuilder<TransactionLogs>()
                .name(BEAN_PREFIX + "reader")
                .repository(transactionLogsRepository)
                .methodName("findByCreatedDate")
                .arguments(Arrays.asList(LocalDateTime.now()))
                .pageSize(chunkSize)
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemProcessor<TransactionLogs, String> processor(){
        return transactionLogs -> {
            return transactionLogs.getFilePath();
        };
    }

    @Bean
    public ItemWriter<String> writer(){
        return items ->{
            for (String item : items) {
                log.info(">>>>>>>> item file path : {}", item);
            }
        };
    }
}
