/**
 * 
 */
package org.hellochange.cash;

/**
 * Represents supported bill denominations.
 * 
 * @author vladimir
 *
 */
public enum BillDenomination {
	ONE(1),
	TWO(2),
	FIVE(5),
	TEN(10),
	TWENTY(20);
	
	/** Actual denomination of the bill. */
	private final int denomination;
	
	/**
	 * Constructor.
	 * 
	 * @param denomination - actual denomination of the bill.
	 */
	private BillDenomination(int denomination) {
		this.denomination = denomination;
	}
	
	/**
	 * Gets bill denomination.
	 * 
	 * @return bill denomination.
	 */
	public int getDenomination() {
		return this.denomination;
	}
}