import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Organization {

    private int id;
    private String code;
    private String name_full;
    private String name_short;
    private String inn;
    private CompanyType company_type;
    private String ogrn;
    private String egrul_date;
    private Country country;
    private String fio_head;
    private String address;
    private String phone;
    private String e_mail;
    private String www;
    private Boolean is_resident;
    private Securities[] securities;

    @Override
    public String toString() {
        ArrayList<Integer> tmpList = App.getDate(egrul_date);
        return name_short + " - " + String.format("%02d/%02d/%02d", tmpList.get(2), tmpList.get(1), Integer.parseInt(String.valueOf(tmpList.get(0)).substring(2)));
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName_full() {
        return name_full;
    }

    public String getName_short() {
        return name_short;
    }

    public String getInn() {
        return inn;
    }

    public CompanyType getCompany_type() {
        return company_type;
    }

    public String getOgrn() {
        return ogrn;
    }

    public String getEgrul_date() {
        return egrul_date;
    }

    public Country getCountry() {
        return country;
    }

    public String getFio_head() {
        return fio_head;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getE_mail() {
        return e_mail;
    }

    public String getWww() {
        return www;
    }

    public Boolean getIs_resident() {
        return is_resident;
    }

    public Securities[] getSecurities() {
        return securities;
    }
}
