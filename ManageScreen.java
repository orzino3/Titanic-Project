/*
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManageScreen extends JPanel {

    private List<Passenger> passengerList;
    private JComboBox<String> survivedComboBox;



    public ManageScreen(int x, int y, int width, int height) {
        passengerList = new ArrayList<>();
        File file = new File(Constants.PATH_TO_DATA_FILE); //this is the path to the data file
        if (file.exists()) {
            this.setLayout(null);
            this.setBounds(x, y + Constants.MARGIN_FROM_TOP, width, height);
            JLabel survivedLabel = new JLabel("Passenger Class: ");
            survivedLabel.setBounds(x + Constants.MARGIN_FROM_LEFT, y, Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            JTextField minPassengerId = new JTextField(20);
            minPassengerId.setBounds(x + Constants.MARGIN_FROM_LEFT, survivedLabel.getHeight()+ y, Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(minPassengerId);

            this.add(survivedLabel);
            this.survivedComboBox = new JComboBox<>(Constants.PASSENGER_CLASS_OPTIONS);
            this.survivedComboBox.setBounds(survivedLabel.getX() + survivedLabel.getWidth() + 1, survivedLabel.getY(), Constants.COMBO_BOX_WIDTH, Constants.COMBO_BOX_HEIGHT);
            this.add(this.survivedComboBox);
            try {
                Scanner scanner = new Scanner(file);
                scanner.nextLine();
                while (scanner.hasNextLine()){
                    String info = scanner.nextLine();
                    Passenger passenger = new Passenger(info);
                    passengerList.add(passenger);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            this.survivedComboBox.addActionListener((e) -> {
                //do whatever you want on change
            });
        }
    }

}
*/

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ManageScreen extends JPanel {

    private List<Passenger> passengerList;
    private JComboBox<String> classComboBox;
    private JTextField minPassengerIdField;
    private JTextField maxPassengerIdField;
    private JTextField nameField;
    private JComboBox<String> sexComboBox;
    private JTextField sibSpField;
    private JTextField parchField;
    private JTextField ticketField;
    private JTextField minFareField;
    private JTextField maxFareField;
    private JTextField cabinField;
    private JComboBox<String> embarkedComboBox;
    private JButton filterButton;
    private JLabel resultLabel;

    public ManageScreen(int x, int y, int width, int height) {
        passengerList = new ArrayList<>();
        File file = new File(Constants.PATH_TO_DATA_FILE);
        if (file.exists()) {
            this.setLayout(null);
            this.setBounds(x, y + Constants.MARGIN_FROM_TOP, width, height);

            JLabel classLabel = new JLabel("Passenger Class: ");
            classLabel.setBounds(x + Constants.MARGIN_FROM_LEFT, y, Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(classLabel);
            classComboBox = new JComboBox<>(Constants.PASSENGER_CLASS_OPTIONS);
            classComboBox.setBounds(classLabel.getX() + classLabel.getWidth() + 1, classLabel.getY(), Constants.COMBO_BOX_WIDTH, Constants.COMBO_BOX_HEIGHT);
            this.add(classComboBox);

            JLabel minPassengerIdLabel = new JLabel("Min Passenger ID: ");
            minPassengerIdLabel.setBounds(x + Constants.MARGIN_FROM_LEFT, y + 40, Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(minPassengerIdLabel);
            minPassengerIdField = new JTextField(20);
            minPassengerIdField.setBounds(minPassengerIdLabel.getX() + minPassengerIdLabel.getWidth() + 1, minPassengerIdLabel.getY(), Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(minPassengerIdField);

            JLabel maxPassengerIdLabel = new JLabel("Max Passenger ID: ");
            maxPassengerIdLabel.setBounds(x + Constants.MARGIN_FROM_LEFT, y + 80, Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(maxPassengerIdLabel);
            maxPassengerIdField = new JTextField(20);
            maxPassengerIdField.setBounds(maxPassengerIdLabel.getX() + maxPassengerIdLabel.getWidth() + 1, maxPassengerIdLabel.getY(), Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(maxPassengerIdField);

            JLabel nameLabel = new JLabel("Name: ");
            nameLabel.setBounds(x + Constants.MARGIN_FROM_LEFT, y + 120, Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(nameLabel);
            nameField = new JTextField(20);
            nameField.setBounds(nameLabel.getX() + nameLabel.getWidth() + 1, nameLabel.getY(), Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(nameField);

            JLabel sexLabel = new JLabel("Sex: ");
            sexLabel.setBounds(x + Constants.MARGIN_FROM_LEFT, y + 160, Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(sexLabel);
            sexComboBox = new JComboBox<>(new String[]{"All", "male", "female"});
            sexComboBox.setBounds(sexLabel.getX() + sexLabel.getWidth() + 1, sexLabel.getY(), Constants.COMBO_BOX_WIDTH, Constants.COMBO_BOX_HEIGHT);
            this.add(sexComboBox);

            JLabel sibSpLabel = new JLabel("SibSp: ");
            sibSpLabel.setBounds(x + Constants.MARGIN_FROM_LEFT, y + 200, Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(sibSpLabel);
            sibSpField = new JTextField(20);
            sibSpField.setBounds(sibSpLabel.getX() + sibSpLabel.getWidth() + 1, sibSpLabel.getY(), Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(sibSpField);

            JLabel parchLabel = new JLabel("Parch: ");
            parchLabel.setBounds(x + Constants.MARGIN_FROM_LEFT, y + 240, Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(parchLabel);
            parchField = new JTextField(20);
            parchField.setBounds(parchLabel.getX() + parchLabel.getWidth() + 1, parchLabel.getY(), Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(parchField);

            JLabel ticketLabel = new JLabel("Ticket: ");
            ticketLabel.setBounds(x + Constants.MARGIN_FROM_LEFT, y + 280, Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(ticketLabel);
            ticketField = new JTextField(20);
            ticketField.setBounds(ticketLabel.getX() + ticketLabel.getWidth() + 1, ticketLabel.getY(), Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(ticketField);

            JLabel minFareLabel = new JLabel("Min Fare: ");
            minFareLabel.setBounds(x + Constants.MARGIN_FROM_LEFT, y + 320, Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(minFareLabel);
            minFareField = new JTextField(20);
            minFareField.setBounds(minFareLabel.getX() + minFareLabel.getWidth() + 1, minFareLabel.getY(), Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(minFareField);

            JLabel maxFareLabel = new JLabel("Max Fare: ");
            maxFareLabel.setBounds(x + Constants.MARGIN_FROM_LEFT, y + 360, Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(maxFareLabel);
            maxFareField = new JTextField(20);
            maxFareField.setBounds(maxFareLabel.getX() + maxFareLabel.getWidth() + 1, maxFareLabel.getY(), Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(maxFareField);

            JLabel cabinLabel = new JLabel("Cabin: ");
            cabinLabel.setBounds(x + Constants.MARGIN_FROM_LEFT, y + 400, Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(cabinLabel);
            cabinField = new JTextField(20);
            cabinField.setBounds(cabinLabel.getX() + cabinLabel.getWidth() + 1, cabinLabel.getY(), Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(cabinField);

            JLabel embarkedLabel = new JLabel("Embarked: ");
            embarkedLabel.setBounds(x + Constants.MARGIN_FROM_LEFT, y + 440, Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(embarkedLabel);
            embarkedComboBox = new JComboBox<>(new String[]{"All", "C", "Q", "S"});
            embarkedComboBox.setBounds(embarkedLabel.getX() + embarkedLabel.getWidth() + 1, embarkedLabel.getY(), Constants.COMBO_BOX_WIDTH, Constants.COMBO_BOX_HEIGHT);
            this.add(embarkedComboBox);

            filterButton = new JButton("Filter");
            filterButton.setBounds(x + Constants.MARGIN_FROM_LEFT, y + 480, Constants.LABEL_WIDTH, Constants.LABEL_HEIGHT);
            this.add(filterButton);

            resultLabel = new JLabel();
            resultLabel.setBounds(x + Constants.MARGIN_FROM_LEFT, y + 520, Constants.LABEL_WIDTH * 3, Constants.LABEL_HEIGHT * 2);
            this.add(resultLabel);

            try {
                Scanner scanner = new Scanner(file);
                scanner.nextLine();
                while (scanner.hasNextLine()) {
                    String info = scanner.nextLine();
                    Passenger passenger = new Passenger(info);
                    passengerList.add(passenger);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            filterButton.addActionListener(e -> filterPassengers());
        }
    }

    private void filterPassengers() {
        int minPassengerId = minPassengerIdField.getText().isEmpty() ? Integer.MIN_VALUE : Integer.parseInt(minPassengerIdField.getText());
        int maxPassengerId = maxPassengerIdField.getText().isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(maxPassengerIdField.getText());
        String name = nameField.getText();
        String sex = sexComboBox.getSelectedItem().toString();
        int sibSp = sibSpField.getText().isEmpty() ? Integer.MIN_VALUE : Integer.parseInt(sibSpField.getText());
        int parch = parchField.getText().isEmpty() ? Integer.MIN_VALUE : Integer.parseInt(parchField.getText());
        String ticket = ticketField.getText();
        float minFare = minFareField.getText().isEmpty() ? Float.MIN_VALUE : Float.parseFloat(minFareField.getText());
        float maxFare = maxFareField.getText().isEmpty() ? Float.MAX_VALUE : Float.parseFloat(maxFareField.getText());
        String cabin = cabinField.getText();
        String embarked = embarkedComboBox.getSelectedItem().toString();
        int pClass = classComboBox.getSelectedItem().equals("All") ? -1 : Integer.parseInt(classComboBox.getSelectedItem().toString().split(" ")[0]);

        List<Passenger> filteredList = passengerList.stream()
                .filter(p -> (minPassengerId <= p.getPassengerId() && p.getPassengerId() <= maxPassengerId))
                .filter(p -> (name.isEmpty() || p.getName().contains(name)))
                .filter(p -> (sex.equals("All") || p.getSex().equals(sex)))
                .filter(p -> (sibSp == Integer.MIN_VALUE || p.getSibSp() == sibSp))
                .filter(p -> (parch == Integer.MIN_VALUE || p.getParch() == parch))
                .filter(p -> (ticket.isEmpty() || p.getTicket().equals(ticket)))
                .filter(p -> (minFare <= p.getFare() && p.getFare() <= maxFare))
                .filter(p -> (cabin.isEmpty() || p.getCabin().contains(cabin)))
                .filter(p -> (embarked.equals("All") || p.getEmbarked() == embarked.charAt(0)))
                .filter(p -> (pClass == -1 || p.getPClass() == pClass))
                .collect(Collectors.toList());

        resultLabel.setText("Found " + filteredList.size() + " passengers");
    }
}

