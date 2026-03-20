package mydrinkshop.repository.file;

import mydrinkshop.domain.Stoc;
import mydrinkshop.service.validator.StocValidator;

public class FileStocRepository
        extends FileAbstractRepository<Integer, Stoc> {

    public FileStocRepository(String fileName) {
        super(fileName, new StocValidator());
        loadFromFile();
    }

    @Override
    protected Stoc extractEntity(String line) {
        String[] elems = line.split(";");

        int id = Integer.parseInt(elems[0]);
        String ingredient = elems[1];
        int cantitate = Integer.parseInt(elems[2]);
        int stocMinim = Integer.parseInt(elems[3]);

        return new Stoc(id, ingredient, cantitate, stocMinim);
    }

    @Override
    protected String createEntityAsString(Stoc entity) {
        return entity.getId() + ";" +
                entity.getIngredient() + ";" +
                entity.getCantitate() + ";" +
                entity.getStocMinim();
    }
}