package org.zju.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private String id;
    private String name;
    private Integer age;
    private String phone;
    private String password;
    public User(String name, String password){
        this.name = name;
        this.password = password;
    }
}