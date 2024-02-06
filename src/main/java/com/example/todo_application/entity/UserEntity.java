package com.example.todo_application.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "user_entity")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank
    private String username;

    @Column(nullable = false)
    @NotBlank
    private String password;

    @Column(unique = true, nullable = false)
    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.REMOVE)
    private List<TaskEntity> createdTasks;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.REMOVE)
    private List<TaskListEntity> createdTaskLists;


//    public UserEntity() {
//        this.role = Role.USER;
//    }

    @PrePersist
    public void init() {
        this.role = Role.USER;
    }
}
