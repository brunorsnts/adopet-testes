package br.com.alura.adopet.api.validacoes;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ValidacaoTutorComAdocaoEmAndamentoTest {

    @InjectMocks
    private ValidacaoTutorComAdocaoEmAndamento validacao;

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private Adocao adocao;

    @Mock
    private Tutor tutor;

    @Test
    void deveriaLancarUmaValidacaoExceptionQuandoTutorPossuiUmaAdocaoEmAndamento() {

        //ARRANGE
        List<Adocao> adocoes = new ArrayList<>();
        SolicitacaoAdocaoDto dto = new SolicitacaoAdocaoDto(1L, 2L, "Motivo Qualquer");
        adocoes.add(adocao);
        BDDMockito.given(adocaoRepository.findAll()).willReturn(adocoes);
        BDDMockito.given(tutorRepository.getReferenceById(any())).willReturn(tutor);
        BDDMockito.given(adocao.getTutor()).willReturn(tutor);
        BDDMockito.given(adocao.getStatus()).willReturn(StatusAdocao.AGUARDANDO_AVALIACAO);

        //ASSERT + ACT
        Assertions.assertThrows(ValidacaoException.class, () -> validacao.validar(dto));
    }

    @Test
    void naoDeveriaLancarUmaValidacaoExceptionQuandoTutorNaoPossuiUmaAdocaoEmAndamento() {

        //ARRANGE
        SolicitacaoAdocaoDto dto = new SolicitacaoAdocaoDto(1L, 2L, "Motivo Qualquer");
        List<Adocao> adocoes = List.of(adocao);
        BDDMockito.given(adocaoRepository.findAll()).willReturn(adocoes);
        BDDMockito.given(tutorRepository.getReferenceById(any())).willReturn(tutor);
        BDDMockito.given(adocao.getTutor()).willReturn(tutor);
        BDDMockito.given(adocao.getStatus()).willReturn(StatusAdocao.APROVADO);

        //ASSERT + ACT
        Assertions.assertDoesNotThrow(() -> validacao.validar(dto));
    }

}