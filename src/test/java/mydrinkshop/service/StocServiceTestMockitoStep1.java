package mydrinkshop.service;

import mydrinkshop.domain.Stoc;
import mydrinkshop.repository.file.FileAbstractRepository;
import mydrinkshop.service.validator.StocValidator;
import mydrinkshop.service.validator.ValidationException;
import mydrinkshop.service.validator.Validator;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StocServiceTestMockitoStep1 {
    private FileAbstractRepository<Integer, Stoc> stocRepository;
    private Validator<Stoc> vali;
    private Stoc stoc;
    private StocService stocService;

    @BeforeEach
    void setUp() {
        stocRepository = mock(FileAbstractRepository.class);
        vali = spy(StocValidator.class);
        stoc = mock(Stoc.class);
        stocService = new StocService(stocRepository, vali);
    }

    @Test
    @Tag("Mockito")
    @DisplayName("Test getAll()")
    @Order(1)
    void test_getAll() {
        Mockito.when(stocRepository.findAll()).thenReturn(List.of());

        assertEquals(List.of(),stocService.getAll());
        assert stocService.getAll().isEmpty();

        Mockito.verify(stocRepository, Mockito.times(2)).findAll();
        Mockito.verify(stocRepository, Mockito.never()).delete(1);
        Mockito.verify(stocRepository, Mockito.never()).update(stoc);
        Mockito.verify(stocRepository, Mockito.never()).findOne(1);
    }

    @Test
    @Tag("Mockito")
    @DisplayName("Test invalid add(...)")
    @Order(2)
    void test_add_invalid() {
        when(stoc.getId()).thenReturn(11);
        when(stoc.getIngredient()).thenReturn("");
        ValidationException e = assertThrows(ValidationException.class, ()->vali.validate(stoc));
        assertEquals("Ingredient invalid!\n", e.getMessage());

        doThrow(new ValidationException("eroare mock")).when(vali).validate(stoc);
        when(stocRepository.save(stoc)).thenReturn(stoc);
        e = assertThrows(ValidationException.class, ()->stocService.add(stoc));
        assertEquals("eroare mock", e.getMessage());
        verify(stocRepository, Mockito.never()).save(stoc);
        verify(vali, Mockito.times(2)).validate(stoc);
    }

    @Test
    @Tag("Mockito")
    @DisplayName("Test valid add(...)")
    @Order(3)
    void test_add_valid() {
        when(stoc.getId()).thenReturn(11);
        when(stoc.getIngredient()).thenReturn("apa");
        when(stoc.getCantitate()).thenReturn(10.0);
        when(stoc.getStocMinim()).thenReturn(5.0);
        try {
            stocService.add(stoc);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        verify(stocRepository, Mockito.times(1)).save(stoc);
    }
}