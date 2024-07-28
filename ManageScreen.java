import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class ManageScreen extends JPanel {

    private List<Passenger> passengerList;
    private List<Passenger> filteredList;
    private JComboBox<String> survivedComboBox;
    private JComboBox<String> sexComboBox;
    private JComboBox<String> embarkedComboBox;

    private JTextField nameTextField;
    private JTextField minIdTextField;
    private JTextField maxIdTextField;
    private JTextField sibSpTextField;
    private JTextField parchTextField;
    private JTextField ticketTextField;
    private JTextField minFareTextField;
    private JTextField maxFareTextField;
    private JTextField cabinTextField;

    private JTextArea result;
    private JButton filter;
    private JButton getStats;
    private int filterInstances;

    private JButton groupBy;
    private JComboBox<String> groupByComboBox;
    private JTextArea groupByArea;



    public ManageScreen(int x, int y, int width, int height) {
        filterInstances = 0;
        passengerList = new ArrayList<>();
        filteredList = new ArrayList<>();
        
        File file = new File(Constants.PATH_TO_DATA_FILE); //this is the path to the data file
        if (file.exists()) {
            this.setLayout(null);
            this.setBounds(x, y + Constants.MARGIN_FROM_TOP, width, height);
            initFields(x,y);
            try {
                Scanner scanner = new Scanner(file);
                scanner.nextLine();
                while (scanner.hasNextLine()){
                    String info = scanner.nextLine();
                    Passenger passenger = new Passenger(info);
                    passengerList.add(passenger);
                }
                scanner.close();
            } catch (FileNotFoundException _) {
            }
            
            this.filter.addActionListener((event ->{
                filterListAction();
            }));

            this.getStats.addActionListener((event ->{
                createStatisticsFile();
            }));
            this.groupBy.addActionListener((event -> {
                groupByAction(groupByComboBox);
            }));
        }
    }

    private void groupByAction(JComboBox<String> comboBox) {
        String selectedItem = (String)comboBox.getSelectedItem();
        switch (selectedItem){
            case "Class":
                setPercentText(calculatePercentageByClass());
                break;

            case "Survived":
                setPercentText(calculatePercentageBySurvived());
                break;

            case "Sex":
                setPercentText(calculatePercentageBySex());
                break;

            case "Age":
                setPercentText(calculatePercentageByAgeGroups());
                break;

            case "Relatives":
                setPercentText(calculatePercentageByParchAndSib());
                break;

            case "Fare":
                setPercentText(calculatePercentageByFareGroups());
                break;

            case "Embarked":
                setPercentText(calculatePercentageByEmbarked());
                break;

            case null:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + selectedItem);
        }


        }



    private void setPercentText(Map<String, Double> data) {
        List<Map.Entry<String, Double>> entryList = new ArrayList<>(data.entrySet());
        entryList.sort(new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return Double.compare(o2.getValue(), o1.getValue());
            }
        });
        StringBuilder message = new StringBuilder();
        for (Map.Entry<String, Double> entry : entryList) {
            message.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        groupByArea.setText(message.toString());
    }

    public Map<String, Double> calculatePercentageByClass() {
        Map<String, Double> percentages = new LinkedHashMap<>();
        int totalPassengers = passengerList.size();
        for (String option : Constants.PASSENGER_CLASS_OPTIONS) {
            if (!option.equals("All")) {
                List<Passenger> classPassengers = passengerList.stream()
                        .filter(passenger -> passenger.getClassAsString().equals(option))
                        .toList();
                double percentage = (classPassengers.size() * Constants.HUNDRED) / totalPassengers;
                percentages.put(option, percentage);
            }
        }

        return percentages;
    }

    public Map<String, Double> calculatePercentageBySurvived() {
        Map<String, Double> percentages = new LinkedHashMap<>();
        int totalPassengers = passengerList.size();
        long survived = passengerList.stream()
                .filter(Passenger::isSurvived)
                .count();
        long notSurvived = totalPassengers - survived;
        percentages.put("Survived: ", (survived * Constants.HUNDRED) / totalPassengers);
        percentages.put("Not survived: ", (notSurvived * Constants.HUNDRED) / totalPassengers);
        return percentages;
    }

    public Map<String, Double> calculatePercentageBySex() {
        Map<String, Double> percentages = new LinkedHashMap<>();
        int totalPassengers = passengerList.size();
        for (String sex : new String[]{"male", "female"}) {
            List<Passenger> sexByPassenger = passengerList.stream()
                    .filter(passenger -> passenger.getSex().equals(sex))
                    .toList();
            double percentage = (sexByPassenger.size() * Constants.HUNDRED) / totalPassengers;
            percentages.put(sex, percentage);
        }

        return percentages;
    }

    public Map<String, Double> calculatePercentageByAgeGroups() {
        Map<String, Double> percentages = new LinkedHashMap<>();
        int totalPassengers = passengerList.size();

        long childCount = passengerList.stream()
                .filter(passenger -> passenger.getAge() <= Constants.CHILD)
                .count();

        long adultCount = passengerList.stream()
                .filter(passenger -> passenger.getAge() >= Constants.ADULT_MIN &&
                        passenger.getAge() <= Constants.ADULT_MAX)
                .count();

        long elderlyCount = passengerList.stream()
                .filter(passenger -> passenger.getAge() >= Constants.ELDER)
                .count();

        percentages.put("Child (0 - 17)", (childCount * Constants.HUNDRED) / totalPassengers);
        percentages.put("Adult (18 - 64)", (adultCount * Constants.HUNDRED) / totalPassengers);
        percentages.put("Elderly (65+)", (elderlyCount * Constants.HUNDRED) / totalPassengers);

        return percentages;
    }

    public Map<String, Double> calculatePercentageByParchAndSib() {
        Map<String, Double> percentages = new LinkedHashMap<>();
        int totalPassengers = passengerList.size();

        long withFamily = passengerList.stream()
                .filter(passenger -> passenger.getSibSp() + passenger.getParch() > 0)
                .count();
        long withoutFamily = passengerList.stream()
                .filter(passenger -> passenger.getSibSp() + passenger.getParch() == 0)
                .count();

        percentages.put("With family", (withFamily * Constants.HUNDRED) / totalPassengers);
        percentages.put("Without family", (withoutFamily * Constants.HUNDRED) / totalPassengers);

        return percentages;
    }

    public Map<String, Double> calculatePercentageByFareGroups() {
        Map<String, Double> percentages = new LinkedHashMap<>();
        int totalPassengers = passengerList.size();
        long lowFareCount = passengerList.stream()
                .filter(passenger -> passenger.getFare() <= Constants.LOW_FARE)
                .count();
        long mediumFareCount = passengerList.stream()
                .filter(passenger -> passenger.getFare() >= Constants.MID_FARE_MIN &&
                        passenger.getFare() <= Constants.MID_FARE_MAX)
                .count();
        long highFareCount = passengerList.stream()
                .filter(passenger -> passenger.getFare() >= Constants.HIGH_FARE)
                .count();
        percentages.put("Low Fare (0 - 49)", (lowFareCount * Constants.HUNDRED) / totalPassengers);
        percentages.put("Medium Fare (50 - 99)", (mediumFareCount * Constants.HUNDRED) / totalPassengers);
        percentages.put("High Fare (100+)", (highFareCount * Constants.HUNDRED) / totalPassengers);

        return percentages;
    }

    public Map<String, Double> calculatePercentageByEmbarked() {
        Map<String, Double> percentages = new LinkedHashMap<>();
        int totalPassengers = passengerList.size();
        long embarkedSCount = passengerList.stream()
                .filter(passenger -> passenger.getEmbarked().equals("S"))
                .count();
        long embarkedCCount = passengerList.stream()
                .filter(passenger -> passenger.getEmbarked().equals("C"))
                .count();
        long embarkedQCount = passengerList.stream()
                .filter(passenger -> passenger.getEmbarked().equals("Q"))
                .count();

        percentages.put("Embarked S", (embarkedSCount * Constants.HUNDRED) / totalPassengers);
        percentages.put("Embarked C", (embarkedCCount * Constants.HUNDRED) / totalPassengers);
        percentages.put("Embarked Q", (embarkedQCount * Constants.HUNDRED) / totalPassengers);

        return percentages;
    }

    private void filterListAction() {
        this.filteredList.clear();

        String filterByClass = String.valueOf(survivedComboBox.getSelectedItem());
        String filterBySex = String.valueOf(sexComboBox.getSelectedItem());
        String filterByDock = String.valueOf(embarkedComboBox.getSelectedItem());

        int minIdVal = convertTextToInteger(minIdTextField);
        int maxIdVal = convertTextToInteger(maxIdTextField);
        float fareMinVal = convertTextToFloat(minFareTextField);
        float fareMaxVal = convertTextToFloat(maxFareTextField);

        filteredList.addAll(
                passengerList.stream()
                        .filter(passenger -> checkText(nameTextField, passenger.getName()))
                        .filter(passenger -> checkId(passenger.getPassengerId(), minIdVal, maxIdVal))
                        .filter(passenger -> checkText(sibSpTextField, passenger.getSibSp()))
                        .filter(passenger -> checkText(parchTextField, passenger.getParch()))
                        .filter(passenger -> checkText(ticketTextField, passenger.getTicket()))
                        .filter(passenger -> checkFare(passenger.getFare(), fareMinVal, fareMaxVal))
                        .filter(passenger -> checkText(cabinTextField, passenger.getCabin()))
                        .filter(passenger -> checkEmbark(passenger, filterByDock))
                        .filter(passenger -> matchesSex(passenger, filterBySex))
                        .filter(passenger -> checkClass(passenger, filterByClass))
                        .toList()
        );

        int totalCount = filteredList.size();
        int survivedCount = (int) filteredList.stream().filter(passenger -> passenger.getSurvived() == 1).count();
        int notSurvivedCount = totalCount - survivedCount;

        String resultText = """
                Total Rows: """ + totalCount
                + """
                \nSurvived: """ + survivedCount
                + """
                \nDid not Survived: """ + notSurvivedCount;
        result.setText(resultText);

        String fileName = generateFileName();
        createCSVFile(fileName);


    }


    private <T> boolean checkText(JTextField textField, T value) {
        String text = textField.getText().trim();
        if (text.isEmpty()) {
            return true;
        }

        return String.valueOf(value).toLowerCase().contains(text.toLowerCase());
    }

    private boolean checkId(int id, int minId, int maxId){
        if (minId == -1 && maxId == -1) {
            return true;
        }
        if (minId == -1) {
            return id <= maxId;
        }
        if (maxId == -1) {
            return id >= minId;
        }
        return id >= minId && id <= maxId;
    }

    private boolean checkFare(float fare, float minFare, float maxFare) {
        if (minFare == -1.0f && maxFare == -1.0f) {
            return true;
        }
        if (minFare == -1.0f) {
            return fare <= maxFare;
        }
        if (maxFare == -1.0f) {
            return fare >= minFare;
        }
        return fare >= minFare && fare <= maxFare;
    }

    private boolean checkEmbark(Passenger passenger, String embarked) {
        if (embarked.equals("All")) {
            return true;
        }
        if (passenger.getEmbarked() != null) {
            return passenger.getEmbarked().equalsIgnoreCase(embarked);
        }
        return false;
    }

    private boolean matchesSex(Passenger passenger, String selectedSex) {
        if (selectedSex.equals("All")) {
            return true;
        }
        return passenger.getSex().equalsIgnoreCase(selectedSex);
    }

    private boolean checkClass(Passenger passenger, String selectedClass) {
        if (selectedClass.equals("All")) {
            return true;
        }
        int pClass = passenger.getPClass();
        return switch (selectedClass) {
            case "1st" -> pClass == 1;
            case "2nd" -> pClass == 2;
            case "3rd" -> pClass == 3;
            default -> false;
        };
    }


    private JTextField createTextField(String label, int x, int y, int width, int height) {
        JLabel jLabel = new JLabel(label);
        jLabel.setBounds(x, y, Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
        this.add(jLabel);

        JTextField jTextField = new JTextField();
        jTextField.setBounds(x + Constants.LABEL_WIDTH, y, Constants.COMBO_BOX_WIDTH, Constants.COMBO_BOX_HEIGHT);
        this.add(jTextField);

        return jTextField;
    }

    private void initFields(int x, int y){

        JLabel survivedLabel = new JLabel("Passenger Class: ");
        survivedLabel.setBounds(x + Constants.MARGIN_FROM_LEFT, y, Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);


        this.add(survivedLabel);
        this.survivedComboBox = new JComboBox<>(Constants.PASSENGER_CLASS_OPTIONS);
        this.survivedComboBox.setBounds(survivedLabel.getX() + survivedLabel.getWidth() + 1, survivedLabel.getY(), Constants.COMBO_BOX_WIDTH, Constants.COMBO_BOX_HEIGHT);
        this.add(this.survivedComboBox);//------

        int textFieldX = survivedLabel.getX();
        int textFieldY = survivedComboBox.getY() + Constants.COMBO_BOX_HEIGHT + Constants.GAP;
        int textFieldWidth = Constants.LABEL_WIDTH + Constants.COMBO_BOX_WIDTH;
        int textFieldHeight = Constants.COMBO_BOX_HEIGHT;

        this.minIdTextField = createTextField("ID Min:", textFieldX, textFieldY , textFieldWidth, textFieldHeight);
        this.maxIdTextField = createTextField("ID Max:", textFieldX, textFieldY += Constants.OBJECT_MARGIN, textFieldWidth, textFieldHeight);
        this.nameTextField = createTextField("Name:", textFieldX, textFieldY += Constants.OBJECT_MARGIN, textFieldWidth, textFieldHeight);

        // Combo box for sex field
        this.sexComboBox = new JComboBox<>(new String[]{"All", "male", "female"});
        JLabel sexLabel = new JLabel("Sex:");
        sexLabel.setBounds(textFieldX, textFieldY += Constants.LABEL_HEIGHT + Constants.GAP,
                Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
        this.add(sexLabel);
        sexComboBox.setBounds(survivedLabel.getX() + Constants.LABEL_WIDTH, sexLabel.getY(),
                Constants.COMBO_BOX_WIDTH, Constants.COMBO_BOX_HEIGHT);
        this.add(sexComboBox);

        this.sibSpTextField = createTextField("SibSp:", textFieldX, textFieldY += Constants.OBJECT_MARGIN, textFieldWidth, textFieldHeight);
        this.parchTextField = createTextField("Parch:", textFieldX, textFieldY += Constants.OBJECT_MARGIN, textFieldWidth, textFieldHeight);
        this.ticketTextField = createTextField("Ticket:", textFieldX, textFieldY += Constants.OBJECT_MARGIN, textFieldWidth, textFieldHeight);
        this.minFareTextField = createTextField("Fare Min:", textFieldX, textFieldY += Constants.OBJECT_MARGIN, textFieldWidth, textFieldHeight);
        this.maxFareTextField = createTextField("Fare Max:", textFieldX, textFieldY += Constants.OBJECT_MARGIN, textFieldWidth, textFieldHeight);
        this.cabinTextField = createTextField("Cabin:", textFieldX, textFieldY += Constants.OBJECT_MARGIN, textFieldWidth, textFieldHeight);

        // Combo box for embarked field
        embarkedComboBox = new JComboBox<>(new String[]{"All", "S", "C", "Q"});
        JLabel embarkedLabel = new JLabel("Embarked:");
        embarkedLabel.setBounds(textFieldX, textFieldY += Constants.OBJECT_MARGIN,
                Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
        this.add(embarkedLabel);
        embarkedComboBox.setBounds(survivedLabel.getX() + Constants.LABEL_WIDTH, embarkedLabel.getY(),
                Constants.COMBO_BOX_WIDTH, Constants.COMBO_BOX_HEIGHT);
        this.add(embarkedComboBox);

        this.filter = new JButton("סינון");
        this.filter.setBounds(this.getWidth() / 3,textFieldY + Constants.OBJECT_MARGIN, Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
        this.filter.setVisible(true);
        this.add(filter);

        this.getStats = new JButton("צור סטטיסטיקה");
        this.getStats.setBounds(this.filter.getX(),this.filter.getY() + Constants.OBJECT_MARGIN, Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
        this.getStats.setVisible(true);
        this.add(getStats);

        this.result = new JTextArea();
        this.result.setBounds(textFieldX, this.getStats.getY()+Constants.OBJECT_MARGIN, Constants.WINDOW_WIDTH,this.getHeight()-this.getStats.getY()-Constants.OBJECT_MARGIN*2);
        this.result.setVisible(true);
        this.add(result);

        this.groupBy = new JButton("קבץ לפי");
        this.groupBy.setBounds(this.survivedComboBox.getX() + Constants.OBJECT_MARGIN*4, this.survivedComboBox.getY(), Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
        this.groupBy.setVisible(true);
        this.add(groupBy);

        groupByComboBox = new JComboBox<>(new String[]{"Class","Survived", "Sex", "Age", "Relatives","Fare","Embarked"});
        this.add(embarkedLabel);
        groupByComboBox.setBounds(groupBy.getX(), groupBy.getY()+Constants.COMBO_BOX_HEIGHT,
                Constants.LABEL_WIDTH, Constants.COMBO_BOX_HEIGHT);
        this.add(groupByComboBox);

        this.groupByArea = new JTextArea();
        this.groupByArea.setBounds(groupBy.getX(),groupByComboBox.getY()+groupByComboBox.getHeight(),groupBy.getWidth(),Constants.OBJECT_MARGIN*2);
        this.add(groupByArea);

    }

    private String generateFileName() {

        int pressCount = ++filterInstances; // Increment and use
        return pressCount + ".csv";
    }

    private void createCSVFile(String fileName) {
        File outputFile = new File(fileName);
        try (PrintWriter writer = new PrintWriter(outputFile)) {
            filteredList.sort(Comparator.comparing(Passenger::getFormattedName));

            writer.println("PassengerId,Survived,PClass,Name,Sex,Age,SibSp,Parch,Ticket,Fare,Cabin,Embarked");


            for (Passenger passenger : filteredList) {
                writer.println(passenger.getPassengerId() + "," +
                        passenger.getSurvived() + "," +
                        passenger.getPClass() + "," +
                        "\"" + passenger.getFormattedName() + "\"," + // Quote name to handle commas
                        passenger.getSex() + "," +
                        passenger.getAge() + "," +
                        passenger.getSibSp() + "," +
                        passenger.getParch() + "," +
                        passenger.getTicket() + "," +
                        passenger.getFare() + "," +
                        passenger.getCabin() + "," +
                        passenger.getEmbarked());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void createStatisticsFile() {
        File outputFile = new File("Statistics.txt");
        try (PrintWriter writer = new PrintWriter(outputFile)) {
            writer.write("התפלגות השורדים לפי מחלקה:\n");
            writeSurvivalRateByClass(writer);
            writer.write("התפלגות השורדים לפי מין:\n");
            writeSurvivalRateByGender(writer);
            writer.write("התפלגות השורדים לפי קבוצת גיל:\n");
            writeSurvivalRateByAgeGroup(writer);
            writer.write("התפלגות השורדים לפי קרובי משפחה:\n");
            writeSurvivalRateByFamily(writer);
            writer.write("התפלגות השורדים לפי עלות כרטיס:\n");
            writeSurvivalRateByTicketPrice(writer);
            writer.write("התפלגות השורדים לפי עלות הנמל ממנו יצאו:\n");
            writeSurvivalRateByEmbarkationPort(writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double calculateSurvivalRate(List<Passenger> passengers) {
        if (passengers.isEmpty()) {
            return 0;
        }
        long survivors = passengers.stream().filter(Passenger::isSurvived).count();
        return (double) survivors / passengers.size() * 100;
    }

    private void writeSurvivalRateByClass(PrintWriter writer) throws IOException {
        Map<Integer, List<Passenger>> passengersByClass = passengerList.stream()
                .collect(Collectors.groupingBy(Passenger::getPClass));

        for (Map.Entry<Integer, List<Passenger>> entry : passengersByClass.entrySet()) {
            Integer passengerClass = entry.getKey();
            List<Passenger> classPassengers = entry.getValue();
            double survivalRate = calculateSurvivalRate(classPassengers);
            writer.write("\n");
            writer.write(passengerClass + " – " + String.format("%.2f", survivalRate) + "%");
        }
        writer.write("\n\n");
    }

    private void writeSurvivalRateByGender(PrintWriter writer) throws IOException {
        Map<String, List<Passenger>> passengersByGender = passengerList.stream()
                .collect(Collectors.groupingBy(Passenger::getSex));

        for (Map.Entry<String, List<Passenger>> entry : passengersByGender.entrySet()) {
            String gender = entry.getKey();
            List<Passenger> genderPassengers = entry.getValue();
            double survivalRate = calculateSurvivalRate(genderPassengers);
            writer.write("\n");
            writer.write(gender + " – " + String.format("%.2f", survivalRate) + "%");
        }

        writer.write("\n\n");
    }

    private static String getAgeGroup(float age) {
        if (age <= 10) {
            return "0-10";
        } else if (age <= 20) {
            return "11-20";
        } else if (age <= 30) {
            return "21-30";
        } else if (age <= 40) {
            return "31-40";
        } else if (age <= 50) {
            return "41-50";
        } else {
            return "51+";
        }
    }

    private void writeSurvivalRateByAgeGroup(PrintWriter writer) throws IOException {
        Map<String, List<Passenger>> passengersByAgeGroup = new LinkedHashMap<>();
        passengersByAgeGroup.put("0-10", new ArrayList<>());
        passengersByAgeGroup.put("11-20", new ArrayList<>());
        passengersByAgeGroup.put("21-30", new ArrayList<>());
        passengersByAgeGroup.put("31-40", new ArrayList<>());
        passengersByAgeGroup.put("41-50", new ArrayList<>());
        passengersByAgeGroup.put("51+", new ArrayList<>());

        for (Passenger p : passengerList) {
            String ageGroup = getAgeGroup(p.getAge());
            passengersByAgeGroup.get(ageGroup).add(p);
        }

        for (Map.Entry<String, List<Passenger>> entry : passengersByAgeGroup.entrySet()) {
            String ageGroup = entry.getKey();
            List<Passenger> ageGroupPassengers = entry.getValue();
            double survivalRate = calculateSurvivalRate(ageGroupPassengers);
            writer.write(ageGroup + " – " + String.format("%.2f", survivalRate) + "%\n");
        }
        writer.write("\n");
    }

    private void writeSurvivalRateByFamily(PrintWriter writer) throws IOException {
        List<Passenger> withFamily = passengerList.stream()
                .filter(p -> p.getSibSp() + p.getParch() > 0)
                .collect(Collectors.toList());
        List<Passenger> withoutFamily = passengerList.stream()
                .filter(p -> p.getSibSp() + p.getParch() == 0)
                .collect(Collectors.toList());

        double withFamilySurvivalRate = calculateSurvivalRate(withFamily);
        double withoutFamilySurvivalRate = calculateSurvivalRate(withoutFamily);
        writer.write("\n");
        writer.write("עם קרובי משפחה – " + String.format("%.2f", withFamilySurvivalRate) + "%\n");
        writer.write("ללא קרובי משפחה – " + String.format("%.2f", withoutFamilySurvivalRate) + "%\n");
        writer.write("\n");
    }

    private void writeSurvivalRateByTicketPrice(PrintWriter writer) throws IOException {
        List<Passenger> lowPrice = passengerList.stream()
                .filter(p -> p.getFare() < 10)
                .collect(Collectors.toList());
        List<Passenger> midPrice = passengerList.stream()
                .filter(p -> p.getFare() >= 10 && p.getFare() <= 30)
                .collect(Collectors.toList());
        List<Passenger> highPrice = passengerList.stream()
                .filter(p -> p.getFare() > 30)
                .collect(Collectors.toList());

        double lowPriceSurvivalRate = calculateSurvivalRate(lowPrice);
        double midPriceSurvivalRate = calculateSurvivalRate(midPrice);
        double highPriceSurvivalRate = calculateSurvivalRate(highPrice);

        writer.write("\n");
        writer.write("פחות מ-10 פאונד – " + String.format("%.2f", lowPriceSurvivalRate) + "%\n");
        writer.write("11-30 פאונד – " + String.format("%.2f", midPriceSurvivalRate) + "%\n");
        writer.write("יותר מ-30 פאונד – " + String.format("%.2f", highPriceSurvivalRate) + "%\n");
        writer.write("\n");
    }

    private void writeSurvivalRateByEmbarkationPort(PrintWriter writer) throws IOException {
        Map<String, List<Passenger>> passengersByPort = passengerList.stream()
                .collect(Collectors.groupingBy(p -> Optional.ofNullable(p.getEmbarked()).orElse("Unknown")));

        for (Map.Entry<String, List<Passenger>> entry : passengersByPort.entrySet()) {
            String port = entry.getKey();
            List<Passenger> portPassengers = entry.getValue();
            double survivalRate = calculateSurvivalRate(portPassengers);
            writer.write(port + " – " + String.format("%.2f", survivalRate) + "%\n");
        }
    }

    private int convertTextToInteger(JTextField textField) {
        String text = textField.getText().trim();

        if (text.isEmpty()) {
            return -1;
        }

        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private float convertTextToFloat(JTextField textField) {
        String text = textField.getText().trim();

        if (text.isEmpty()) {
            return -1.0f;
        }

        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return -1.0f; 
        }
    }

}




