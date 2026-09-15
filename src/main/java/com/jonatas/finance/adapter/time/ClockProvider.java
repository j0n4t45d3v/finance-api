package com.jonatas.finance.adapter.time;

import java.time.LocalDateTime;

public interface ClockProvider {

    LocalDateTime now();
}
