package ru.patyukov.ylab.zadanie6;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.support.SimpleSessionStatus;
import ru.patyukov.ylab.zadanie6.controller.XoRestAPIController;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(XoRestAPIController.class)
public class XoRestAPIControllerTest {

    private XoRestAPIController xoRestAPIController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void statisticsPlayerBbTest() throws Exception {
        //mockMvc.perform(get("/api/statisticsplayerBD"));
    }

    //    @Test   // Страница ошибок.
//    public void testErrorFile() throws Exception {
//        mockMvc.perform(get("/gameplay/errorFile"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("zadanie5/errorFile"))
//                .andExpect(content().string(containsString("Крестики нолики")))
//                .andExpect(content().string(containsString("Что-то пошло не так !!!")))
//                .andExpect(content().string(containsString("Главная")));
//    }

}
