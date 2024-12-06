package bank;

import java.io.IOException;
import java.util.Scanner;

import javax.security.auth.login.LoginException;

import bank.exceptions.AmountException;

public class Menu {
  private static Menu bankUserMenu = new Menu();

  private Scanner inputScanner = null;

  public static void main(String[] arguments) {
    bankUserMenu.inputScanner = new Scanner(System.in);

    Customer loggedCustomer = bankUserMenu.authenticateCustomer();

    if (loggedCustomer != null) {
      Account loggedCustomerAccount = DataSource.getAccount(loggedCustomer.getAccountId());

      bankUserMenu.showUserMenu(loggedCustomer, loggedCustomerAccount);
    }

    bankUserMenu.inputScanner.close();
  }

  private Customer authenticateCustomer(){
    bankUserMenu.clearConsole();
    bankUserMenu.showHeader();

    System.out.print("Please enter your username: ");
    String customerUsername = inputScanner.next();

    System.out.print("Please enter your password: ");
    String customerPassword = inputScanner.next();

    Customer customerToAuthenticate = null;

    try {
      customerToAuthenticate = Authenticator.logIn(customerUsername, customerPassword);
    } catch (LoginException customerLogInException) {
      System.out.println("There was an error: " + customerLogInException.getMessage());
    }

    return customerToAuthenticate;
  }

  private void showUserMenu(Customer loggedCustomer, Account loggedCustomerAccount) {
    int menuOption = 0;

    while((menuOption != 4) && (loggedCustomer.isAuthenticated() == true)) {
      bankUserMenu.clearConsole();
      bankUserMenu.showHeader();

      System.out.println("\n===========================================");
      System.out.println("Please select one of the following options:\n");

      System.out.println("1- Deposit.");
      System.out.println("2- Withdraw.");
      System.out.println("3- Check balance.");
      System.out.println("4- Exit.");

      System.out.println("===========================================");

      menuOption = inputScanner.nextInt();

      bankUserMenu.clearConsole();
      bankUserMenu.showHeader();

      switch (menuOption) {
        case 1:
          depositFunds(loggedCustomerAccount);
          break;
        case 2:
          withdrawFunds(loggedCustomerAccount);
          break;
        case 3:
          checkBalance(loggedCustomerAccount);
          break;
        case 4:
          logOut(loggedCustomer);
          break;
        default:
          System.out.println("That's an invalid option.");
          break;
      }
    }
  }

  private void logOut(Customer loggedCustomer) {
    Authenticator.logOut(loggedCustomer);

    System.out.println("Thanks for banking at Globe Bank International!");
  }

  private void checkBalance(Account loggedCustomerAccount) {
    System.out.println("Your current amount is: " + loggedCustomerAccount.getBalance());

    System.out.print("Press any key to continue...");

    try{
      System.in.read();
    } catch (IOException keyException) {
      keyException.printStackTrace();
    }
  }

  private void withdrawFunds(Account loggedCustomerAccount) {
    double amount;
    System.out.print("How much would you like to withdraw?: ");
    amount = inputScanner.nextDouble();

    try {
      loggedCustomerAccount.withdraw(amount);

      System.out.println("Your amount has been withdrawn.\n");

      System.out.print("Press any key to continue...");
      System.in.read();
    } catch (AmountException withdrawalAmountException) {
      System.out.println(withdrawalAmountException.getMessage());
      System.out.println("Please try again.");
    } catch (IOException keyException) {
      keyException.printStackTrace();
    }
  }

  private void depositFunds(Account loggedCustomerAccount) {
    double amount;
    System.out.print("How much would you like to deposit?: ");
    amount = inputScanner.nextDouble();

    try {
      loggedCustomerAccount.deposit(amount);

      System.out.println("Your amount has been deposited.\n");

      System.out.print("Press any key to continue...");
      System.in.read();
    } catch (AmountException depositAmountException) {
      System.out.println(depositAmountException.getMessage());
      System.out.println("Please try again.");
    } catch (IOException keyException) {
      keyException.printStackTrace();
    }
  }

  private void showHeader() {
    System.out.println("Welcome to Globe Bank International.");
  }

  private void clearConsole() {
    for (int consoleLine = 0; consoleLine < 50; consoleLine++) {
      System.out.println();
    }
  }
}
