package mydrinkshop.service;

import mydrinkshop.domain.IngredientReteta;
import mydrinkshop.domain.Reteta;
import mydrinkshop.domain.Stoc;
import mydrinkshop.repository.Repository;
import mydrinkshop.service.validator.RetetaValidator;
import mydrinkshop.service.validator.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StocService {

    private final Repository<Integer, Stoc> stocRepo;
    private final Validator<Stoc> validator;

    public StocService(Repository<Integer, Stoc> stocRepo, Validator<Stoc> validator) {
        this.stocRepo = stocRepo;
        this.validator = validator;
    }

    public List<Stoc> getAll() {
        return stocRepo.findAll();
    }

    public void add(Stoc s) {
        validator.validate(s);
        stocRepo.save(s);
    }

    public void update(Stoc s) {
        stocRepo.update(s);
    }

    public void delete(int id) {
        stocRepo.delete(id);
    }

    public boolean areSuficient(Reteta reteta) {
        List<IngredientReteta> ingredienteNecesare = reteta.getIngrediente();

        for (IngredientReteta e : ingredienteNecesare) {
            String ingredient = e.getDenumire();
            double necesar = e.getCantitate();

            double disponibil = stocRepo.findAll().stream()
                    .filter(s -> s.getIngredient().equalsIgnoreCase(ingredient))
                    .mapToDouble(Stoc::getCantitate)
                    .sum();

            if (disponibil < necesar) {
                return false;
            }
        }
        return true;
    }
    // am sters if redundant din for stoc; am sters variabile redundante; am refactorizat codul
    public void consuma(Reteta reteta) {

        if (!areSuficient(reteta))
            throw new IllegalStateException("Stoc insuficient pentru rețeta.");
        for (IngredientReteta e : reteta.getIngrediente()) {
            // S-a inlocuit lucrul cu streams in if si for
            List<Stoc> ingredienteStoc = new ArrayList<>();
            stocRepo.findAll().forEach(obj -> {
                if (obj.getIngredient().equalsIgnoreCase(e.getDenumire()))
                    ingredienteStoc.add(obj);
            });

            double cantitateIngredienteReteta = e.getCantitate();
            for (Stoc s : ingredienteStoc) {
                double cantitateFolosita = Math.min(s.getCantitate(), cantitateIngredienteReteta);
                s.setCantitate((int)(s.getCantitate() - cantitateFolosita));
                cantitateIngredienteReteta -= cantitateFolosita;

                stocRepo.update(s);
            }
        }
    }

}