/**
 * 
 */
package org.hellochange.cli;

import org.hellochange.cash.CashRegister;
import org.hellochange.cli.proc.CliCommand;

/**
 * Show command that prints out the contents of the cash register attached to it.
 * 
 * @author vladimir
 *
 */
public class ShowCommand implements CliCommand {
  /**
   * Cash register this command operates with.
   */
  private final CashRegister cashRegister;
  
  /**
   * Constructor.
   * 
   * @param cashRegister - cash register.
   */
  public ShowCommand(final CashRegister cashRegister) {
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
    return "show";
  }

  @Override
  public boolean execute(String[] args) {
    System.out.println(this.cashRegister.getContents().toString());
    return true;
  }
}