package bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataSource {
  public static Connection connectToDatabase() {
    String databaseFilePath = "jdbc:sqlite:resources/bank.db";

    Connection databaseConnection = null;

    try{
      databaseConnection = DriverManager.getConnection(databaseFilePath);
    } catch (SQLException databaseConnectionException) {
      databaseConnectionException.printStackTrace();
    }

    return databaseConnection;
  }

  public static Customer getCustomer(String customerUsername) {
    String sqlQuery = "SELECT * FROM customers WHERE username = ?";

    Customer retrievedCustomer = null;

    try (
      Connection databaseConnection = connectToDatabase();
      PreparedStatement customerPreparedStatement = databaseConnection.prepareStatement(sqlQuery);
    ) {
      customerPreparedStatement.setString(1, customerUsername);

      try (
        ResultSet customerResultSet = customerPreparedStatement.executeQuery()
      ) {
        retrievedCustomer = new Customer(
          customerResultSet.getInt("id"),
          customerResultSet.getString("name"),
          customerResultSet.getString("username"),
          customerResultSet.getString("password"),
          customerResultSet.getInt("account_id")
        );
      } catch (SQLException customerResultSetException) {
        customerResultSetException.printStackTrace();
      }
    } catch (SQLException databaseConnectionException) {
      databaseConnectionException.printStackTrace();
    }

    return retrievedCustomer;
  }

  public static Account getAccount(int accountId) {
    String sqlQuery = "SELECT * FROM accounts WHERE id = ?";

    Account retrievedAccount = null;

    try (
      Connection databaseConnection = connectToDatabase();
      PreparedStatement accountPreparedStatement = databaseConnection.prepareStatement(sqlQuery);
    ) {
      accountPreparedStatement.setInt(1, accountId);

      try (
        ResultSet accountResultSet = accountPreparedStatement.executeQuery();
      ) {
        retrievedAccount = new Account(
          accountResultSet.getInt("id"),
          accountResultSet.getString("type"),
          accountResultSet.getDouble("balance")
        );
      } catch (SQLException accountResultSetException) {
        accountResultSetException.printStackTrace();
      }
    } catch (SQLException databaseConnectionException) {
      databaseConnectionException.printStackTrace();
    }

    return retrievedAccount;
  }

  public static void updateAccountBalance(int accountId, double updatedBalance) {
    String sqlQuery = "UPDATE accounts SET balance = ? WHERE id = ?";

    try (
      Connection databaseConnection = connectToDatabase();
      PreparedStatement accountPreparedStatement = databaseConnection.prepareStatement(sqlQuery)
    ) {
      accountPreparedStatement.setDouble(1, updatedBalance);
      accountPreparedStatement.setInt(2, accountId);

      accountPreparedStatement.executeUpdate();

    } catch (SQLException databaseConnectionException) {
      databaseConnectionException.printStackTrace();
    }
  }
}
