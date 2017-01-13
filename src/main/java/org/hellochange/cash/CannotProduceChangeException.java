/**
 * 
 */
package org.hellochange.cash;

/**
 * Exception for the case when the register cannot produce the change amount requested.
 * 
 * @author vladimir
 *
 */
public class CannotProduceChangeException extends Exception {
	/**
	 * Generated serial version ID. 
	 */
	private static final long serialVersionUID = 1315523342618248491L;
	
	/** Change amount requested. */
	private final int changeAmount;

	/**
	 * Constructor.
	 * 
	 * @param message - error message.
	 * @param changeAmnt - change amount requested.
	 */
	public CannotProduceChangeException(final String message, final int changeAmnt) {
		super(message);
		this.changeAmount = changeAmnt;
	}

	/**
	 * Gets change amount requested.
	 * 
	 * @return change amount requested.
	 */
	public int getChangeAmount() {
		return changeAmount;
	}
}