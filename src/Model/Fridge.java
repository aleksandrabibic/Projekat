package Model;

import izuzeci.UnexpectedIngredientException;
import izuzeci.UnexpectedWeightException;

import java.util.ArrayList;
import java.util.List;

//4. Fridge
//Колекција WeightedIngredient-а
//Методе које додају и бришу намирнице
//Hint: Ако намирница постоји, додати количину


public class Fridge {
    private List<WeightedIngredient> weightedIngredients;

    //konstruktor
    public Fridge() {
        this.weightedIngredients = new ArrayList<WeightedIngredient>();
    }

    //Метода која додаје намирнице, прво проверимо да ли постоји намирница коју додајемо методом indexOf- ако метода врати -1
    //то значи да само додамо састојак, иначе ако врати да постоји тај састојак додамо му ту количну
    public void addWeightedIngredient(WeightedIngredient weightedIngredient) {
        int indeksSastojka = weightedIngredients.indexOf(weightedIngredient);
        if (indeksSastojka != -1)
            weightedIngredients.get(indeksSastojka).addWeight(weightedIngredient.getWeight());
        else
            weightedIngredients.add(weightedIngredient);
    }

    //Метода која брише намирнице
    //Уколико је индекс састојка -1, намирница не постоји, иначе уколико није смањујемо количину тог састојка
    //На крају ако је количина нула, бришемо тај састојак из листе састојака
    public void subtractWeightedIngredient(WeightedIngredient weightedIngredient) {
        int indeksSastojka = weightedIngredients.indexOf(weightedIngredient);
        if (indeksSastojka == -1) throw new UnexpectedIngredientException("Ova namirnica ne postoji u frizideru.");
        weightedIngredients.get(indeksSastojka).subtractWeight(weightedIngredient.getWeight());
        if (weightedIngredients.get(indeksSastojka).getWeight() == 0)
            weightedIngredients.remove(indeksSastojka);
    }

    //Метода која проверава да ли је могуће направити неко јело
    //Hint: Водити рачуна и о количини намирница
    public void provjeraSastojaka(Recipe recipe) {
        for (var sastojak : recipe.getWeightedIngredients()) {
            int indeksSastojka = weightedIngredients.indexOf(sastojak);
            if (indeksSastojka == -1) throw new UnexpectedIngredientException("Ova namirnica ne postoji u frizideru.");
            if (weightedIngredients.get(indeksSastojka).getWeight() < sastojak.getWeight())
                throw new UnexpectedWeightException("Tezina koju imamo je manja od unesene.");

        }
    }
    //Метода која "прави" јело, тј смањује количине састојака
    //Hint: Проверити да ли је уопште могуће направити јело
    public void pripremaJela(Recipe recipe) {
        provjeraSastojaka(recipe);
        for(var Sastojak : recipe.getWeightedIngredients()) {
            this.subtractWeightedIngredient(Sastojak);
        }
    }
    @Override
    public String toString() {
        String retVal="";
        for (WeightedIngredient wi : this.weightedIngredients) {
            retVal+=wi.toString();
        }
        return "Fridge\n" + retVal;
    }
}