package com.example.springbatchstudy;

import com.example.springbatchstudy.domain.DirectionEnum;
import com.example.springbatchstudy.domain.TransactionLogs;
import com.example.springbatchstudy.repository.TransactionLogsRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Random;

@SpringBootTest(classes = SpringBatchStudyApplication.class)
@Slf4j
public class TransactionTest {

    @Autowired
    TransactionLogsRepository transactionLogsRepository;

    @Test
    @DisplayName("송수신 이력 생성")
    @Transactional
    void createTransaction(){

        DirectionEnum [] directionArr = new DirectionEnum[]{DirectionEnum.RECEIVE, DirectionEnum.SEND};

        Random random = new Random();

        for (int i=0; i<1000; i++){
            int randomIndex = (int) random.nextInt(2);
            String filePath = "ElecStrip" + "#" + i;

            TransactionLogs transactionLogs = new TransactionLogs(directionArr[randomIndex], filePath);
            transactionLogsRepository.save(transactionLogs);
        }
    }
}
