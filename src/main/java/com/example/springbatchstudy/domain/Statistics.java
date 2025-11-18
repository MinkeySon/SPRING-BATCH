package com.example.springbatchstudy.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Statistics extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Comment("집계 일자")
    private LocalDate statDate;

    @Column(nullable = false)
    @Comment("수신 성공 건수")
    private Integer recvSuccCount;

    @Column(nullable = false)
    @Comment("수신 실패 건수")
    private Integer recvFailCount;

    @Column(nullable = false)
    @Comment("송신 성공 건수")
    private Integer sendSuccCount;

    @Column(nullable = false)
    @Comment("송신 실패 건수")
    private Integer sendFailCount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("송수신 여부")
    private DirectionEnum direction;
}
