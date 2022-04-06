package ru.patyukov.ylab.zadanie6.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.patyukov.ylab.zadanie6.exceptions.XoException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class XoRestAPIControllerTest {

    @Autowired
    private XoRestAPIController xoRestAPIController;


    @Autowired
    private MockMvc mockMvc;

    @Test
    void help() throws Exception {
        mockMvc.perform(get("/gameplay/api/help"))
                .andExpect(status().isOk());
    }

    @Test
    void statisticsPlayerBD() throws Exception {
        mockMvc.perform(get("/gameplay/api/statisticsplayerBD"))
                .andExpect(status().isOk());
    }

    @Test
    void statisticsPlayerFile() throws Exception {
        mockMvc.perform(get("/gameplay/api/statisticsplayerFile"))
                .andExpect(status().isOk());
    }

    @Test
    void listPath() throws Exception {
        mockMvc.perform(get("/gameplay/api/listPath"))
                .andExpect(status().isOk());
    }

    @Test
    void nameFilePlay() throws Exception {
        mockMvc.perform(get("/gameplay/api/nameFilePlay/печенюха"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/gameplay/api/nameFilePlay/печенюха.xml"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/gameplay/api/nameFilePlay/печенюха.json"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/gameplay/api/nameFilePlay/Лена_and_Андрей_0.json"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/gameplay/api/nameFilePlay/Лена_and_Андрей_0.xml"))
                .andExpect(status().isOk());
    }

    @Test
    void listNameHistory() throws Exception {
        mockMvc.perform(get("/gameplay/api/listNameHistory"))
                .andExpect(status().isOk());
    }

    @Test
    void historyIdPlay() throws Exception {
        mockMvc.perform(get("/gameplay/api//historyIdPlay/0"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/gameplay/api//historyIdPlay/-100"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/gameplay/api//historyIdPlay/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/gameplay/api//historyIdPlay/1000"))
                .andExpect(status().isOk());
    }

    @Test
    void returnGameplay() throws Exception {
        mockMvc.perform(get("/gameplay/api//historyIdPlay/0"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/gameplay/api//historyIdPlay/-100"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/gameplay/api//historyIdPlay/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/gameplay/api//historyIdPlay/1000"))
                .andExpect(status().isOk());

    }

    @Test
    void fileplay() throws Exception {
    }

    @Test
    void playerSave() throws Exception {
    }

    @Test
    void playNext() throws Exception {
        mockMvc.perform(post("/gameplay/api/playNext/0/00"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/gameplay/api/playNext/-10/00"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/gameplay/api/playNext/100/00"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/gameplay/api/playNext/1/000"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/gameplay/api/playNext/1/0k"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/gameplay/api/playNext/1/00"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/gameplay/api/playNext/2/00"))
                .andExpect(status().isOk());
    }

    @Test
    void handle() {
        String s = xoRestAPIController.handle(new XoException("test"));
        Assertions.assertEquals("test", s);
    }
}