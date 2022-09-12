package Model;

import java.util.ArrayList;
import java.util.List;

//3. Recipe implements Priceable
//Назив рецепта
//Тежина рецепта (Enum: BEGINNER, EASY, MEDIUM, HARD, PRO)
//Колекција WeightedIngredient-а



public class Recipe implements Priceable {
    private String nazivRecepta;
    private RecipeDifficulty tezinaRecepta;
    private List<WeightedIngredient> weightedIngredients;

    public Recipe(String nazivRecepta) {
        this.nazivRecepta = nazivRecepta;
        weightedIngredients = new ArrayList<>(); //na pocetku kad tek imamo naziv recepta ovo je prazno
    }

    public String getNazivRecepta() {
        return nazivRecepta;
    }

    public void setNazivRecepta(String nazivRecepta) {
        this.nazivRecepta = nazivRecepta;
    }

    public RecipeDifficulty getTezinaRecepta() {
        return tezinaRecepta;
    }

    public void setTezinaRecepta(RecipeDifficulty tezinaRecepta) {
        this.tezinaRecepta = tezinaRecepta;
    }

    public List<WeightedIngredient> getWeightedIngredients() {
        return weightedIngredients;
    }

    public void setWeightedIngredients(List<WeightedIngredient> weightedIngredients) {
        this.weightedIngredients = weightedIngredients;
    }
    //Методa за додавање састојака
    public void addWeightedIngredients(WeightedIngredient wi){
            if (this.weightedIngredients == null)
                this.weightedIngredients = new ArrayList<WeightedIngredient>();
            this.weightedIngredients.add(wi);
        }

    //Методa за брисање састојака
    public void removeWeightedIngredients(int weightedIngredientId) {
//        int index = -1;
//        for (int i = 0; i < this.weightedIngredients.size(); i++) {
//            if (weightedIngredients.get(i).getId() == weightedIngredientId) {
//                index = i;
//                break;
//            }
//        }
        this.weightedIngredients.removeIf(recipe -> recipe.getId() == weightedIngredientId);
    }
    //Метода која креира нови рецепт са X% састојака
    //Ако хоћемо пола рецепта: getScaledRecipe(50.0)
    //Враћа нови рецепт где је тежина сваког састојка скалирана за 50%
    //Ако хоћемо трећину рецепта: getScaledRecipe(33.333)
    //Нпр ако смо имали 6 јаја у рецепту, у новом ће бити 2 (2.xxxx)
    public Recipe skaliranRecept (double procenat) {
        Recipe skaliranRec=new Recipe(this.nazivRecepta);
        for (var wi : this.weightedIngredients)
            skaliranRec.addWeightedIngredients(wi.skaliranaTezina(procenat));
        skaliranRec.tezinaRecepta=this.tezinaRecepta;
        return skaliranRec;

    }
    //getPrice() - Укупна цена рецепта
    @Override
    public Double getPrice() {
        return weightedIngredients.stream().mapToDouble(WeightedIngredient::getPrice).sum();
    }
    @Override
    //moramo proci kroz petlju da izvucemo sastojke iz liste
    public String toString() {
        String sastojci = "";
        for (var wi : this.weightedIngredients)
            sastojci=sastojci + wi.toString();
        return "Recipe Title: '" + this.nazivRecepta + "', Difficulty: "+this.tezinaRecepta.toString()+ ", price: " + this.getPrice().toString()+
                "\nWeighted ingredients:\n" + sastojci;
    }}

