/**
 * 
 */
package org.hellochange;

import java.io.IOException;

import org.hellochange.cash.CashRegister;
import org.hellochange.cash.SimpleCashRegister;
import org.hellochange.cli.GetChangeCommand;
import org.hellochange.cli.PutCashCommand;
import org.hellochange.cli.QuitCommand;
import org.hellochange.cli.ShowCommand;
import org.hellochange.cli.TakeCashCommand;
import org.hellochange.cli.proc.CliProcessor;

/**
 * Main class to start Hello Change program.
 * 
 * @author vladimir
 *
 */
public class Main {

  /**
   * Program starting point.
   * 
   * @param args - command line arguments if any.
   */
  public static void main(String[] args) throws IOException {
    // 1: Create empty cash register
    final CashRegister cashRegister = new SimpleCashRegister();
    
    // 2: setup CLI interface
    final CliProcessor cli = new CliProcessor(
        QuitCommand.INSTANCE, 
        new ShowCommand(cashRegister),
        new GetChangeCommand(cashRegister),
        new PutCashCommand(cashRegister),
        new TakeCashCommand(cashRegister));
    
    // 3: run CLI
    cli.run();
  }
}