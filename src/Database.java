import Model.Fridge;
import Model.Recipe;
import Model.RecipeDifficulty;
import Model.WeightedIngredient;
import izuzeci.IngredientNotFoundException;
import izuzeci.RecipeNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Database {
    //5. Направити "базу" свих могућих састојака
    //Најмање 15 почетних
    //6. Направити "базу" свих рецепата
    //Најмање 10 почетних
    private final List<WeightedIngredient> sastojci = new ArrayList<WeightedIngredient>();
    private final List<Recipe> omiljeniRecepti = new ArrayList<Recipe>();
    private final List<Recipe> recepti = new ArrayList<Recipe>();
    private Fridge frizider = new Fridge();
    public Database() {
        pocetniSastojci();
        pocetniRecept();
        this.frizider = new Fridge();
    }

    public List<WeightedIngredient> getSastojci() {
        return sastojci;
    }

    public List<Recipe> getOmiljeniRecepti() {
        return omiljeniRecepti;
    }

    public List<Recipe> getRecepti() {
        return recepti;
    }

    public Fridge getFrizider() {
        return frizider;
    }

    private WeightedIngredient findIngredientById(int id) {
        for (WeightedIngredient wi : this.sastojci)
            if (wi.getId() == id)
                return wi;
        throw new IngredientNotFoundException("Sastojak sa id: " + id + "nepostoji.");
    }

    //1. Додавање намирнице у фрижидер (ако већ постоји, мења се количина)
    public void addIngredientToFridge(int ingredientId, double weight) {
        var sastojak = new WeightedIngredient(findIngredientById(ingredientId));
        sastojak.setWeight(weight);
        this.frizider.addWeightedIngredient(sastojak);
    }

    //2. Брисање намирнице из фрижидера
    //Смањење количине
    //Целокупно брисање
    public void subtractIngredientFromFridge(int ingredientId, double weight) {
        var sastojak = new WeightedIngredient(findIngredientById(ingredientId));
        sastojak.setWeight(weight);
        this.frizider.subtractWeightedIngredient(sastojak);
    }

    // 3. Провера која све јела може да направи са намирницама из фрижидера
    public List<Recipe> dostupniReceptiUFrizideru() {
        return this.dostupniReceptiUFrizideru(100);
    }

    //4. Провера која све скалирана јела може да направи (скалирање за 50%)
    public List<Recipe> dostupniReceptiUFrizideru(double procenatSkaliranja) {
        List<Recipe> dostupniRecepti = new ArrayList<Recipe>();
        for (Recipe recept : recepti) {
            try {
                this.frizider.provjeraSastojaka(recept.skaliranRecept(procenatSkaliranja));
                dostupniRecepti.add(recept.skaliranRecept(procenatSkaliranja));
            } catch (RuntimeException izuzetak) {
                continue;
            }
        }
        return dostupniRecepti;
    }

    //5. Прављење јела (као изнад)
    private Recipe receptiPoNazivu(String nazivJela) {
        for (Recipe recept : this.recepti)
            if (recept.getNazivRecepta().toLowerCase().equals(nazivJela.trim().toLowerCase()))
                return recept;
        throw new RecipeNotFoundException("Ne postoji recept sa imenom: " + nazivJela);
    }

    public void pravljenjeJela(String nazivJela, double procenat) {
        Recipe recept = receptiPoNazivu(nazivJela);
        frizider.pripremaJela(recept.skaliranRecept(procenat));
    }

    //6. Провера која све јела могу да се направе за X динара
    public List<Recipe> receptiDoXdinara(double price) {
        return this.recepti.stream().filter(recipe -> recipe.getPrice() <= price).collect(Collectors.toList());
    }

    //7. Провера која су све јела X тежине рецепта
    public List<Recipe> receptiPoTezini(RecipeDifficulty difficulty) {
        return this.recepti.stream().filter(recipe -> recipe.getTezinaRecepta().equals(difficulty)).collect(Collectors.toList());
    }

    //8. Комбинација 6. и 7. услова
    public List<Recipe> findAllRecipesByDifficultyAndPrice(double price, RecipeDifficulty difficulty) {
        List<Recipe> receptiPoCeni = receptiDoXdinara(price);
        List<Recipe> receptiPotezini = receptiPoTezini(difficulty);
        return receptiPotezini.stream().filter(receptiPoCeni::contains).collect(Collectors.toList()); // Presek dvije liste.
    }

    //    9. Корисник може да сортира рецепте по тежини
    public List<Recipe> sortiranjePoTezini() {
        return this.recepti.stream()
                .sorted((recept1, recept2) -> recept1.getTezinaRecepta().compareTo(recept2.getTezinaRecepta()))
                .collect(Collectors.toList());
    }
    //10. Корисник може да сортира рецепте по цени
    public List<Recipe> sortiranjePoCeni() {
        return this.recepti.stream().sorted((recept1, recept2) -> recept1.getPrice().compareTo(recept2.getPrice()))
                .collect(Collectors.toList());
    }
    //8. Додати могућност да корисник бира "омиљене" рецепте
    //Корисник може да погледа омиљене рецепте
    //Корисник може да дода или избрише омиљене рецепте
    //Корисник може да погледа омиљене рецепте до X динара
    public List<Recipe> omiljeniRecepti() {
        return this.omiljeniRecepti;
    }
    public void dodajOmiljenirecept(String nazivRecepta) {
        this.omiljeniRecepti.add(this.receptiPoNazivu(nazivRecepta));
    }

    public void izbrisiOmiljeniRecept(String nazivRecepta) {
        this.omiljeniRecepti
                .removeIf(favRecipe -> favRecipe.getNazivRecepta().toLowerCase().equals(nazivRecepta.trim().toLowerCase()));
    }

    public List<Recipe> omiljeniReceptiDoXDinara(double price) {
        return this.omiljeniRecepti.stream().filter(recipe -> recipe.getPrice() <= price).collect(Collectors.toList());
    }
    //6. Направити "базу" свих рецепата-Најмање 10 почетних
    private Recipe pocetniRecept(String title, ArrayList<Integer> ingredientIndexs, ArrayList<Double> scaleWeightOfRecipes, RecipeDifficulty difficulty) {
        var recept = new Recipe(title);
        for (int i=0;i<ingredientIndexs.size();i++)
            recept.addWeightedIngredients(sastojci.get(ingredientIndexs.get(i)).skaliranaTezina(scaleWeightOfRecipes.get(i)));
        recept.setTezinaRecepta(difficulty);
        return recept;
    }

    private void pocetniRecept() {
        this.recepti.add(pocetniRecept("Sarme",new ArrayList<Integer>() {{add(1);add(4);add(9);}},new ArrayList<Double>() {{add(100.0);add(50.0);add(90.0);}},RecipeDifficulty.EASY));
        this.recepti.add(pocetniRecept("Pita od jabuka",new ArrayList<Integer>() {{add(1);add(3);add(10);}},new ArrayList<Double>() {{add(100.0);add(50.0);add(90.0);}},RecipeDifficulty.EASY));
        this.recepti.add(pocetniRecept("Kajgana",new ArrayList<Integer>() {{add(10);}},new ArrayList<Double>() {{add(400.0);}},RecipeDifficulty.BEGINNER));
        this.recepti.add(pocetniRecept("Krofne",new ArrayList<Integer>() {{add(0);add(3);add(10);}},new ArrayList<Double>() {{add(300.0);add(50.0);add(90.0);}},RecipeDifficulty.MEDIUM));
        this.recepti.add(pocetniRecept("Palacinke",new ArrayList<Integer>() {{add(0);add(10);add(4);add(11);}},new ArrayList<Double>() {{add(100.0);add(50.0);add(100.0);add(10.0);}},RecipeDifficulty.BEGINNER));
        this.recepti.add(pocetniRecept("Cheesecake",new ArrayList<Integer>() {{add(13);add(3);add(8);add(11);}},new ArrayList<Double>() {{add(100.0);add(50.0);add(100.0);add(10.0);}},RecipeDifficulty.HARD));
        this.recepti.add(pocetniRecept("Belgijski vafl",new ArrayList<Integer>() {{add(4);add(5);add(10);add(14);}},new ArrayList<Double>() {{add(100.0);add(50.0);add(100.0);add(10.0);}},RecipeDifficulty.HARD));
        this.recepti.add(pocetniRecept("Baklava",new ArrayList<Integer>() {{add(9);add(3);add(10);add(14);}},new ArrayList<Double>() {{add(100.0);add(50.0);add(100.0);add(10.0);}},RecipeDifficulty.MEDIUM));
        this.recepti.add(pocetniRecept("Kebab",new ArrayList<Integer>() {{add(4);add(5);add(10);add(14);}},new ArrayList<Double>() {{add(100.0);add(50.0);add(100.0);add(10.0);}},RecipeDifficulty.PRO));
        this.recepti.add(pocetniRecept("Pohovani kackavalj",new ArrayList<Integer>() {{add(7);add(5);add(10);add(14);}},new ArrayList<Double>() {{add(100.0);add(50.0);add(100.0);add(10.0);}},RecipeDifficulty.HARD));
        this.recepti.add(pocetniRecept("Lazanje",new ArrayList<Integer>() {{add(1);add(4);add(15);add(14);}},new ArrayList<Double>() {{add(100.0);add(50.0);add(100.0);add(10.0);}},RecipeDifficulty.MEDIUM));
        this.recepti.add(pocetniRecept("Musaka",new ArrayList<Integer>() {{add(1);add(12);add(4);add(14);}},new ArrayList<Double>() {{add(100.0);add(50.0);add(100.0);add(10.0);}},RecipeDifficulty.HARD));
        this.recepti.add(pocetniRecept("Pileci rolat",new ArrayList<Integer>() {{add(1);add(4);add(7);add(15);}},new ArrayList<Double>() {{add(100.0);add(50.0);add(100.0);add(10.0);}},RecipeDifficulty.EASY));

    }
    private void pocetniSastojci() {
        this.sastojci.add(new WeightedIngredient("Mleko", 1.0, 60));//0
        this.sastojci.add(new WeightedIngredient("Meso", 1, 120));  //1
        this.sastojci.add(new WeightedIngredient("Jabuke", 1, 50)); //2
        this.sastojci.add(new WeightedIngredient("Secer", 1, 150)); //3
        this.sastojci.add(new WeightedIngredient("So", 1, 180));    //4
        this.sastojci.add(new WeightedIngredient("Brasno", 1, 90)); //5
        this.sastojci.add(new WeightedIngredient("Kupus", 1, 135));    //6
        this.sastojci.add(new WeightedIngredient("Kackavalj", 1, 95));//7
        this.sastojci.add(new WeightedIngredient("Jagode", 1, 79.99));//8
        this.sastojci.add(new WeightedIngredient("Orah", 1, 170.79));//9
        this.sastojci.add(new WeightedIngredient("Jaja", 1, 18));    //10
        this.sastojci.add(new WeightedIngredient("Plazma", 1, 135)); //11
        this.sastojci.add(new WeightedIngredient("Krompir", 1, 95)); //12
        this.sastojci.add(new WeightedIngredient("Pavlaka", 0.5, 79.99)); //13
        this.sastojci.add(new WeightedIngredient("Ulje", 1, 170.79));  //14
        this.sastojci.add(new WeightedIngredient("Biber", 1, 12.79));  //15

    }
}

