package com.example.springbatchstudy.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StepNextConditionalJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job stepNextConditionalJob() {
        return jobBuilderFactory.get("stepNextConditionalJob")
                .start(conditionalJobsStep1())

                    // step1 에서 fail 일 경우 step 3 실행 이후 결과에 상관없이 종료
                    .on("FAILED")
                    .to(conditionalJobsStep3())
                    .on("*")
                    .end()

                    // step1 에서 fail 이 아닐 경우 step 2 실행 이후 결과에 상관없이 종료
                .from(conditionalJobsStep1())
                    .on("*")
                    .to(conditionalJobsStep2())
                    .next(conditionalJobsStep3())
                    .on("*")
                    .end()
                .end()
                .build();
    }

    @Bean
    public Step conditionalJobsStep1(){
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>>> This is stepNextConditionalJob Step1");

                    /**
                     * ExitStatus 를 FAILED 로 지정
                     * 해당 status 를 보고 flow 진행
                     */
                    contribution.setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step conditionalJobsStep2(){
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>>> This is stepNextConditionalJob Step2");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step conditionalJobsStep3(){
        return stepBuilderFactory.get("step3")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>>> This is stepNextConditionalJob Step3");
                    return RepeatStatus.FINISHED;
                }).build();
    }

}
