package io.github.xiaohei7972.dailybrief.briefing;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/briefings")
public class BriefingController {

    private final BriefingService briefingService;

    public BriefingController(BriefingService briefingService) {
        this.briefingService = briefingService;
    }

    @GetMapping("/today")
    public Briefing getTodayBriefing() {
        return briefingService.getTodayBriefing();
    }
}
