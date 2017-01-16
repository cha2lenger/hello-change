/**
 * 
 */
package org.hellochange.cli.proc;

/**
 * Exception for the case when validation filed for the command line command arguments.
 * 
 * @author vladimir
 *
 */
public class ArgsValidationException extends Exception {

  /**
   * Generated serial version ID.
   */
  private static final long serialVersionUID = 6007520386278670757L;

  /**
   * Constructor.
   * 
   * @param message - error message.
   */
  public ArgsValidationException(String message) {
    super(message);
  }
}