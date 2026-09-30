package io.github.xiaohei7972.dailybrief.briefing;

import java.time.LocalDate;
import java.util.List;

public record Briefing(LocalDate date, List<BriefingItem> items) {
    public Briefing {
        items = List.copyOf(items);
    }
}
