package com.zh.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class UserModel {
//    @Id
//    private long id;

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;
}
