package bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataSource {

  public static Connection connect() {
    String databaseFile = "jdbc:sqlite:resources/bank.db";
    Connection bankDatabaseConnection = null;

    try {
      bankDatabaseConnection = DriverManager.getConnection(databaseFile);
      System.out.println("Connected!");
    } catch (SQLException exception) {
      exception.printStackTrace();
    }

    return bankDatabaseConnection;
  }

  public static Customer getCustomer(String customerUsername) {
    String sqlCustomerQuery = "SELECT * FROM customers WHERE username = ?";
    Customer retrievedCustomer = null;

    try (
      Connection bankDatabaseConnection = connect();
      PreparedStatement customerQueryStatement = bankDatabaseConnection.prepareStatement(sqlCustomerQuery)
    ) {
      customerQueryStatement.setString(1, customerUsername);

      try(ResultSet customerResultSet = customerQueryStatement.executeQuery()) {
        retrievedCustomer = new Customer(
          customerResultSet.getInt("id"),
          customerResultSet.getString("name"),
          customerResultSet.getString("username"),
          customerResultSet.getString("password"),
          customerResultSet.getInt("account_id")
        );
      }
    } catch (SQLException exception) {
      exception.printStackTrace();
    }

    return retrievedCustomer;
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
    Customer exampleCustomer = getCustomer("twest8o@friendfeed.com");
    System.out.println(exampleCustomer.getName());

    int customerAccountId = exampleCustomer.getAccountId();

    Account exampleAccount = getAccount(customerAccountId);
    System.out.println(exampleAccount.getBalance());
  }
}
