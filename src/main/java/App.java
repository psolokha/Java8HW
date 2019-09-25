import com.google.gson.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;

public class App {

    public static void main(String... args){

        Consumer<String> printer = System.out::println;
        orgList.forEach((o) -> printer.accept(o.toString()));

        printPastDueSecuritites();

        printByDate("31/01/01");

        printCurrencies("USD");





    }

    public static ArrayList<Organization> orgList = new ArrayList<>();

    static {
        try {
            JsonArray jsonArray = new JsonParser().parse(new FileReader("/home/pavelsolokha/dev/java8hw/src/main/resources/test.json")).getAsJsonArray();

            Gson gson = new Gson();
            for (JsonElement element : jsonArray) {
                orgList.add(gson.fromJson(element, Organization.class));
            }
        } catch (IOException ex) {
            System.out.println("Не получается открыть файл");
            ex.printStackTrace();
        }
    }


    public static ArrayList<Integer> getDate(String date) {
        StringTokenizer tokenizer = new StringTokenizer(date, "/-:.,\\");
        ArrayList<Integer> dateList = new ArrayList<>();

        while (tokenizer.hasMoreTokens()) {
            dateList.add(Integer.parseInt(tokenizer.nextToken()));
        }

        if (String.valueOf(dateList.get(2)).length() < 3 && !(String.valueOf(dateList.get(0)).length() > 2)) {
            dateList.set(2, dateList.get(2) > 50? (1900 + dateList.get(2)): (2000 + dateList.get(2)));
        }

        return dateList;
    }

    public static void printCurrencies(String currency) {
        orgList.forEach(organization -> {
            Securities[] sec = organization.getSecurities();
            for (Securities s: sec) {
                if (s.getCurrency().getCode().equals(currency.toUpperCase())) System.out.println(s.getId() + " - " + s.getCode());
            }
        });
    }

    public static void printByDate(String date) {

        LocalDate targetDate = LocalDate.of(getDate(date).get(2), getDate(date).get(1), getDate(date).get(0));

        orgList.stream().filter(organization -> {
            LocalDate orgDate = LocalDate.of(getDate(organization.getEgrul_date()).get(0), getDate(organization.getEgrul_date()).get(1), getDate(organization.getEgrul_date()).get(2));
            return orgDate.compareTo(targetDate) > 0? true:false;
        }).forEach(organization -> System.out.println(organization.toString()));

    }


    public static void printPastDueSecuritites() {

        LocalDate currentDate = LocalDate.now();
        System.out.println("Количество просроченных бумаг: " + orgList.stream().filter((org) -> {
            Securities[] sec = org.getSecurities();
            for (Securities s: sec) {
                LocalDate dateExpired = LocalDate.of(getDate(s.getDate_to()).get(0), getDate(s.getDate_to()).get(1), getDate(s.getDate_to()).get(2));
                if (dateExpired.compareTo(currentDate) < 0) {
                    System.out.println(s.getCode() + " - " + s.getDate_to() + " - " + s.getName_full());
                    return true;
                }
            }
            return false;
        }).count());
    }
}
