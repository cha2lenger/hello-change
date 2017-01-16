/**
 * 
 */
package org.hellochange.cli.proc;

import java.util.HashMap;
import java.util.Map;

import org.hellochange.cash.BillDenomination;
import org.hellochange.cash.Cash;

/**
 * Abstract implementation of {@link ParameterizedCliCommand} which expects Cash to be passed in as a set of arguments.
 * 
 * @author vladimir
 *
 */
public abstract class CliCashCommand extends ParameterizedCliCommand<Cash> {
  /**
   * {@inheritDoc}
   */
  @Override
  protected Cash extractAndValidateArgs(String[] args) throws ArgsValidationException {
    if((args == null) || args.length != BillDenomination.denominationsReversed.size()) {
      throw new ArgsValidationException(String.format(
          "Command [%1$s] expects %2$d non-negative integer arguments as cash definition.", 
          getName(), BillDenomination.denominationsReversed.size())); 
    }
    
    final Map<BillDenomination, Integer> contents = new HashMap<>();

    for (int i = 0; i < BillDenomination.denominationsReversed.size(); i++) {
      final BillDenomination denomination = BillDenomination.denominationsReversed.get(i);
      final int amount;
      try {
        amount = Integer.parseInt(args[i]);
      } catch (NumberFormatException nfEx) {
        throw new ArgsValidationException(String.format(
            "Command [%1$s] expects %3$d non-negative integer arguments" + 
            " as cash definition: cannot parse [%2$s] to integer.", getName(), args[i], BillDenomination.denominationsReversed.size()));
      }
      
      if (amount < 0) {
        throw new ArgsValidationException(String.format(
            "Command [%1$s] expects %3$d non-negative integer arguments" + 
            " as cash definition: argument [%2$s] is negative.", getName(), args[i], BillDenomination.denominationsReversed.size()));        
      }
      
      contents.put(denomination, amount);
    }
    
    return Cash.newInstance(contents);
  }
}