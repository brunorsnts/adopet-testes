package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroPetDto;
import br.com.alura.adopet.api.dto.PetDto;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.TipoPet;
import br.com.alura.adopet.api.repository.PetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @InjectMocks
    private PetService petService;

    @Mock
    private PetRepository petRepository;

    @Captor
    private ArgumentCaptor<Pet> captor;

    @Test
    void deveriaRetornarApenasOsPetsDisponiveis() {

        //ARRANGE
        Pet pet = new Pet();
        List<Pet> pets = List.of(pet);
        BDDMockito.given(petRepository.findAllByAdotadoFalse()).willReturn(pets);

        //ACT
        List<?> list = petService.buscarPetsDisponiveis();

        //ASSERT
        Assertions.assertEquals(1, list.size());
        list.forEach(e -> {
            Assertions.assertInstanceOf(PetDto.class, e);
        });
    }

    @Test
    void naoDeveriaRetornarPetsQuandoNaoEstaoDisponiveis() {

        //ARRANGE
        BDDMockito.given(petRepository.findAllByAdotadoFalse()).willReturn(List.of());

        //ACT
        List<?> list = petService.buscarPetsDisponiveis();

        //ASSERT
        Assertions.assertEquals(0, list.size());
    }

    @Test
    void deveriaCadastrarUmPetNoBanco() {

        //ARRANGE
        Abrigo abrigo = new Abrigo();
        CadastroPetDto cadastroPetDto = new CadastroPetDto(TipoPet.GATO,
                "Marcos",
                "Pastor Alemão",
                6,
                "Amarelo Com Branco",
                2.5F);

        //ACT
        petService.cadastrarPet(abrigo, cadastroPetDto);

        //ASSERT
        BDDMockito.then(petRepository).should().save(captor.capture());
        Pet pet = captor.getValue();
        Assertions.assertEquals(cadastroPetDto.nome(), pet.getNome());
        Assertions.assertEquals(cadastroPetDto.cor(), pet.getCor());
        Assertions.assertEquals(cadastroPetDto.idade(), pet.getIdade());
    }

}