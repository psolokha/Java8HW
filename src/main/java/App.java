import com.google.gson.*;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class App {

    public static ArrayList<Organization> orgList = new ArrayList<>();
    public static LocalDate currentDate = LocalDate.now();
    public static Consumer<String> printer = System.out::println;
    public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

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

        printer.accept("Список организаций: ");

        if (!(orgList.isEmpty()))orgList.forEach((o) -> printer.accept(o.toString()));
        printPastDueSecuritites();
        printByDate();
        printCurrencies();
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
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
        if (orgList.isEmpty()) return;
        printer.accept("Введите код валюты: ");
        try {
            String currency = reader.readLine();

        printer.accept(orgList.stream().filter(o -> {
            Securities[] sec = o.getSecurities();
            for (Securities s: sec) {
                if (s.getCurrency().getCode().equals(currency.toUpperCase())) {
                    printer.accept(s.getId() + " - " + s.getCode());
                    return true;
                }
            }
            return false;
            }).count()==0?"Валюты с таким кодом не найдено" : "Готово");
        } catch (IOException e) {
            System.out.println("Введено неверное значение");
            e.printStackTrace();
        }
    }

    public static void printByDate() {

        if (orgList.isEmpty()) return;

        printer.accept("Введите дату для отображения организай\nсозданных после этой даты: ");
        try  {
            String date = reader.readLine();

            LocalDate targetDate = LocalDate.of(getDate(date).get(2), getDate(date).get(1), getDate(date).get(0));
            ArrayList<Organization> tmpList = orgList.stream().filter(organization -> {
                LocalDate orgDate = LocalDate.of(getDate(organization.getEgrul_date()).get(0), getDate(organization.getEgrul_date()).get(1), getDate(organization.getEgrul_date()).get(2));
                return orgDate.compareTo(targetDate) > 0 ? true : false;
            }).collect(Collectors.toCollection(ArrayList::new));

            if (tmpList.isEmpty()) printer.accept("Нет организаций зарегистрированных после этой даты.\n");
            else tmpList.forEach(o -> printer.accept(o.toString()));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static void printPastDueSecuritites() {
        if (orgList.isEmpty()) return;
        printer.accept("Количество просроченных бумаг: " + orgList.stream().filter((org) -> {
            Securities[] sec = org.getSecurities();
            for (Securities s: sec) {
                LocalDate dateExpired = LocalDate.of(getDate(s.getDate_to()).get(0), getDate(s.getDate_to()).get(1), getDate(s.getDate_to()).get(2));
                if (dateExpired.compareTo(currentDate) < 0) {
                    printer.accept(s.getCode() + " - " + s.getDate_to() + " - " + s.getName_full());
                    return true;
                }
            }
            return false;
        }).count() + "\n");
    }

}
