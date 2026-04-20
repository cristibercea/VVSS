package mydrinkshop.service;

import mydrinkshop.domain.Stoc;
import mydrinkshop.repository.file.FileAbstractRepository;
import mydrinkshop.repository.file.FileStocRepository;
import mydrinkshop.service.validator.StocValidator;
import mydrinkshop.service.validator.ValidationException;
import mydrinkshop.service.validator.Validator;
import net.bytebuddy.pool.TypePool;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StocServiceTestMockitoStep3 {
    private FileAbstractRepository<Integer, Stoc> stocRepository;
    private Validator<Stoc> vali;
    private Stoc stoc;
    private StocService stocService;

    @BeforeEach
    void setUp() {
        stocRepository = new FileStocRepository("data/stocuri.txt");
        vali = new StocValidator();
        stoc = new Stoc(11, "", 10.0, 5.0);
        stocService = new StocService(stocRepository, vali);
    }


    @Test
    @Tag("Mockito")
    @DisplayName("Test getAll()")
    @Order(1)
    void test_getAll() {
        assertEquals(10, stocService.getAll().size());
        assert !stocService.getAll().isEmpty();
    }

    @Test
    @Tag("Mockito")
    @DisplayName("Test invalid add(...)")
    @Order(2)
    void test_add_invalid() {
        ValidationException e = assertThrows(ValidationException.class, ()->vali.validate(stoc));
        assertEquals("Ingredient invalid!\n", e.getMessage());

        e = assertThrows(ValidationException.class, ()->stocService.add(stoc));
        assertEquals("Ingredient invalid!\n", e.getMessage());
    }

    @Test
    @Tag("Mockito")
    @DisplayName("Test valid add(...)")
    @Order(3)
    void test_add_valid() {
        stoc.setIngredient("apa");
        try {
            stocService.add(stoc);
            stocService.delete(11);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}