package mydrinkshop.service;

import mydrinkshop.domain.Stoc;
import mydrinkshop.repository.file.FileAbstractRepository;
import mydrinkshop.repository.file.FileStocRepository;
import mydrinkshop.service.validator.StocValidator;
import mydrinkshop.service.validator.ValidationException;
import mydrinkshop.service.validator.Validator;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StocServiceTestMockitoStep2 {
    private FileAbstractRepository<Integer, Stoc> stocRepository;
    private Validator<Stoc> vali;
    private Stoc stoc;
    private StocService stocService;

    @BeforeEach
    void setUp() {
        stocRepository = new FileStocRepository("data/stocuri.txt");
        vali = spy(StocValidator.class);
        stoc = mock(Stoc.class);
        stocService = new StocService(stocRepository, vali);
    }

    @Test
    @Tag("Mockito")
    @DisplayName("Test getAll()")
    @Order(1)
    void test_getAll() {
        assertEquals(10 ,stocService.getAll().size());
        assert !stocService.getAll().isEmpty();
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
        verify(vali, Mockito.times(1)).validate(stoc);

        doThrow(new ValidationException("eroare mock")).when(vali).validate(stoc);
        e = assertThrows(ValidationException.class, ()->stocService.add(stoc));
        assertEquals("eroare mock", e.getMessage());
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
            stocService.delete(11);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}