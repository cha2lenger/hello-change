/**
 * 
 */
package org.hellochange.cli;

import org.hellochange.cash.Cash;
import org.hellochange.cash.CashRegister;
import org.hellochange.cash.NoSufficientFundsException;
import org.hellochange.cli.proc.CliCashCommand;

/**
 * Put command that removes cash from the cash register attached to it.
 * 
 * @author vladimir
 *
 */
public class TakeCashCommand extends CliCashCommand {
  /**
   * Cash register this command works with.
   */
  private final CashRegister cashRegister;

  /**
   * Constructor.
   * 
   * @param cashRegister - cash register.
   */
  public TakeCashCommand(final CashRegister cashRegister) {
    if( cashRegister == null) {
      throw new IllegalArgumentException("Null has been passed in as required parameter: cashRegister.");
    }
    
    this.cashRegister = cashRegister;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return "take";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean execute(Cash cash) {
    final Cash registerCash;
    try {
      registerCash = this.cashRegister.remove(cash);
      System.out.println(registerCash.toString());
    } catch (NoSufficientFundsException nsfEx) {
      System.out.println(String.format("Sorry: %1$s", nsfEx.getMessage()));
      System.out.println(this.cashRegister.getContents().toString());
    }

    return true;
  }
}