package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AtualizacaoTutorDto;
import br.com.alura.adopet.api.dto.CadastroTutorDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class TutorServiceTest {

    @InjectMocks
    private TutorService tutorService;

    @Mock
    private TutorRepository tutorRepository;

    @Captor
    private ArgumentCaptor<Tutor> captor;

    @Test
    void deveriaCadastrarTutorNoBancoCasoEmailETelefoneJaNaoEstejaCadastrado() {

        //ARRANGE
        CadastroTutorDto dto = new CadastroTutorDto(
                "Bruno",
                "21999999999",
                "bbb@gmail.com"
        );
        given(tutorRepository.existsByTelefoneOrEmail(any(),any())).willReturn(false);

        //ACT
        tutorService.cadastrar(dto);

        //ASSERT
        then(tutorRepository).should().save(captor.capture());
        Tutor tutor = captor.getValue();
        Assertions.assertEquals(dto.nome(), tutor.getNome());
        Assertions.assertEquals(dto.email(), tutor.getEmail());
        Assertions.assertEquals(dto.telefone(), tutor.getTelefone());
    }

    @Test
    void deveriaLancarUmaValidacaoExceptionCasoEmailOuTelefoneDeCadastroDoTutorJaEstejaVinculadoAOutroTutor() {

        //ARRANGE
        CadastroTutorDto dto = new CadastroTutorDto(
                "Bruno",
                "21999999999",
                "bbb@gmail.com"
        );
        given(tutorRepository.existsByTelefoneOrEmail(any(),any())).willReturn(true);

        //ACT + ASSERT
        Assertions.assertThrows(ValidacaoException.class, () -> tutorService.cadastrar(dto));
    }

    @Test
    void deveriaAtualizarDadosDeUmTutor() {

        //ARRANGE
        Tutor tutor = new Tutor(new CadastroTutorDto("Bruno", "21999999999", "bbb@gmail.com"));
        given(tutorRepository.getReferenceById(any())).willReturn(tutor);
        AtualizacaoTutorDto dto = new AtualizacaoTutorDto(
                1L,
                "Alex",
                "2108745968",
                "alex@gmail.com"
        );

        // ACT
        tutorService.atualizar(dto);

        //ASSERT
        Assertions.assertEquals(dto.nome(), tutor.getNome());
        Assertions.assertEquals(dto.email(), tutor.getEmail());
        Assertions.assertEquals(dto.telefone(), tutor.getTelefone());
    }

}