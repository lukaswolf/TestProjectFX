package pl.lukaszwilk.TestProjectFX.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.lukaszwilk.TestProjectFX.models.MySQLConnector;
import pl.lukaszwilk.TestProjectFX.models.UserSession;
import pl.lukaszwilk.TestProjectFX.models.Utils;
import pl.lukaszwilk.TestProjectFX.models.dao.ContactDao;
import pl.lukaszwilk.TestProjectFX.models.dao.impl.ContactDaoImpl;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    ListView<String> listView;

    @FXML
    TextField textName, textNumber,textFieldDIN,textFieldDNN;
    @FXML
    Button buttonLogOut ,buttonAdd,buttonDelete;


private ObservableList conntactsItem;
private UserSession session =UserSession.getInstance();
private ContactDao contactDao = new ContactDaoImpl();


    public void initialize(URL location, ResourceBundle resources) {

        textName.setEditable(false);
        textNumber.setEditable(false);

        loadContacts();
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            textName.setText(newValue);
            textNumber.setText(contactDao.getNumber(newValue));
        });
        buttonLogOut.setOnMouseClicked(event -> logout());
        buttonAdd.setOnMouseClicked(event -> add());
        buttonDelete.setOnMouseClicked(event -> delete());


       updateActions();



        
    }

    private void delete() {
        contactDao.removeContact(listView.getSelectionModel().getSelectedItem());
        Utils.createSimpleDialog("Usuwanie","","Usunieto");
        loadContacts();
    }

    private boolean checkAdd(){
        String textfield1 = textFieldDIN.getText();
        String textfield2 = textFieldDNN.getText();

        if (textfield1.isEmpty() || textfield2.isEmpty()){
            Utils.createSimpleDialog("Dodawanie","Error","Pola nie moga byc puste");
            return false;
        }
        if (textfield1.length()<=4 || textfield2.length()<=9){
            Utils.createSimpleDialog("Dodawanie","Error","Dane za krótkie");
            return false;
        }
        if (textfield1.equals(listView.toString())){
            Utils.createSimpleDialog("Dodawanie","Error","Nazwa zajeta");
            return false;
        }
        return true;
    }

    private void add() {
        if (!checkAdd()){
            return;
        }

        contactDao.addContact(textFieldDIN.getText(),textFieldDNN.getText());
        Utils.createSimpleDialog("Dodawanie","","Dodano");
        textFieldDIN.clear();
        textFieldDNN.clear();
        loadContacts();
    }




    private void updateActions(){
        textName.setOnMouseClicked(event -> {
            if (event.getClickCount()>=2){
                textName.setEditable(true);
            }
        });
        textNumber.setOnMouseClicked(event -> {
            if (event.getClickCount()>=2){
                textNumber.setEditable(true);
            }
        });
        textName.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue){
                    contactDao.editContact(textName.getText(),textNumber.getText(),listView.getSelectionModel().getSelectedItem());
                    loadContacts();
                    textName.setEditable(false);
                }

            }
        });
        textNumber.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue){
                    contactDao.editContact(textName.getText(),textNumber.getText(),listView.getSelectionModel().getSelectedItem());
                    loadContacts();
                    textNumber.setEditable(false);
                }
            }
        });
    }

    private void logout() {
        session.setLogin(false);
        session.setUsername(null);
        session.setId(0);

        Stage stage = (Stage) buttonLogOut.getScene().getWindow();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("loginView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(new Scene(root,600,400));
    }

    private void loadContacts() {
        conntactsItem = FXCollections.observableArrayList(contactDao.getAllContactNames(session.getUsername()));
        listView.setItems(conntactsItem);

    }
}
