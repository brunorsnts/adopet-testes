package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ValidacaoTutorComLimiteDeAdocoesTest {

    @InjectMocks
    private ValidacaoTutorComLimiteDeAdocoes validacao;

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private Tutor tutor;

    @Test
    void deveriaLancarUmaValidacaoExceptionQuandoOTutorAtingiuOLimiteMaximoDeAdocoes() {

        // ARRANGE
        SolicitacaoAdocaoDto dto = new SolicitacaoAdocaoDto(1L, 2L, "Motivo qualquer");
        BDDMockito.given(tutorRepository.getReferenceById(any())).willReturn(tutor);
        BDDMockito.given(adocaoRepository.countByTutorAndStatus(tutor, StatusAdocao.APROVADO)).willReturn(5);

        //ACT + ASSERT
        Assertions.assertThrows(ValidacaoException.class, () -> validacao.validar(dto));
    }

    @Test
    void naoDeveriaLancarUmaValidacaoExceptionQuandoOTutorNaoAtingiuOLimiteMaximoDeAdocoes() {

        // ARRANGE
        SolicitacaoAdocaoDto dto = new SolicitacaoAdocaoDto(1L, 2L, "Motivo qualquer");
        BDDMockito.given(tutorRepository.getReferenceById(any())).willReturn(tutor);
        BDDMockito.given(adocaoRepository.countByTutorAndStatus(tutor, StatusAdocao.APROVADO)).willReturn(1);

        //ACT + ASSERT
        Assertions.assertDoesNotThrow(() -> validacao.validar(dto));
    }

}