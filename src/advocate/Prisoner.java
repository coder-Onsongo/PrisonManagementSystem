package advocate;

public class Prisoner {// a data class
    private int id;
    private String name;
    private int age;
    private String crime;
    private int monthsRemaining;

    public Prisoner(int id, String name, int age, String crime, int monthsRemaining) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.crime = crime;
        this.monthsRemaining = monthsRemaining;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getCrime() { return crime; }
    public int getMonthsRemaining() { return monthsRemaining; }
}

