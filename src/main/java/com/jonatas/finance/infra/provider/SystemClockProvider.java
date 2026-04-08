package com.jonatas.finance.infra.provider;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class SystemClockProvider implements ClockProvider {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }

}
