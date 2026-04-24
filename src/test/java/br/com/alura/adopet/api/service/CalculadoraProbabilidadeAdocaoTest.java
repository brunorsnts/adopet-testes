package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.ProbabilidadeAdocao;
import br.com.alura.adopet.api.model.TipoPet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalculadoraProbabilidadeAdocaoTest {

    @Test
    void deveriaRetornarProbabilidadeAltaQuandoGatoJovemPesoBaixo() {

        //ARRANGE
        CalculadoraProbabilidadeAdocao calculadora = new CalculadoraProbabilidadeAdocao();
        CadastroPetDto pet = new CadastroPetDto(
                TipoPet.GATO,
                "Ana",
                "Coisinha",
                4,
                "Laranja",
                4.0f
        );

        //ACT
        ProbabilidadeAdocao probabilidade = calculadora.calcular(new Pet(pet, new Abrigo()));

        //ASSERT
        Assertions.assertEquals(ProbabilidadeAdocao.ALTA, probabilidade);
    }

    @Test
    void deveriaRetornarProbabilidadeMediaQuandoGatoVelhoPesoBaixo() {

        //ARRANGE
        CalculadoraProbabilidadeAdocao calculadora = new CalculadoraProbabilidadeAdocao();
        CadastroPetDto pet = new CadastroPetDto(
                TipoPet.GATO,
                "Ana",
                "Coisinha",
                15,
                "Laranja",
                4.0f
        );

        //ACT
        ProbabilidadeAdocao probabilidade = calculadora.calcular(new Pet(pet, new Abrigo()));

        //ASSERT
        Assertions.assertEquals(ProbabilidadeAdocao.MEDIA, probabilidade);
    }
}
