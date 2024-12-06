package bank;

import javax.security.auth.login.LoginException;

public class Authenticator {
  public static Customer logIn(String customerUsername, String customerPassword) throws LoginException {
    Customer customerToLogIn = DataSource.getCustomer(customerUsername);

    if(customerToLogIn == null) {
      throw new LoginException("The username was not found.");
    }

    if(customerPassword.equals(customerToLogIn.getPassword())) {
      customerToLogIn.setAuthenticated(true);
      return customerToLogIn;
    } else {
      throw new LoginException("The password is incorrect.");
    }
  }

  public static void logOut(Customer customerToLogOut) {
    customerToLogOut.setAuthenticated(false);
  }
}
