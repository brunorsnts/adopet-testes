package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.ReprovacaoAdocaoDto;
import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDto;
import br.com.alura.adopet.api.model.*;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validacoes.ValidacaoSolicitacaoAdocao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class AdocaoServiceTest {

    @InjectMocks
    private AdocaoService service;

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private TutorRepository tutorRepository;

    @Mock
    private EmailService emailService;

    @Spy
    private List<ValidacaoSolicitacaoAdocao> validacoes = new ArrayList<>();

    @Mock
    private ValidacaoSolicitacaoAdocao validador1;

    @Mock
    private ValidacaoSolicitacaoAdocao validador2;

    @Mock
    private Pet pet;

    @Mock
    private Tutor tutor;

    @Mock
    private Abrigo abrigo;

    private SolicitacaoAdocaoDto dto;

    @Captor
    private ArgumentCaptor<Adocao> captor;

    @Test
    void deveriaSalvarAdocaoNoBanco() {

        //ARRANGE
        dto = new SolicitacaoAdocaoDto(1L, 2L, "Qualquer");
        given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);

        //ACT
        service.solicitar(dto);

        //ASSERT
        then(adocaoRepository).should().save(captor.capture());
        Adocao adocaoSalva = captor.getValue();
        Assertions.assertEquals(pet, adocaoSalva.getPet());
        Assertions.assertEquals(tutor, adocaoSalva.getTutor());
        Assertions.assertEquals(dto.motivo(), adocaoSalva.getMotivo());
    }

    @Test
    void deveriaChamarOMetodoValidarDeTodasAsValidacoes() {

        //ARRANGE
        dto = new SolicitacaoAdocaoDto(1L, 2L, "Qualquer");
        given(petRepository.getReferenceById(dto.idPet())).willReturn(pet);
        given(tutorRepository.getReferenceById(dto.idTutor())).willReturn(tutor);
        given(pet.getAbrigo()).willReturn(abrigo);
        validacoes.add(validador1);
        validacoes.add(validador2);

        //ACT
        service.solicitar(dto);

        //ASSERT
        then(validador1).should().validar(dto);
        then(validador2).should().validar(dto);
    }

    @Test
    void deveriaMarcarOStatusDaAdocaoComoAprovada() {

        //ARRANGE
        Adocao adocao = new Adocao(tutor, pet, "motivo qualquer");
        AprovacaoAdocaoDto aprovacaoAdocaoDto = new AprovacaoAdocaoDto(1L);
        given(adocaoRepository.getReferenceById(any())).willReturn(adocao);
        given(pet.getAbrigo()).willReturn(abrigo);

        //ACT
        service.aprovar(aprovacaoAdocaoDto);

        //ASSERT
        Assertions.assertEquals(StatusAdocao.APROVADO,adocao.getStatus());
    }

    @Test
    void deveriaMarcarOStatusDaAdocaoComoReprovada() {

        //ARRANGE
        Adocao adocao = new Adocao(tutor, pet, "motivo qualquer");
        ReprovacaoAdocaoDto reprovacaoAdocaoDto = new ReprovacaoAdocaoDto(1L, "Justificativa qualquer");
        given(adocaoRepository.getReferenceById(any())).willReturn(adocao);
        given(pet.getAbrigo()).willReturn(abrigo);

        //ACT
        service.reprovar(reprovacaoAdocaoDto);

        //ASSERT
        Assertions.assertEquals(StatusAdocao.REPROVADO,adocao.getStatus());
        Assertions.assertEquals(reprovacaoAdocaoDto.justificativa(), adocao.getJustificativaStatus());
    }
}