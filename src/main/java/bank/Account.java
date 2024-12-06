package bank;

import bank.exceptions.AmountException;

public class Account {
  private int id;
  private String type;
  private double balance;

  public Account(int id, String type, double balance) {
    this.id = id;
    this.type = type;
    this.balance = balance;
  }

  public int getId() {
    return this.id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public double getBalance() {
    return this.balance;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }

  public void deposit(double depositAmount) throws AmountException{
    if (depositAmount < 1) {
      throw new AmountException("The minimum deposit is $1.00.");
    } else {
      double newBalance = balance + depositAmount;
      setBalance(newBalance);

      DataSource.updateAccountBalance(id, balance);
    }
  }

  public void withdraw(double withdrawalAmount) throws AmountException {
    if (withdrawalAmount < 1) {
      throw new AmountException("The minimum withdrawal is $1.00.");
    } else {
      double currentBalance = getBalance();

      if (withdrawalAmount > currentBalance) {
        throw new AmountException("You don't have sufficient funds for this withdrawal.");
      } else if (withdrawalAmount <= currentBalance) {
        double newBalance = balance - withdrawalAmount;
        setBalance(newBalance);

        DataSource.updateAccountBalance(id, balance);
      }
    }
  }
}
