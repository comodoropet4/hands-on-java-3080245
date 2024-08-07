package bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataSource {

  public static Connection connect() {

    String db_file = "jdbc:sqlite:resources/bank.db";
    Connection connection = null;

    try {
      connection = DriverManager.getConnection(db_file);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return connection;
  }

  public static Customer getCustomer(String username) {
    String sql = "select * from customers where username = ?";
    Customer customer = null;

    try (Connection connection = connect();
        PreparedStatement statement = connection.prepareStatement(sql)) {

      statement.setString(1, username);
      try (ResultSet resultSet = statement.executeQuery()) {
        customer = new Customer(
            resultSet.getInt("id"),
            resultSet.getString("name"),
            resultSet.getString("username"),
            resultSet.getString("password"),
            resultSet.getInt("account_id"));
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return customer;
  }

  public static Account getAccount(int accountId) {
    String sqlAccountQuery = "SELECT * FROM accounts WHERE id = ?";
    Account retrievedAccount = null;

    try (
      Connection bankDatabaseConnection = connect();
      PreparedStatement accountQueryStatement = bankDatabaseConnection.prepareStatement(sqlAccountQuery);
    ) {
      accountQueryStatement.setInt(1, accountId);

      try(ResultSet accountResultSet = accountQueryStatement.executeQuery()) {
        retrievedAccount = new Account(
          accountResultSet.getInt("id"),
          accountResultSet.getString("type"),
          accountResultSet.getDouble("balance")
        );
      }
    } catch (SQLException exception) {
      exception.printStackTrace();
    }

    return retrievedAccount;
  }

  public static void main(String[] args) {
    Customer customer = getCustomer("twest8o@friendfeed.com");
    System.out.println(customer.getName());

    Account exampleAccount = getAccount(10385);
    System.out.println(exampleAccount.getBalance());
  }
}
