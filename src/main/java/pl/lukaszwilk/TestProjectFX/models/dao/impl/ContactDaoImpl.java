package pl.lukaszwilk.TestProjectFX.models.dao.impl;

import pl.lukaszwilk.TestProjectFX.models.MySQLConnector;
import pl.lukaszwilk.TestProjectFX.models.UserSession;
import pl.lukaszwilk.TestProjectFX.models.dao.ContactDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactDaoImpl implements ContactDao {

    private MySQLConnector mySQLConnector = MySQLConnector.getInstance();
    private UserSession userSession = UserSession.getInstance();
    @Override
    public List<String> getAllContactNames(String username) {
        List<String> nameList = new ArrayList<>();
        try {
        PreparedStatement preparedStatement = mySQLConnector.getConnection().prepareStatement(
                "SELECT name FROM conntact INNER JOIN user ON user.id = conntact.user WHERE user.username = ?"
        );

            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                nameList.add(resultSet.getString("name"));
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nameList;
    }

    @Override
    public String getNumber(String contact) {
        try {
            PreparedStatement preparedStatement = mySQLConnector.getConnection().prepareStatement(
                    "SELECT number FROM conntact WHERE name = ?"
            );

            preparedStatement.setString(1, contact);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return resultSet.getString("number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public boolean addContact(String name, String number) {
        try {
            PreparedStatement statement = mySQLConnector.getConnection().prepareStatement(
                    "INSERT  INTO conntact    VALUES  (?,?,?,?)   "
            );
            statement.setInt(1,0);
            statement.setString(2,name);
            statement.setString(3,number);
            statement.setInt(4,userSession.getId());
            statement.setString(5,name);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void removeContact(String name) {
        try {
            PreparedStatement statement = mySQLConnector.getConnection().prepareStatement(
                    "DELETE FROM conntact WHERE name = ?"
            );
            statement.setString(1,name);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean editContact(String newName, String number,String oldName) {
        try {
            PreparedStatement statement = mySQLConnector.getConnection().prepareStatement(
                    "UPDATE conntact SET number = ?, name = ? WHERE name = ?"
            );
            statement.setString(1, number);
            statement.setString(2, newName);
            statement.setString(3, oldName);

            statement.execute();
            statement.close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
