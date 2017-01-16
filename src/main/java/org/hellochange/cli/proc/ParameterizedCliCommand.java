/**
 * 
 */
package org.hellochange.cli.proc;

/**
 * Defines specific command line.
 * 
 * @param <ARGS> - arbitrary class that represents valid version of command arguments 
 * after they are successfully extracted from the command line.
 * 
 * @author vladimir
 * 
 */
public abstract class ParameterizedCliCommand<ARGS> implements CliCommand {
  
  /**
   * Extracts and validates command arguments from the raw array of them extracted from the command line.
   * 
   * @param args - raw array representation of the command line arguments.
   * @return valid command arguments extracted from the command line.
   * @throws ArgsValidationException - in case if validation of command arguments has failed.
   */
  protected abstract ARGS extractAndValidateArgs(String[] args) throws ArgsValidationException;
  
  /**
   * Executes the command for the passed in arguments previously extracted from the command line and validated.
   * 
   * @param arguments - parsed and valid arguments to use for the command call.
   * @return boolean flag which says whether the program has to continue execution.
   */
  protected abstract boolean execute(ARGS arguments);

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean execute(String[] args) {
    // 1: Extract and validate parameters first
    final ARGS arguments;
    try {
      arguments = extractAndValidateArgs(args);
    } catch (ArgsValidationException avEx) {
      System.out.println(avEx.getMessage());
      return true;
    }
    
    return execute(arguments);
  }
}