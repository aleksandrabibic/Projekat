package Model;
//1. Ingredient (abstract) implements Priceable
//id
//Назив састојка
public abstract class Ingredient implements Priceable {

    private int id;
    private String nazivSastojka;

    public Ingredient(int id, String nazivSastojka) {
        this.id = id;
        this.nazivSastojka = nazivSastojka;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazivSastojka() {
        return nazivSastojka;
    }

    public void setNazivSastojka(String nazivSastojka) {
        this.nazivSastojka = nazivSastojka;
    }


}
