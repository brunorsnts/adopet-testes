package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AbrigoDto;
import br.com.alura.adopet.api.dto.CadastroAbrigoDto;
import br.com.alura.adopet.api.dto.PetDto;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class AbrigoServiceTest {

    @InjectMocks
    private AbrigoService service;

    @Mock
    private AbrigoRepository abrigoRepository;

    @Mock
    private PetRepository petRepository;

    @Test
    void deveriaListarOsAbrigosCadastradosNoBanco() {
        //ARRANGE
        Abrigo abrigo1 = new Abrigo();
        Abrigo abrigo2 = new Abrigo();
        List<Abrigo> abrigos = List.of(abrigo1, abrigo2);
        BDDMockito.given(abrigoRepository.findAll()).willReturn(abrigos);

        //ACT
        List<AbrigoDto> retorno = service.listar();

        //ASSERT
        Assertions.assertEquals(2, retorno.size());
    }

    @Test
    void deveriaLancarValidacaoExceptionSeAbrigoJaEstiverCadastrado() {

        //ARRANGE
        CadastroAbrigoDto dto = new CadastroAbrigoDto("Abrigo da Paz", "22", "bb@gmail.com");
        BDDMockito.given(abrigoRepository.existsByNomeOrTelefoneOrEmail(any(), any(), any())).willReturn(true);

        //ACT + ASSERT
        Assertions.assertThrows(ValidacaoException.class, () -> service.cadatrar(dto));
    }

    @Test
    void deveriaSalvarCadastroDeAbrigoNoBanco() {

        //ARRANGE
        CadastroAbrigoDto dto = new CadastroAbrigoDto("Abrigo da Paz", "22", "bb@gmail.com");
        BDDMockito.given(abrigoRepository.existsByNomeOrTelefoneOrEmail(any(), any(), any())).willReturn(false);

        //ACT + ASSERT
        Assertions.assertDoesNotThrow(() -> service.cadatrar(dto));
        BDDMockito.then(abrigoRepository).should().save(any());
    }

    @Test
    void deveriaListarPetsDeUmAbrigoCasoIdExista() {

        //ARRANGE
        Long idExisting = 1L;
        Pet pet = new Pet();
        Abrigo abrigo = new Abrigo();
        BDDMockito.given(abrigoRepository.findById(idExisting)).willReturn(Optional.of(abrigo));
        BDDMockito.given(petRepository.findByAbrigo(abrigo)).willReturn(List.of(pet));

        //ACT
        List<PetDto> retorno = service.listarPetsDoAbrigo("1");

        //ASSERT
        Assertions.assertEquals(1, retorno.size());
    }

    @Test
    void deveriaListarPetsDeUmAbrigoCasoNomeDoAbrigoExista() {

        //ARRANGE
        String nomeExisting = "Raio de Sol";
        Pet pet = new Pet();
        Abrigo abrigo = new Abrigo();
        BDDMockito.given(abrigoRepository.findByNome(nomeExisting)).willReturn(Optional.of(abrigo));
        BDDMockito.given(petRepository.findByAbrigo(abrigo)).willReturn(List.of(pet));

        //ACT
        List<PetDto> retorno = service.listarPetsDoAbrigo("Raio de Sol");

        //ASSERT
        Assertions.assertEquals(1, retorno.size());
    }

    @Test
    void deveriaLancarValidacaoExceptionCasoIdDoAbrigoNaoExista() {
        //ARRANGE
        Long idNotExisting = 2L;
        BDDMockito.given(abrigoRepository.findById(idNotExisting)).willReturn(Optional.empty());

        //ACT + ASSERT
        Assertions.assertThrows(ValidacaoException.class, () -> service.listarPetsDoAbrigo("2"));
    }

    @Test
    void deveriaLancarValidacaoExceptionCasoNomeDoAbrigoNaoExista() {
        //ARRANGE
        String nomeNotExisting = "Mar e Sol";
        BDDMockito.given(abrigoRepository.findByNome(nomeNotExisting)).willReturn(Optional.empty());

        //ACT + ASSERT
        Assertions.assertThrows(ValidacaoException.class, () -> service.listarPetsDoAbrigo(nomeNotExisting));
    }
}