package ru.edu.cct.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.edu.cct.service.ExchangeRatesServiceImpl;

@Component
public class InvalidationScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRatesServiceImpl.class);

    @Scheduled(cron = "* 0 0 * * ?")
    public void invalidateCache() {
        LOG.info("InvalidationScheduler start");
    }
}
