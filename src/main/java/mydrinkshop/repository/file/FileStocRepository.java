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
        if(elems.length==0) return null;
        int id = Integer.parseInt(elems[0]);
        String ingredient = elems[1];
        double cantitate = Double.parseDouble(elems[2]);
        double stocMinim = Double.parseDouble(elems[3]);

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