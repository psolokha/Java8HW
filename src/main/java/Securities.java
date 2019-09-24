
public class Securities {

    private long id;
    private String code;
    private String name_full;
    private String cfi;
    private String date_to;
    private String state_reg_date;
    private State state;
    private Currency currency;

    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName_full() {
        return name_full;
    }

    public String getCfi() {
        return cfi;
    }

    public String getDate_to() {
        return date_to;
    }

    public String getState_reg_date() {
        return state_reg_date;
    }

    public State getState() {
        return state;
    }

    public Currency getCurrency() {
        return currency;
    }

    private class State {
        int id;
        String name;
    }
}
