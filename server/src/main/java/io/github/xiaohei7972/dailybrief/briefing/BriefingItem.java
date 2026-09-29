package io.github.xiaohei7972.dailybrief.briefing;

import java.net.URI;

public record BriefingItem(
        String id,
        String title,
        String summary,
        URI sourceUrl
) {
}
