
public class Passenger {
    private int passengerId;
    private int survived;
    private int pClass;
    private String name;
    private String sex;
    private float age;
    private int sibSp;
    private int parch;
    private String ticket;
    private float fare;
    private String cabin;
    private String embarked;
    String[] dataArray;

    public Passenger(String data) {
      dataArray = data.split(",");

        setPassengerId();
        setSurvived();
        setpClass();
        setName();
        setSex();
        setAge();
        setSibSp();
        setParch();
        setTicket();
        setFare();
        setCabin();
        setEmbarked();

    }

    public void setPassengerId() {
        if (dataArray[Constants.PASSENGER_ID_INDEX] != null && !dataArray[Constants.PASSENGER_ID_INDEX].isEmpty()) {
            passengerId = Integer.parseInt(dataArray[Constants.PASSENGER_ID_INDEX]);
        } else {
            passengerId = 0;
        }
    }

    public void setSurvived() {
        if (dataArray[Constants.SURVIVED_INDEX] != null && !dataArray[Constants.SURVIVED_INDEX].isEmpty()) {
            survived = Integer.parseInt(dataArray[Constants.SURVIVED_INDEX]);
        }
    }

    public void setpClass() {
        if (dataArray[Constants.P_CLASS_INDEX] != null && !dataArray[Constants.P_CLASS_INDEX].isEmpty()) {
            pClass = Integer.parseInt(dataArray[Constants.P_CLASS_INDEX]);
        }
    }

    public void setName() {
        if (dataArray[Constants.NAME_SECOND_PART_INDEX] != null && !dataArray[Constants.NAME_SECOND_PART_INDEX].isEmpty()) {
            if (dataArray[Constants.NAME_FIRST_PART_INDEX] != null && !dataArray[Constants.NAME_FIRST_PART_INDEX].isEmpty()) {
                name = dataArray[Constants.NAME_FIRST_PART_INDEX] + " " + dataArray[Constants.NAME_SECOND_PART_INDEX];
            }
        } else {
            name = "";
        }
    }

    public void setSex() {
        if (dataArray[Constants.SEX_INDEX] != null && !dataArray[Constants.SEX_INDEX].isEmpty()) {
            sex = dataArray[Constants.SEX_INDEX];
        } else {
            sex = "";
        }
    }

    public void setAge() {
        if (dataArray[Constants.AGE_INDEX] != null && !dataArray[Constants.AGE_INDEX].isEmpty()) {
            age = Float.parseFloat(dataArray[Constants.AGE_INDEX]);
        } else {
            age = 0;
        }
    }

    public void setSibSp() {
        if (dataArray[Constants.SIB_SP_INDEX] != null && !dataArray[Constants.SIB_SP_INDEX].isEmpty()) {
            sibSp = Integer.parseInt(dataArray[Constants.SIB_SP_INDEX]);
        } else {
            sibSp = 0;
        }
    }

    public void setParch() {
        if (dataArray[Constants.PARCH_INDEX] != null && !dataArray[Constants.PARCH_INDEX].isEmpty()) {
            parch = Integer.parseInt(dataArray[Constants.PARCH_INDEX]);
        } else {
            parch = 0;
        }
    }

    public void setTicket() {
        if (dataArray[Constants.TICKET_ID_INDEX] != null && !dataArray[Constants.TICKET_ID_INDEX].isEmpty()) {
            ticket = dataArray[Constants.TICKET_ID_INDEX];
        } else {
            ticket = "";
        }
    }

    public void setFare() {
        if (dataArray[Constants.FARE_INDEX] != null && !dataArray[Constants.FARE_INDEX].isEmpty()) {
            fare = Float.parseFloat(dataArray[Constants.FARE_INDEX]);
        } else {
            fare = 0;
        }
    }

    public void setCabin() {
        if (dataArray[Constants.CABIN_INDEX] != null && !dataArray[Constants.CABIN_INDEX].isEmpty()) {
            cabin = dataArray[Constants.CABIN_INDEX];
        } else {
            cabin = "";
        }
    }

    public void setEmbarked() {
        try {
            if (dataArray[Constants.EMBARKED_INDEX] != null && !dataArray[Constants.EMBARKED_INDEX].isEmpty()) {
                embarked = dataArray[Constants.EMBARKED_INDEX];
            } else {
                embarked = "";
            }
        } catch (Exception e) {
            embarked = "";
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

    public String getClassAsString() {
        String pClass = "";
        switch (this.pClass) {
            case 1 -> pClass = "1st";
            case 2 -> pClass = "2nd";
            case 3 -> pClass = "3rd";
        }
        return pClass;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }


    public float getAge() {
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

    public String getEmbarked() {
        return embarked;
    }

   public String getFormattedName() {
        String formattedName = dataArray[Constants.NAME_FIRST_PART_INDEX] + dataArray[Constants.NAME_SECOND_PART_INDEX];
        int dotIndex = formattedName.indexOf(".");
        formattedName = formattedName.substring(dotIndex + 1);
        formattedName = formattedName.replace("\"", "")
                .replace("(", "")
                .replace(")", "")
                .trim();
        return formattedName;
    }


    public boolean isSurvived() {
        return this.survived == 1;

    }


}

