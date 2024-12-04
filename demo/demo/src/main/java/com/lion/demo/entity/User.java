package com.lion.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Entity
// DB
@Table(name = "users")
// Table Name
@Data // @Getter + @Setter + @ToString = @Data
// 기본 생성자, 모든 필드를 포함한 생성자 생성
@NoArgsConstructor
@AllArgsConstructor
// Builder 패턴 사용 가능
@Builder
public class User {
    @Id // primary key
    private String uid;
    private String pwd;
    private String uname;
    private String email;
    private LocalDate registerDate;
    private String role;
}
