package com.est.runtime.signup.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String levelName;

    @Column(nullable = false, unique = true)
    private Integer levelNumber;

    @OneToMany(mappedBy="userLevel", fetch = FetchType.EAGER)
    @Builder.Default
    private List<AuthorityForLevel> authorities = new ArrayList<>();
}