/**
 * 
 */
package org.hellochange.cli.proc;

/**
 * Defines command line (CLI) command API.
 * 
 * @author vladimir
 *
 */
public interface CliCommand {
  /** Gets name of the command which is used to invoke it. */
  String getName();
  
  /**
   * Executes the command for the passed in I/O parameters.
   * 
   * @param arguments passed together with the command.
   * @return boolean flag which says whether the program has to continue execution.
   */
  boolean execute(String[] args);  
}