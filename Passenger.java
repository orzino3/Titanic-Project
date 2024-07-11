/*
import java.io.File;

public class Passenger
{
    private int passengerId;
    private int survived;
    private int pClass;
    private String name;
    private String sex;
    private int age;
    private int sibSp;
    private int parch;
    private String ticket;
    private float fare;
    private String cabin;
    private char embarked;

    public Passenger(String text){

        try {
            String[] strings = text.split(",");
            this.passengerId = Integer.parseInt(strings[0]);
            this.survived = Integer.parseInt(strings[1]);
            this.pClass = Integer.parseInt(strings[2]);
            this.name = strings[3]+strings[4];
            this.sex = strings[5];
            this.age = Integer.parseInt(strings[6]);
            this.sibSp = Integer.parseInt(strings[7]);
            this.parch = Integer.parseInt(strings[8]);
            this.ticket = strings[9];
            this.fare = Float.parseFloat(strings[10]);
            this.cabin = strings[11];
            this.embarked = strings[12].charAt(0);
        } catch (Exception e) {

        }

    }

    public String getName() {
        return name;
    }

    public String getFormattedName(){
        String temp = this.getName();

       String firstName = temp.substring((temp.indexOf('.')+2),temp.length());
        String lastName = temp.substring(0,temp.indexOf(','));


        return firstName+" "+lastName;
//this.name.substring(this.name.indexOf('.')+2,this.name.length())

    }


}
*/

public class Passenger {
    private int passengerId;
    private int survived;
    private int pClass;
    private String name;
    private String sex;
    private int age;
    private int sibSp;
    private int parch;
    private String ticket;
    private float fare;
    private String cabin;
    private char embarked;

    public Passenger(String text) {
        try {
            String[] strings = text.split(",");
            this.passengerId = Integer.parseInt(strings[0]);
            this.survived = Integer.parseInt(strings[1]);
            this.pClass = Integer.parseInt(strings[2]);
            this.name = strings[3] + strings[4];
            this.sex = strings[5];
            this.age = Integer.parseInt(strings[6]);
            this.sibSp = Integer.parseInt(strings[7]);
            this.parch = Integer.parseInt(strings[8]);
            this.ticket = strings[9];
            this.fare = Float.parseFloat(strings[10]);
            this.cabin = strings[11];
            this.embarked = strings[12].charAt(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getPassengerId() {
        return passengerId;
    }

    public int getSurvived() {
        return survived;
    }

    public int getPClass() {
        return pClass;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public int getAge() {
        return age;
    }

    public int getSibSp() {
        return sibSp;
    }

    public int getParch() {
        return parch;
    }

    public String getTicket() {
        return ticket;
    }

    public float getFare() {
        return fare;
    }

    public String getCabin() {
        return cabin;
    }

    public char getEmbarked() {
        return embarked;
    }

    public String getFormattedName() {
        String temp = this.getName();
        String firstName = temp.substring((temp.indexOf('.') + 2));
        String lastName = temp.substring(0, temp.indexOf(','));
        return firstName + " " + lastName;
    }
}

