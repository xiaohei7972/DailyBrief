package io.github.xiaohei7972.dailybrief.briefing;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BriefingController.class)
class BriefingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BriefingService briefingService;

    @Test
    void returnsTodayBriefing() throws Exception {
        when(briefingService.getTodayBriefing())
                .thenReturn(new Briefing(LocalDate.of(2026, 10, 1), List.of()));

        mockMvc.perform(get("/api/v1/briefings/today"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value("2026-10-01"))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items").isEmpty());
    }
}
