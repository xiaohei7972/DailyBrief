package io.github.xiaohei7972.dailybrief.briefing;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class BriefingService {

    private final Clock clock;

    public BriefingService(Clock clock) {
        this.clock = clock;
    }

    public Briefing getTodayBriefing() {
        return new Briefing(LocalDate.now(clock), List.of());
    }
}
