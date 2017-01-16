/**
 * 
 */
package org.hellochange.cli;

import org.hellochange.cli.proc.CliCommand;

/**
 * CLI command for program termination.
 * 
 * @author vladimir
 *
 */
public final class QuitCommand implements CliCommand {
  /** The only instance of the command available. */
  public static final QuitCommand INSTANCE = new QuitCommand(); 
  
  /**
   * Hidden constructor.
   */
  private QuitCommand() {
    // nothing to do here
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName() {
    return "quit";
  }

  /**
   * {@inheritDoc} 
   */
  @Override
  public boolean execute(String[] args) {
    // make sure program is terminated
    System.out.println("Bye");
    return false;
  }
}