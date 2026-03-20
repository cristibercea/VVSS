package mydrinkshop.repository.file;

import mydrinkshop.domain.CategorieBautura;
import mydrinkshop.domain.Product;
import mydrinkshop.domain.TipBautura;
import mydrinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class FileProductRepositoryTest {
    FileProductRepository fileProductRepository;
    Product product;

    @BeforeEach
    void setUp() {
        fileProductRepository = new FileProductRepository("data/products.txt");
        product = new Product(2000000, "nume", 50.0, CategorieBautura.ALL, TipBautura.ALL);
    }

    @AfterEach
    void tearDown() {
        fileProductRepository.delete(product.getId());
        fileProductRepository=null;
        product=null;
    }

    @Test
    @Tag("ECP")
    @DisplayName("Testare Save ECP - nume valid")
    @Order(1)
    void test_ECP_Nume_Valid_Adaugare_Produs() {
        product.setNume("Latte");
        product.setPret(27.0);
        assertEquals(fileProductRepository.save(product), product);
    }

    @Test
    @Tag("ECP")
    @DisplayName("Testare Save ECP - pret valid")
    @Order(2)
    void test_ECP_Pret_Valid_Adaugare_Produs() {
        product.setNume("Flat White");
        product.setPret(10.0);
        assertEquals(fileProductRepository.save(product), product);
    }

    @Test
    @Tag("ECP")
    @DisplayName("Testare Save ECP - nume invalid")
    @Order(3)
    void test_ECP_Nume_Invalid_Adaugare_Produs() {
        product.setNume("");
        product.setPret(99.0);
        ValidationException ex = assertThrows(ValidationException.class,()->fileProductRepository.save(product));
        assertEquals("Numele nu poate fi gol!\n", ex.getMessage());
    }

    @Test
    @Tag("ECP")
    @DisplayName("Testare Save ECP - pret invalid")
    @Order(4)
    void test_ECP_Pret_Invalid_Adaugare_Produs() {
        product.setNume("Matcha");
        product.setPret(-100.0);
        ValidationException ex = assertThrows(ValidationException.class,()->fileProductRepository.save(product));
        assertEquals("Pret invalid!\n", ex.getMessage());
    }

    @Test
    @Tag("BVA")
    @DisplayName("Testare Save BVA - nume valid")
    @Order(5)
    void test_BVA_Nume_Valid_Adaugare_Produs() {
        product.setNume("M");
        product.setPret(5);
        assertEquals(fileProductRepository.save(product), product);
    }

    @Test
    @Tag("BVA")
    @DisplayName("Testare Save BVA - pret valid")
    @Order(6)
    void test_BVA_Pret_Valid_Adaugare_Produs() {
        product.setNume("Adf");
        product.setPret(1.0);
        assertEquals(fileProductRepository.save(product), product);
    }

    @Test
    @Tag("BVA")
    @DisplayName("Testare Save BVA - nume invalid")
    @Order(7)
    void test_BVA_Nume_Invalid_Adaugare_Produs() {
        product.setNume(null);
        product.setPret(50);
        ValidationException ex = assertThrows(ValidationException.class,()->fileProductRepository.save(product));
        assertEquals("Numele nu poate fi gol!\n", ex.getMessage());
    }

    @Test
    @Tag("BVA")
    @DisplayName("Testare Save BVA - pret invalid")
    @Order(8)
    void test_BVA_Pret_Invalid_Adaugare_Produs() {
        product.setNume("Adf");
        product.setPret(0);
        ValidationException ex = assertThrows(ValidationException.class,()->fileProductRepository.save(product));
        assertEquals("Pret invalid!\n", ex.getMessage());
    }

}