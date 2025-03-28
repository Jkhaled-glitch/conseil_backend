package com.api.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.api.service.JobService;


@Service
public class JobServiceImpl implements JobService {

   


    private static final Logger logger = LoggerFactory.getLogger("example");


    @Override
    @Scheduled(cron = "1 0 0 1 * ?")
    public void scheduled() {
        logger.info("Job Synchronise Decompte Start");

       

        logger.info("Job Synchronise Decompte End");
    }

}

    
