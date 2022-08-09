package com.mahmoudh.kiwe.entity;

import javax.persistence.*;

@Entity
@Table(name= "USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long userId;


}
