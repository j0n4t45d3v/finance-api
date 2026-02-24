package com.jonatas.finance.infra.provider;

import java.time.LocalDateTime;

public interface ClockProvider {

    LocalDateTime now();

}
