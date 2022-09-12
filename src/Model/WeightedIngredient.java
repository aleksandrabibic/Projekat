package Model;

import izuzeci.UnexpectedWeightException;

//2. WeightedIngredient extends Ingredient
//double weight
//double pricePerUnit

public class WeightedIngredient extends Ingredient{
    private static int count = 0;
    private double weight;
    private double pricePerUnit;

    public WeightedIngredient(int id, String nazivSastojka, double weight, double pricePerUnit) {
        super(id, nazivSastojka);
        this.weight = weight;
        this.pricePerUnit = pricePerUnit;
    }
    public WeightedIngredient(String name, double weight, double pricePerUnit) {
        super(count++, name);
        this.weight = weight;
        this.pricePerUnit = pricePerUnit;
    }
    public WeightedIngredient(WeightedIngredient wi) {
        super(wi.getId(), wi.getNazivSastojka());
        this.weight = wi.weight;
        this.pricePerUnit = wi.pricePerUnit;
    }



    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public double getWeight() {
        return weight;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    //funkcija za dodavanje tezine sastojku
    public void addWeight(double weight) {
        this.weight+=weight;
    }
    //funkcija za oduzimanje tezine sastojku
    public void subtractWeight(double weight) {
        if(this.weight<weight) throw new UnexpectedWeightException("Tezina koju imamo je manja od unesene.");
        this.weight-=weight;
    }
    //funkcija koja skalira svaki sastojak za proslijedjeni procenat
    public WeightedIngredient skaliranaTezina (double procenat) {
        return new WeightedIngredient(getId(), getNazivSastojka(), weight * (procenat / 100), pricePerUnit);
    }
    //getPrice() -> weight * pricePerUnit
    @Override
    public Double getPrice() {
        return weight * pricePerUnit;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + getId() + ", nazivSastojka='" + getNazivSastojka() + '\'' + "WeightedIngredient{" + "weight=" + weight +
                ", pricePerUnit=" + pricePerUnit + '}';
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WeightedIngredient)
            return ((WeightedIngredient) obj).getId() == this.getId();
        return false;
    }
}
