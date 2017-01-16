/**
 * 
 */
package org.hellochange.cli;

import org.hellochange.cash.Cash;
import org.hellochange.cash.CashRegister;
import org.hellochange.cli.proc.CliCashCommand;

/**
 * Put command that adds cash to the cash register attached to it.
 * 
 * @author vladimir
 *
 */
public class PutCashCommand extends CliCashCommand {
  /**
   * Cash register this command works with.
   */
  private final CashRegister cashRegister;

  /**
   * Constructor.
   * 
   * @param cashRegister - cash register.
   */
  public PutCashCommand(final CashRegister cashRegister) {
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
    return "put";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean execute(final Cash cash) {
    final Cash registerCash = this.cashRegister.add(cash);
    System.out.println(registerCash.toString());
    return true;
  }
}