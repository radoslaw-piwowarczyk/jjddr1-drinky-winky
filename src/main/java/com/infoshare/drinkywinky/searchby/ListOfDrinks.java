package com.infoshare.drinkywinky.searchby;

import com.infoshare.drinkywinky.menu.Menu;
import com.infoshare.drinkywinky.properties.AppConfig;
import com.infoshare.drinkywinky.properties.ConfigLoader;
import com.infoshare.drinkywinky.repositories.Repository;
import com.infoshare.drinkywinky.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.infoshare.drinkywinky.menu.Menu.SCANNER;

//TODO - WHOLE CLASS ListOfDrinks ALREADY REWRITTEN TO new CLASS SubmenuCreator. This class is intended to DELETE!

public class ListOfDrinks {
    private static final Logger STDOUT = LoggerFactory.getLogger("CONSOLE_OUT");
    public static final int NUMBER_OF_DRINKS_BY_PAGE = 7;
    public static final String MENU_BUILDER = "│                                          │\n";
    public static final int MENU_WIDTH_1 = 37;
    public static final int MENU_WIDTH_2 = 33;
    private static Object SORT_TYPE = AppConfig.recipeSortType;
    private int pageNumber = 0;
    private int numberOfPages;
    private int trigger;
    private String in;
    private List<String> alphabeticalList;

    private List<String> currentDefaultListOfDrinks;

    //TODO - ALREADY REWRITTEN TO CLASS SubmenuCreator
    public void alphabeticalScrollingMenu(List<String> drinkList) {
        alphabeticalList = drinkList;
        countNumberOfMenuPages();
        ConfigLoader config = new ConfigLoader();
        config.loadAppConfig();
        alphabeticalList = drinkList;
        countNumberOfMenuPages();
        toAlphabeticalList();

        do {
            STDOUT.info("\n┌──────────────────────────────────────────┐\n");
            STDOUT.info("│ \u001b[33m CHOOSE NUMBER OF DRINK OR OTHER OPTION \u001b[0m │\n");
            STDOUT.info(MENU_BUILDER);

            fillingMenuByDrinks();

            STDOUT.info(MENU_BUILDER);

            showPageChangeArrows();

            STDOUT.info(MENU_BUILDER);
            STDOUT.info(MENU_BUILDER);
            STDOUT.info("│    Press \u001b[33mX\u001b[0m to return to the MAIN MENU    │\n");
            STDOUT.info("└──────────────────────────────────────────┘\n\n");

            STDOUT.info("\u001b[33mYOUR CHOICE: \u001b[0m");

            //TODO NULL POINTER EXCEPTION BY INPUT WRONG LETTER
            chooseTheOption();

        } while (true);
    }

    private void showPageChangeArrows() {
        STDOUT.info("│                Page {}/{}                  │\n", pageNumber + 1, numberOfPages);
        if (numberOfPages > 2 && pageNumber != 0 && pageNumber != numberOfPages - 1) {
            STDOUT.info("│   \u001b[33mP\u001b[0m <- Previous page    Next page -> \u001b[33mN\u001b[0m   │\n");
        }
        if (pageNumber == numberOfPages - 1 && numberOfPages > 1) {
            STDOUT.info("│   \u001b[33mP\u001b[0m <- Previous page                     │\n");
        }
        if (pageNumber == 0 && numberOfPages > 1) {
            STDOUT.info("│                         Next page -> \u001b[33mN\u001b[0m   │\n");
        }
    }

    private void fillingMenuByDrinks() {
        for (int i = (1 + pageNumber * NUMBER_OF_DRINKS_BY_PAGE); i <= (NUMBER_OF_DRINKS_BY_PAGE + (pageNumber * NUMBER_OF_DRINKS_BY_PAGE)); i++) {
            if (i <= alphabeticalList.size()) {
                int numberOfSpaces = MENU_WIDTH_1 - Integer.toString(i).length() - alphabeticalList.get(i - 1).length();
                String whitespace = String.format("%1$" + numberOfSpaces + "s", "");
                STDOUT.info("│   \u001b[33m{}.\u001b[0m {}{}│\n", i, alphabeticalList.get(i - 1), whitespace);
            } else {
                int numberOfSpaces2 = MENU_WIDTH_2 - Integer.toString(i).length();
                String whitespace = String.format("%1$" + numberOfSpaces2 + "s", "");
                STDOUT.info("│   \u001b[33m{}.\u001b[0m --- {}│\n", i, whitespace);
            }
        }
    }

    private void countNumberOfMenuPages() {
        numberOfPages = alphabeticalList.size() / NUMBER_OF_DRINKS_BY_PAGE;
        if (alphabeticalList.size() % NUMBER_OF_DRINKS_BY_PAGE != 0) {
            numberOfPages = alphabeticalList.size() / NUMBER_OF_DRINKS_BY_PAGE + 1;
        }
    }

    private void toAlphabeticalList() {
        if (SORT_TYPE.equals("DESC")) {
            alphabeticalList.sort(Collections.reverseOrder());
            alphabeticalList = new ArrayList<>(alphabeticalList);

        } else {
            alphabeticalList = alphabeticalList.stream().sorted().collect(Collectors.toList());
        }
    }

    //TODO - ALREADY REWRITTEN TO CLASS SubmenuCreator
    private void chooseTheOption() {
        in = SCANNER.next();
        trigger = 1;
        changePageOfMenu();
        quitToMainMenu();
        if (trigger == 1) {
            chooseSpecificDrink();
        }
    }

    //TODO - ALREADY REWRITTEN TO CLASS SubmenuCreator
    private void changePageOfMenu() {
        if (in.equalsIgnoreCase("N")) {
            if (pageNumber == numberOfPages - 1) {
                STDOUT.info("\n\u001b[31m It's LAST page, you cannot move forward!\u001b[0m\n");
            } else if (pageNumber < numberOfPages - 1) {
                pageNumber++;
            }
            trigger = 0;
        }

        if (in.equalsIgnoreCase("P")) {
            if (pageNumber == 0) {
                STDOUT.info("\n\u001b[31m It's FIRST page, you cannot move back!\u001b[0m\n");
            } else if (pageNumber > 0) {
                pageNumber--;
            }
            trigger = 0;
        }
    }

    //TODO - ALREADY REWRITTEN TO CLASS SubmenuCreator
    private void quitToMainMenu() {
        if (in.equalsIgnoreCase("X")) {

            Menu.mainMenu();
            trigger = 0;
        }
    }

    //TODO - ALREADY REWRITTEN TO CLASS SubmenuCreator
    private void chooseSpecificDrink() {
        if ((Integer.parseInt(in) >= (1 + pageNumber * NUMBER_OF_DRINKS_BY_PAGE))
                && (Integer.parseInt(in) <=
                (alphabeticalList.size()))) {

            String s = String.valueOf(Repository.getInstance().getDrinkByName(alphabeticalList.get(Integer.parseInt(in) - 1)));
            STDOUT.info(s);

        }
    }

    //TODO - ALREADY REWRITTEN TO CLASS SubmenuCreator
    public static void main(String[] args) {
        ListOfDrinks list = new ListOfDrinks();
        list.alphabeticalScrollingMenu(Utils.getNamesOfAllDrink(Repository.getInstance().getDrinkList()));
    }
}
