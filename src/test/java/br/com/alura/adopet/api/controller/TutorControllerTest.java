package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.service.TutorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@AutoConfigureMockMvc
@SpringBootTest
class TutorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TutorService tutorService;

    @Test
    void deveriaRealizarOCadastroDeUmTutorConfirmandoComStatus200() throws Exception {

        //ARRANGE
        String json = """
                {
                    "nome": "Bruno",
                    "telefone": "21985425875",
                    "email": "brd@gmail.com"
                }
                """;

        //ACT
        var resultado = mvc.perform(
                put("/tutores")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, resultado.getStatus());
    }

    @Test
    void naoDeveriaRealizarOCadastroDeUmTutorConfirmandoComStatus400() throws Exception {

        //ARRANGE
        String json = """
                {
                    "nome": "Bruno",
                    "telefone": "21985425875",
                    "email": "brdgmail.com"
                }
                """;

        //ACT
        var resultado = mvc.perform(
                post("/tutores")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(400, resultado.getStatus());
    }

    @Test
    void deveriaAtualizarOsDadosDeUmTutorConfirmandoComOStatus200() throws Exception {

        //ARRANGE
        String json = """
                {
                    "id": 1,
                    "nome": "carlo",
                    "telefone": "87536987458",
                    "email": "carlo@gmail.com"
                }
                """;

        //ACT
        var resultado = mvc.perform(
                post("/tutores")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, resultado.getStatus());
    }

    @Test
    void naoDeveriaAtualizarOsDadosDeUmTutorConfirmandoComOStatus400() throws Exception {

        //ARRANGE
        String json = """
                {
                    "id": 1,
                    "nome": "carlo",
                    "telefone": "87536987458",
                    "email": "carlogmail.com"
                }
                """;

        //ACT
        var resultado = mvc.perform(
                put("/tutores")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(400, resultado.getStatus());
    }

}