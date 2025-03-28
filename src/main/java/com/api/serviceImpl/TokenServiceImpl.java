package com.api.serviceImpl;

import com.api.dao.mysql.TokenDao;
import com.api.entities.mysql.User;
import com.api.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenDao tokenDao;

    @Autowired
    public TokenServiceImpl(TokenDao tokenDao) {
        this.tokenDao = tokenDao;
    }

    @Override
    public Integer countByUser(User user) {
        return tokenDao.countByUser(user);
    }
}