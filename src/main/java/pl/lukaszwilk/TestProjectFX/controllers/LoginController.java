package pl.lukaszwilk.TestProjectFX.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.lukaszwilk.TestProjectFX.models.UserSession;
import pl.lukaszwilk.TestProjectFX.models.Utils;
import pl.lukaszwilk.TestProjectFX.models.dao.UserDao;
import pl.lukaszwilk.TestProjectFX.models.dao.impl.UserDaoImpl;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController  implements Initializable {
    @FXML
    TextField loginL, nameR;

    @FXML
    PasswordField passwordL, passwordR, passwordRR;

    @FXML
    Button buttonL, buttonRR;

    private UserSession userSession = UserSession.getInstance();
    private UserDao userDao = new UserDaoImpl();

    public void initialize(URL location, ResourceBundle resources) {
        buttonL.setOnMouseClicked(event -> tryLogin());
        buttonRR.setOnMouseClicked(event -> tryLoginR());
    }

    private void tryLoginR() {
        String name = nameR.getText();
        String password = passwordR.getText();
        if (!checkRegisterData()){
            return;
        }


        if (userDao.register(name,password)){
            Utils.createSimpleDialog("Rejestracja",""," zarejestrowano");
        } else {
            Utils.createSimpleDialog("Rejestracja","","Nie zarejestrowano");
        }
        }
    private boolean checkRegisterData(){
        String name = nameR.getText();
        String password = passwordR.getText();
        String passwordRepeat = passwordRR.getText();

        if (name.isEmpty() || password.isEmpty() || passwordRepeat.isEmpty()){
            Utils.createSimpleDialog("Rejestracja","Popraw","Pola nie moga byc puste");
            return false;
        }
        if (!password.equals(passwordRepeat)){
            Utils.createSimpleDialog("Rejestracja","Popraw","Hasła sie nie zgadzaja");
            return false;
        }
        if (name.length()<=3 || password.length()<=5 || passwordRepeat.length()<5){
            Utils.createSimpleDialog("Rejestracja","Popraw","Za krótkie");
            return false;
        }
        return true;
    }


    private void tryLogin() {
        String login = loginL.getText();
        String password = passwordL.getText();
        if (!checkLoginData()){
            return;
        }

        if (userDao.login(login, password)) {

            userSession.setUsername(login);
            userSession.setLogin(true);

            try {
                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("mainView.fxml"));
                Stage stageRoot =(Stage) buttonL.getScene().getWindow();
                stageRoot.setScene(new Scene(root,600,400));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
           Utils.createSimpleDialog("Logowanie","","Nie zalogowao");
        }
    }
    private boolean checkLoginData(){
        String login = loginL.getText();
        String password = passwordL.getText();

        if (login.isEmpty() || password.isEmpty()){
        Utils.createSimpleDialog("Logowanie","Popraw","Pola nie moga byc puste");
        return false;
    }
        if (login.length()<=3 || password.length()<=5){
        Utils.createSimpleDialog("Logowanie","Popraw","Za krótkie dane");
         return false;
    }
return true;
         }
}
