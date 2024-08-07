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
      System.out.println("Connected!");
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return connection;
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

  public static void main(String[] args){
    Customer exampleCustomer = getCustomer("twest8o@friendfeed.com");
    System.out.println(exampleCustomer.getName());
  }
}
