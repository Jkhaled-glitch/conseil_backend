package com.api.service;

import com.api.entities.mysql.User;

public interface TokenService {
    public Integer countByUser(User user);
}

