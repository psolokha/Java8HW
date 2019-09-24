import com.google.gson.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class App {

    public static void main(String... args){

        printOrganizationList();




        printPastDueSecuritites("13.12.1991");






    }

    public static ArrayList<Organization> orgList = new ArrayList<>();

    static {
        try {
            JsonArray jsonArray = new JsonParser().parse(new FileReader("D:\\projects\\Java\\java8hw\\src\\main\\resources\\test.json")).getAsJsonArray();

            Gson gson = new Gson();
            for (JsonElement element : jsonArray) {
                orgList.add(gson.fromJson(element, Organization.class));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void printOrganizationList() {
        orgList.forEach((org) -> System.out.println(org.toString()));
    }

    public static ArrayList<Integer> getDate(String date) {
        StringTokenizer tokenizer = new StringTokenizer(date, "/-:.,\\");
        ArrayList<Integer> dateList = new ArrayList<>();

        while (tokenizer.hasMoreTokens()) {
            dateList.add(Integer.parseInt(tokenizer.nextToken()));
        }

        return dateList;
    }


    public static void printPastDueSecuritites(String date) {

        LocalDate currentDate = LocalDate.now();
        LocalDate dateExpired = LocalDate.of(getDate(date).get(2), getDate(date).get(1), getDate(date).get(0));

        System.out.println(currentDate.toString());
        System.out.println(dateExpired.toString());



    }
}
