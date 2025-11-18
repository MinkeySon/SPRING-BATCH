package com.example.springbatchstudy.domain;

import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TransactionLogs extends BaseTimeEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("송수신 여부")
    private DirectionEnum direction;

    @Column(nullable = false)
    @Comment("처리된 파일명")
    private String filePath;

    @Column
    @Comment("에러 사유")
    private String errorMessage;

    public TransactionLogs (DirectionEnum direction, String filePath) {
        this.direction = direction;
        this.filePath = filePath;
    }
}
