/**
 * 
 */
package org.hellochange.cash;

/**
 * Exception for the case when there is no enough bills of certain denomination in the register to remove.
 * 
 * @author vladimir
 *
 */
public class NotSufficientFundsException extends Exception {
	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = -1442379541359885348L;

	/**
	 * Denomination.
	 */
	private final BillDenomination denomination;
	
	/**
	 * Requested amount of bills to remove.
	 */
	private final int amountToRemove;
	
	/**
	 * Available amount of bills.
	 */
	private final int amountAvailable;
	
	/**
	 * 
	 * Constructor.
	 * 
	 * @param message - error message.
	 * @param denomination - bill denomination.
	 * @param amountToRemove - amount of bills requested for removal.
	 * @param amountAvail - amount of bills available.
	 */
	public NotSufficientFundsException(final String message, final BillDenomination denomination, final int amountToRemove, final int amountAvail) {
		super(message);
		this.denomination = denomination;
		this.amountToRemove = amountToRemove;
		this.amountAvailable = amountAvail;
	}

	/**
	 * Gets bill denomination.
	 * 
	 * @return bill denomination.
	 */
	public BillDenomination getDenomination() {
		return denomination;
	}

	/**
	 * Gets amount of bills to remove.
	 * 
	 * @return amount of bills to remove.
	 */
	public int getAmountToRemove() {
		return amountToRemove;
	}

	/**
	 * Gets amount of bills available in register.
	 * 
	 * @return amount of bills available in register.
	 */
	public int getAmountAvailable() {
		return amountAvailable;
	}
}