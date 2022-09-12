import Model.Recipe;
import Model.RecipeDifficulty;
import Model.WeightedIngredient;

import java.util.List;
import java.util.Scanner;

public class Application {
    private static void ispisSastojaka(List<WeightedIngredient> ingredients) {
        for (WeightedIngredient wi : ingredients) {
            System.out.println(wi.toString());
        }
    }
    private static int tryParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.out.println("Neispravan unos");
            return -1;
        }
    }

    private static double tryParseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            System.out.println("Neispravan unos");
            return -1;
        }
    }
        private static RecipeDifficulty tryParseRecipeDifficulty(String value) {
            try {
                return RecipeDifficulty.values()[Integer.parseInt(value)];
            } catch (Exception e) {
                System.out.println("Neispravan unos");
                return null;
            }
        }

    public static void main(String[] args) {
        boolean run = true;
        Scanner scanner = new Scanner(System.in);
        Database database = new Database();
        while (run) {
            String ulaz="       *Dobrodosli u nasu kuhinju*";
            String menu = "Za opciju koju zelite unesite zeljeni broj(1-14), a za kraj unesite 0:\n1. Dodavanje namirnice u frizider.\n2. Uzimanje namirnice iz frizidera\n3. Jela koja se mogu napraviti sa namirnicama iz frizidera\n"
                    + "4. Skalirana jela koja mozemo napraviti.\n5. Pravljenje jela\n6. Jela koja se mogu napraviti do zeljene cijene.\n"
                    + "7. Sva jela odredjene tezine recepta. \n8. Kombinacija 6. i 7.\n9. Lista sortiranih jela po tezini\n10. Lista sortiranih jela po ceni(Jela su sortirana od najmanje do najvece cene)\n"
                    + "11. Lista omiljenih recepata\n12. Dodaj omiljeni recept\n13. Brisanje omiljenog recepta\n14. Lista omiljenih recept do odredjene cene\n0. Kraj";
            System.out.println(ulaz);
            System.out.println(menu);
            String command = scanner.nextLine();
            switch (command) {
                case ("0"): {
                    run = false;
                    System.out.println("Program je zavrsen.");
                    break;
                }
                case ("1"): {
                    ispisSastojaka(database.getSastojci());
                    System.out.print("ID namirnice koju zelite dodati(pogledati listu namirnica):");
                    String id = scanner.nextLine();
                    System.out.print("Unesite tezinu namirnice:");
                    String weight = scanner.nextLine();
                    try {
                        if (tryParseInt(id) == -1 || tryParseDouble(weight) == -1)
                            break;
                        database.addIngredientToFridge(Integer.parseInt(id), Double.parseDouble(weight));
                    } catch (Exception izuzetak) {
                        System.out.println(izuzetak.getMessage());
                    }
                    break;
                }
                case ("2"): {
                    System.out.println(database.getFrizider().toString());
                    System.out.print("ID namirnice koju zelite uzeti (pogledati listu namirnica):");
                    String id = scanner.nextLine();
                    System.out.print("Unesite tezinu namirnice koju zelite da uzmete:");
                    String weight = scanner.nextLine();
                    try {
                        if (tryParseInt(id) == -1 || tryParseDouble(weight) == -1)
                            break;
                        database.subtractIngredientFromFridge(Integer.parseInt(id), Double.parseDouble(weight));
                    } catch (Exception izuzetak) {
                        System.out.println(izuzetak.getMessage());
                    }
                    break;
                }
                case ("3"): {
                    for (Recipe recept : database.dostupniReceptiUFrizideru())
                        System.out.println(recept.toString());
                    break;
                }
                case ("4"): {
                    System.out.print("Unesite procenat skaliranja:");
                    String procenat = scanner.nextLine();
                    if (tryParseDouble(procenat) == -1)
                        break;
                    for (Recipe recipe : database.dostupniReceptiUFrizideru(Double.parseDouble(procenat))) {
                        System.out.println(recipe.toString());
                    }
                    break;
                }
                case ("5"): {
                    System.out.print("Unesite naziv recepta koji zelite da pripremate:");
                    String recipeTitle = scanner.nextLine();
                    System.out.print("Unesite procenat skaliranja:");
                    String procenat = scanner.nextLine();
                    if (tryParseDouble(procenat) == -1)
                        break;
                    double scaleProcentage = 0;
                    if (procenat.trim().isEmpty())
                        scaleProcentage = 100;
                    else
                        scaleProcentage = Double.parseDouble(procenat);
                    try {
                        database.pravljenjeJela(recipeTitle, scaleProcentage);
                    } catch (Exception izuzetak) {
                        System.out.println(izuzetak.getMessage());
                    }
                    break;
                }
                case ("6"): {
                    System.out.print("Unesite maksimalnu cenu:");
                    String price = scanner.nextLine();
                    if (tryParseDouble(price) == -1)
                        break;
                    for (Recipe recipe : database.receptiDoXdinara(Double.parseDouble(price))) {
                        System.out.println(recipe.toString());
                    }
                    break;
                }
                case ("7"): {
                    System.out.print("Unesite tezinu:\n");
                    for (RecipeDifficulty diff : RecipeDifficulty.values()) {
                        System.out.println(diff.ordinal() + ". " + diff.toString() + "\n");
                    }
                    String diff = scanner.nextLine();
                    RecipeDifficulty difficulty = tryParseRecipeDifficulty(diff);
                    List<Recipe> list = database.receptiPoTezini(difficulty);
                    for (Recipe recipe : list)
                        System.out.println(recipe.toString());
                    break;
                }
                case ("8"): {
                    System.out.print("Unesite maksimalnu cenu:");
                    String price = scanner.nextLine();
                    System.out.print("Unesite tezinu:\n");
                    for (RecipeDifficulty diff : RecipeDifficulty.values()) {
                        System.out.println(diff.ordinal() + ". " + diff.toString() + "\n");
                    }
                    String diff = scanner.nextLine();
                    RecipeDifficulty difficulty = tryParseRecipeDifficulty(diff);
                    for (Recipe recipe : database.findAllRecipesByDifficultyAndPrice(Double.parseDouble(price),
                            difficulty)) {
                        System.out.println(recipe.toString());
                    }
                    break;

                }
                case ("9"): {
                    for (Recipe wi : database.sortiranjePoTezini()) {
                        System.out.println(wi.toString());
                    }
                    break;
                }
                case ("10"): {
                    for (Recipe wi : database.sortiranjePoCeni()) {
                        System.out.println(wi.toString());
                    }
                    break;
                }
                case ("11"): {
                    for (Recipe wi : database.omiljeniRecepti()) {
                        System.out.println(wi.toString());
                    }
                    break;
                }
                case ("12"): {
                    System.out.print("Unesite naziv recepta:");
                    String recipeTitle = scanner.nextLine();
                    try {
                        database.dodajOmiljenirecept(recipeTitle);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case ("13"): {
                    System.out.print("Unesite naziv recepta:");
                    String recipeTitle = scanner.nextLine();
                    try {
                        database.izbrisiOmiljeniRecept(recipeTitle);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case ("14"): {
                    System.out.print("Unesite maksimalnu cenu:");
                    String price = scanner.nextLine();
                    for (Recipe wi : database.omiljeniReceptiDoXDinara(Double.parseDouble(price))) {
                        System.out.println(wi.toString());
                    }
                    break;
                }

                default: {
                    continue;
                }
            }
        }
    }
}

