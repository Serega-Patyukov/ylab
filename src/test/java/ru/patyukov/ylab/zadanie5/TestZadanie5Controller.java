package ru.patyukov.ylab.zadanie5;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.patyukov.ylab.zadanie5.springboot.controller.Zadanie5Controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(Zadanie5Controller.class)
public class TestZadanie5Controller {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testErrorFile() throws Exception {
        mockMvc.perform(get("/gameplay/errorFile"))
                .andExpect(status().isOk())
                .andExpect(view().name("zadanie5/errorFile"))
                .andExpect(content().string(containsString("Крестики нолики")))
                .andExpect(content().string(containsString("Что-то пошло не так !!!")))
                .andExpect(content().string(containsString("Главная")));
    }

}
