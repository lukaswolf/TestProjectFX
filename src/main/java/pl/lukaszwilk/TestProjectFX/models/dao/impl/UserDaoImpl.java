package pl.lukaszwilk.TestProjectFX.models.dao.impl;

import pl.lukaszwilk.TestProjectFX.models.MySQLConnector;
import pl.lukaszwilk.TestProjectFX.models.UserSession;
import pl.lukaszwilk.TestProjectFX.models.Utils;
import pl.lukaszwilk.TestProjectFX.models.dao.UserDao;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao{
    private MySQLConnector connector = MySQLConnector.getInstance();
    private UserSession session =UserSession.getInstance();



    public boolean login(String name, String password) {
        try {
            java.sql.PreparedStatement preparedStatement = connector.getConnection().prepareStatement(
                    "SELECT * FROM user WHERE username =? "
            );
            preparedStatement.setString(1,name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()){
                return false;
            }
            session.setId(resultSet.getInt("id"));
            return resultSet.getString("password").equals(Utils.shaHash(password));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean register(String name, String password) {
        try {

            java.sql.PreparedStatement preparedStatement = connector.getConnection().prepareStatement(
                    "SELECT * FROM user WHERE username =? "
            );
            preparedStatement.setString(1,name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return false;
            }


            java.sql.PreparedStatement preparedStatementInsert = connector.getConnection().prepareStatement(
                    "INSERT INTO user VALUES(?,?,?)"
            );
            preparedStatementInsert.setInt(1,0);
            preparedStatementInsert.setString(2,name);
            preparedStatementInsert.setString(3, Utils.shaHash(password));
            preparedStatementInsert.execute();
            preparedStatement.close();
            preparedStatementInsert.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void removeUser(int id) {
        try {
            java.sql.PreparedStatement preparedStatement = connector.getConnection().prepareStatement(
                    "DELETE  FROM user WHERE id =?"
            );
            preparedStatement.setInt(1,id);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
