package GUI;

import data.api.GlobalCompany;
import data.api.LocalCompany;
import data.api.NetCompany;
import data.jsonParer.GlobalCurrency;
import data.jsonParer.NbpCurrency;
import data.jsonParer.NetCurrency;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;

import static java.lang.Thread.sleep;

public class App extends Application {
    private boolean canPrintData = false;
    private boolean restart = false;
    private boolean loading = false;
    private boolean dataUpdated = false;
    private final int width = 400;
    private final int height = 400;
    private final int interval = 5;
    private Thread taskThread;
    private Stage window;

    private final NbpCurrency nbpData = new NbpCurrency();
    private final LocalCompany nbp = new LocalCompany("NBP", nbpData);
    private final GlobalCurrency globalCurrencyData = new GlobalCurrency();
    private final GlobalCompany global = new GlobalCompany("Global Currency Rate", globalCurrencyData);
    private final NetCurrency netCurrencyData = new NetCurrency();
    private final NetCompany net = new NetCompany("Net Currency Rate", netCurrencyData);

    @Override
    public void init() {
        getCurrencySet();
        loadData();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setOpacity(0.95);
        window.setTitle("Currency converter");

        String buttonStyle = "-fx-font-size: 1em; ";
        String fillLabel = "Fill data.";
        String primaryText = "I have";
        String finalText = "I want";

        while (!dataUpdated)    // Waiting for all data
            sleep(interval);

        ComboBox<String> primaryCurrencyComboBox = initComboBox();
        ComboBox<String> finalCurrencyComboBox = initComboBox();

        primaryCurrencyComboBox.setPromptText(primaryText);
        finalCurrencyComboBox.setPromptText(finalText);

        /* Input field */
        TextField primaryCurrencyTextField = new TextField();
        primaryCurrencyTextField.setPromptText("Enter value");

        /* Buttons */
        Button buttonConvert = new Button("Convert");
        Button buttonRestart = new Button("Restart");
        Button buttonChart = new Button("Chart");
        Button buttonExit = new Button("Exit");

        buttonConvert.setStyle(buttonStyle);
        buttonRestart.setStyle(buttonStyle);
        buttonChart.setStyle(buttonStyle);
        buttonExit.setStyle(buttonStyle);

        buttonConvert.setOnAction(e -> {
            System.out.println("Converting...");
            boolean wrongInput = false;

            try{
                Double.parseDouble(primaryCurrencyTextField.getText());
            } catch (Exception exception) {
                primaryCurrencyTextField.setText("");
                primaryCurrencyTextField.setPromptText("Value have to be number");
                wrongInput = true;
            }

            if(primaryCurrencyComboBox.getValue() == null) {
                primaryCurrencyComboBox.setPromptText("Choose!!!");
            } else if(finalCurrencyComboBox.getValue() == null) {
                finalCurrencyComboBox.setPromptText("Choose!!!");
            } else {
                if(!wrongInput){
                    try {
                        nbp.update(primaryCurrencyTextField.getText(), primaryCurrencyComboBox.getValue(), finalCurrencyComboBox.getValue());
                        global.update(primaryCurrencyTextField.getText(), primaryCurrencyComboBox.getValue(), finalCurrencyComboBox.getValue());
                        net.update(primaryCurrencyTextField.getText(), primaryCurrencyComboBox.getValue(), finalCurrencyComboBox.getValue());
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    canPrintData = true;
                }
            }
        });

        buttonRestart.setOnAction(e -> {
            System.out.println("Restarting...");

            getCurrencySet();
            loadData();

            canPrintData = true;
            restart = true;

            primaryCurrencyTextField.clear();
            primaryCurrencyTextField.setPromptText("Enter value");

            primaryCurrencyComboBox.getSelectionModel().clearSelection();
            primaryCurrencyComboBox.setPromptText(primaryText);

            finalCurrencyComboBox.getSelectionModel().clearSelection();
            finalCurrencyComboBox.setPromptText(finalText);
        });

        buttonChart.setOnAction(e -> {

            Stage chartWindow = new Stage();
            chartWindow.initOwner(window);
            ChartApp chart = new ChartApp();
            try {
                if(finalCurrencyComboBox.getValue() == null)
                    finalCurrencyComboBox.setPromptText("Choose currency!!!");
                else
                    chart.start(chartWindow, finalCurrencyComboBox.getValue());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        buttonExit.setOnAction(e -> closeApp());

        HBox hboxComboBox = new HBox();
        HBox hbox = new HBox();

        hboxComboBox.getChildren().addAll(primaryCurrencyTextField, primaryCurrencyComboBox, finalCurrencyComboBox);
        hboxComboBox.setSpacing(10);
        hbox.getChildren().addAll(buttonConvert, buttonRestart, buttonChart, buttonExit);
        hbox.setSpacing(40);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(30,30,30,30));
        root.setBottom(hbox);
        root.setTop(hboxComboBox);
        root.setCenter(new Label(fillLabel));

        window.setScene(new Scene(root, width, height));
        window.show();

        taskThread = new Thread(() -> {
            while (true){
                check();

                Platform.runLater(() -> {
                    if (!loading && !restart)
                        root.setCenter(updateOutput());
                    else if (restart){
                        root.setCenter(new Label(fillLabel));
                        restart = false;
                    }
                    else if(loading) {
                        root.setCenter(new Label("Loading..."));
                        loading = false;
                    }
                });
            }
        });
        taskThread.start();
    }

    private void getCurrencySet() {
        Thread getCurrencySet =  new Thread(() -> {
            try{
                nbpData.update();
                dataUpdated = true;
            } catch (IllegalArgumentException | InterruptedException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        getCurrencySet.start();
    }

    private void loadData() {
        Thread loadData =  new Thread(() -> {
            try{
                globalCurrencyData.update();
                netCurrencyData.update();
                nbpData.updateHistData(LocalDate.now().plusMonths(-1).toString(), LocalDate.now().toString());
            } catch (IllegalArgumentException | InterruptedException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        loadData.start();
    }

    private ComboBox initComboBox() {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().add("PLN");

        for (NbpCurrency.Rate item : nbpData.root.rates)    // Write available currency to combo box
            comboBox.getItems().add(item.code);

        comboBox.setOnAction((event) -> {
            int selectedIndex = comboBox.getSelectionModel().getSelectedIndex();
            Object selectedItem = comboBox.getSelectionModel().getSelectedItem();
//            System.out.println("Selection made: [" + selectedIndex + "] " + selectedItem);
//            System.out.println("   ComboBox.getValue(): " + comboBox.getValue());
        });

        return comboBox;
    }

    private void check() {
        while (!canPrintData)   // waiting for changes
            try {
                sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        canPrintData = false;
    }

    private VBox updateOutput() {
        TableView tableView = new TableView();

        TableColumn<LocalCompany, String> column1 = new TableColumn<>("Company name");
        column1.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<LocalCompany, String> column2 = new TableColumn<>("Converted Value");
        column2.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);

        tableView.getItems().add(nbp);
        tableView.getItems().add(global);
        tableView.getItems().add(net);

        VBox vbox = new VBox(tableView);
        vbox.setMaxSize(width - 150,height - 150);

        return vbox;
    }

    private void closeApp() {
        System.out.println("Exit.");
        taskThread.stop();
        window.close();
    }

    @Override
    public void stop() {
        closeApp();
    }
}
