package com.example.todo_application.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "user_entity")
@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.REMOVE)
    private List<TaskEntity> createdTasks;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.REMOVE)
    private List<TaskListEntity> createdTaskLists;


    public UserEntity() {
        this.role = Role.USER;
    }
}
