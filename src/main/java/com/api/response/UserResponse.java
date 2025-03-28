package com.api.response;


import com.api.entities.mysql.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private String  username;
    private Role role;
    private String status;
}

