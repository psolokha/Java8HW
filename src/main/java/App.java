import com.google.gson.*;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class App {

    private static ArrayList<Organization> orgList = new ArrayList<>();
    private static LocalDate currentDate = LocalDate.now();
    private static Consumer<String> printer = System.out::println;
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    static {
        printer.accept("Введите путь к файлу: ");
        try  {
            FileReader fileReader = new FileReader(reader.readLine());
            JsonArray jsonArray = new JsonParser().parse(fileReader).getAsJsonArray();

            Gson gson = new Gson();
            for (JsonElement element : jsonArray) {
                orgList.add(gson.fromJson(element, Organization.class));
            }
            printer.accept("Создан список организайи\n");
        } catch (IOException ex) {
            printer.accept("Не получается открыть файл\nРабота программы приостановлена.\nПроверьте правильность пути и запустите программу повторно.");
            ex.printStackTrace();
        }
    }

    public static void main(String... args) {


        printOrganizations();
        printPastDueSecuritites();
        printByDate();
        printCurrencies();

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void printOrganizations(){
        if (!(orgList.isEmpty())){
            printer.accept("Список организаций: ");
            orgList.forEach((o) -> printer.accept(o.toString()));
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

    public static void printCurrencies() {
        if (!(orgList.isEmpty())) {
            printer.accept("Введите код валюты: ");
            try {
                String currency = reader.readLine();
                ArrayList<Securities> tmpSecList = new ArrayList<>();
                getAllSecurities().forEach(s -> {
                    if (s.getCurrency().getCode().equals(currency.toUpperCase())) tmpSecList.add(s);
                });
                if (tmpSecList.isEmpty()) printer.accept("Валюты с таким кодом не найдено");
                else tmpSecList.forEach(s->printer.accept(s.getId() + " - " + s.getCode()));
            } catch (IOException e) {
                System.out.println("Введено неверное значение");
                e.printStackTrace();
            }
        }
    }

    public static void printByDate() {

        if (!(orgList.isEmpty())) {

            printer.accept("Введите дату для отображения организаций\nсозданных после этой даты: ");
            try {
                String date = reader.readLine();

                LocalDate targetDate = LocalDate.of(getDate(date).get(2), getDate(date).get(1), getDate(date).get(0));
                ArrayList<Organization> tmpList = orgList.stream().filter(organization -> {
                    LocalDate orgDate = LocalDate.of(getDate(organization.getEgrul_date()).get(0), getDate(organization.getEgrul_date()).get(1), getDate(organization.getEgrul_date()).get(2));
                    return orgDate.compareTo(targetDate) > 0;
                }).collect(Collectors.toCollection(ArrayList::new));

                if (tmpList.isEmpty()) printer.accept("Нет организаций зарегистрированных после этой даты.\n");
                else tmpList.forEach(o -> printer.accept(o.toString()));

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static ArrayList<Securities> getAllSecurities() {
            ArrayList<Securities> tmpList = new ArrayList<>();

            orgList.forEach(organization -> {
                for (Securities sec : organization.getSecurities()) {
                    tmpList.add(sec);
                }
            });
            return tmpList;
        }


    public static void printPastDueSecuritites() {
        if (!(orgList.isEmpty())) {

            printer.accept("Количество просроченных бумаг: " + getAllSecurities().stream().filter(s -> {
                LocalDate dateExpired = LocalDate.of(getDate(s.getDate_to()).get(0), getDate(s.getDate_to()).get(1), getDate(s.getDate_to()).get(2));
                if (dateExpired.compareTo(currentDate) < 0) {
                    printer.accept(s.getCode() + " - " + s.getDate_to() + " - " + s.getName_full());
                    return true;
                }
                return false;
            }).count());
        }
    }

}
