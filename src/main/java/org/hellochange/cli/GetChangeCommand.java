/**
 * 
 */
package org.hellochange.cli;

import org.hellochange.cash.Cash;
import org.hellochange.cash.CashRegister;
import org.hellochange.cli.proc.ArgsValidationException;
import org.hellochange.cli.proc.ParameterizedCliCommand;

/**
 * Change command command that extracts requested amount of change from the register.
 * 
 * @author vladimir
 *
 */
public class GetChangeCommand extends ParameterizedCliCommand<Integer> {
  /**
   * Cash register this command works with.
   */
  private final CashRegister cashRegister;

  /**
   * Constructor.
   * 
   * @param cashRegister - cash register.
   */
  public GetChangeCommand(final CashRegister cashRegister) {
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
    return "change";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Integer extractAndValidateArgs(String[] args) throws ArgsValidationException {
    if((args == null) || args.length != 1) {
      throw new ArgsValidationException(String.format(
          "Command [%1$s] expects non-negative integer argument as change amount.", getName())); 
    }    
    
    final Integer cashAmount;
    try {
      cashAmount = Integer.parseInt(args[0]);
    } catch (NumberFormatException nfEx) {
      throw new ArgsValidationException(String.format(
          "Command [%1$s] expects non-negative integer argument as change amount:" + " cannot parse [%2$s] to integer.", 
          getName(), args[0])); 
    }
    
    if(cashAmount < 0) {
      throw new ArgsValidationException(String.format(
          "Command [%1$s] expects non-negative integer argument as change amount:" + " argument [%2$s] is negative.", 
          getName(), args[0]));      
    }
    
    return cashAmount;
  }

  /**
   * {@inheritDoc}
   */  
  @Override
  protected boolean execute(Integer cashAmount) {
    final Cash changeCash = this.cashRegister.change(cashAmount);
    if (changeCash == null) {
      System.out.println("sorry");
    } else {
      System.out.println(changeCash.toString());
    }
    return true;
  }
}