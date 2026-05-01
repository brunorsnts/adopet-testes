package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.service.AbrigoService;
import br.com.alura.adopet.api.service.PetService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class AbrigoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AbrigoService abrigoService;

    @MockBean
    private PetService petService;

    @Test
    void deveriaRetornarCodigo200QuandoDadosDaRequisicaoParaCadastrarAbrigoForValido() throws Exception {

        //ARRANGE
        String json = """
                {
                    "nome": "Mar e Sol",
                    "telefone": "21985745236",
                    "email": "brd@gmail.com"
                }
                """;

        //ACT
        var response = mvc.perform(
                post("/abrigos")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)

        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaRetornarCodigo400QuandoDadosDaRequisicaoParaCadastrarAbrigoForInvalido() throws Exception {

        //ARRANGE
        String json = """
                {
                    "nome": "Mar e Sol",
                    "telefone": "21985745236",
                    "email": "brdgmail.com"
                }
                """;

        //ACT
        var response = mvc.perform(
                post("/abrigos")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)

        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    void deveriaRetornarCodigo200QuandoRetornarAbrigos() throws Exception {

        //ACT
        var response = mvc.perform(
                get("/abrigos")
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaRetornarCodigo200QuandoRetornarPetsDeUmAbrigo() throws Exception {

        //ARRANGE
        BDDMockito.given(abrigoService.listarPetsDoAbrigo(any())).willReturn(List.of());

        //ACT
        var response = mvc.perform(
                get("/abrigos/1/pets")
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaRetornarCodigo404QuandoAbrigoNaoFoiEncontrado() throws Exception {

        //ARRANGE
        BDDMockito.given(abrigoService.listarPetsDoAbrigo(any())).willThrow(ValidacaoException.class);

        //ACT
        var response = mvc.perform(
                get("/abrigos/1/pets")
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    void deveriaRetornarCodigo200QuandoPetFoiCadastradoEmUmAbrigoComSucesso() throws Exception {

        //ARRANGE
        String json = """
                {
                    "tipo": "CACHORRO",
                    "nome": "lulu",
                    "raca": "chiuaua",
                    "idade": 8,
                    "cor": "marrom",
                    "peso": 2.0  
                    
                }
                """;

        Abrigo abrigo = new Abrigo();
        BDDMockito.given(abrigoService.carregarAbrigo(any())).willReturn(abrigo);

        //ACT
        var response = mvc.perform(
                post("/abrigos/1/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    void deveriaRetornarCodigo404QuandoIdDoAbrigoNaoExisteParaCadastroDePet() throws Exception {

        //ARRANGE
        String json = """
                {
                    "tipo": "CACHORRO",
                    "nome": "lulu",
                    "raca": "chiuaua",
                    "idade": 8,
                    "cor": "marrom",
                    "peso": 2.0  
                    
                }
                """;

        BDDMockito.given(abrigoService.carregarAbrigo(any())).willThrow(ValidacaoException.class);

        //ACT
        var response = mvc.perform(
                post("/abrigos/2/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }
}