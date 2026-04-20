package mydrinkshop.service;

import mydrinkshop.domain.IngredientReteta;
import mydrinkshop.domain.Reteta;
import mydrinkshop.domain.Stoc;
import mydrinkshop.repository.Repository;
import mydrinkshop.repository.file.FileRetetaRepository;
import mydrinkshop.repository.file.FileStocRepository;
import mydrinkshop.service.validator.StocValidator;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StocServiceTest {
    Reteta reteta;
    IngredientReteta ingredientReteta1, ingredientReteta2;
    String dummy_stoc_file = "stoc_gol.txt";
    Repository<Integer, Stoc> stocRepo;
    StocService stocService;
    FileRetetaRepository retetaRepository;
    double cantitate_cafea, cantitate_apa, cantitate_cafea_min, cantitate_apa_min;

    @BeforeEach
    void setUp() {
        ingredientReteta1 = new IngredientReteta("cafea_macinata",0);
        ingredientReteta2 = new IngredientReteta("apa",0);
        reteta = new Reteta(2000, List.of());
        stocRepo = new FileStocRepository("data/stocuri.txt");
        retetaRepository = new FileRetetaRepository("data/retete.txt");
        stocService = new StocService(stocRepo, new StocValidator());
        cantitate_cafea = stocRepo.findOne(1).getCantitate();
        cantitate_cafea_min = stocRepo.findOne(1).getStocMinim();
        cantitate_apa = stocRepo.findOne(2).getCantitate();
        cantitate_apa_min = stocRepo.findOne(2).getStocMinim();
    }

    @AfterEach
    void tearDown() {
        stocRepo.update(new Stoc(1,"cafea_macinata",cantitate_cafea,cantitate_cafea_min));
        stocRepo.update(new Stoc(2,"apa",cantitate_apa,cantitate_apa_min));
        retetaRepository.delete(2000);
    }

    @Test
    @Tag("WBT")
    @DisplayName("Testare consuma() - stoc insuficient")
    @Order(1)
    void test_WBT_consuma_invalid_cantitate() {
        ingredientReteta1.setCantitate(1400);
        ingredientReteta2.setCantitate(2);
        reteta.setIngrediente(List.of(ingredientReteta1,ingredientReteta2));
        IllegalStateException e = assertThrows(IllegalStateException.class, ()->stocService.consuma(reteta));
        assertEquals("Stoc insuficient pentru rețeta.", e.getMessage());
    }

    @Test
    @Tag("WBT")
    @DisplayName("Testare consuma() - reteta vida")
    @Order(2)
    void test_WBT_consuma_invalid_reteta_fara_ingrediente() {
        stocService.consuma(reteta);
        assertEquals(cantitate_cafea, stocRepo.findOne(1).getCantitate());
        assertEquals(cantitate_cafea_min, stocRepo.findOne(1).getStocMinim());
        assertEquals(cantitate_apa, stocRepo.findOne(2).getCantitate());
        assertEquals(cantitate_apa_min, stocRepo.findOne(2).getStocMinim());
    }

    @Test
    @Tag("WBT")
    @DisplayName("Testare consuma() - nu exista nimic pe stoc")
    @Order(3)
    void test_WBT_consuma_invalid_stoc_vid() {
        stocRepo = new FileStocRepository(dummy_stoc_file);
        stocService = new StocService(stocRepo, new StocValidator());
        reteta.setIngrediente(List.of(ingredientReteta1,ingredientReteta2));
        stocService.consuma(reteta);
        assertEquals(0, stocRepo.findAll().size());
        stocRepo = new FileStocRepository("data/stocuri.txt");
        stocService = new StocService(stocRepo, new StocValidator());
    }

    @Test
    @Tag("WBT")
    @DisplayName("Testare consuma() - toate bune")
    @Order(5)
    void test_WBT_consuma_valid() {
        ingredientReteta1.setCantitate(2);
        ingredientReteta2.setCantitate(2);
        reteta.setIngrediente(List.of(ingredientReteta1, ingredientReteta2));
        stocService.consuma(reteta);
        assertEquals(cantitate_cafea - 2, stocRepo.findOne(1).getCantitate());
        assertEquals(cantitate_apa - 2, stocRepo.findOne(2).getCantitate());
    }
}