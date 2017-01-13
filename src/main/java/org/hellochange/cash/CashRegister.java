/**
 * 
 */
package org.hellochange.cash;

/**
 * Defines the API of a Cash Register.
 * 
 * @author vladimir
 *
 */
public interface CashRegister {
	/**
	 * Gets the current state of the register.
	 * 
	 * @return gets all the cash available in the register.
	 */
	Cash getContents();
	
	/**
	 * Adds passed in cash to the register and returns it resulting state.
	 * 
	 * @param cash - cash to add to the register.
	 * @return the resulting state of the register.
	 */
	Cash add(Cash cash);
	
	/**
	 * Removes passed in cash from the register and returns it resulting state.
	 * 
	 * @param cash - cash to remove from the register.
	 * @return the resulting state of the register.
	 * @throws NotSufficientFundsException - in case if there is no sufficient amount of bills in the register to complete remove request.
	 */	
	Cash remove(Cash cash) throws NotSufficientFundsException;
	
	/**
	 * For the change amount passed in computes, removes from register and returns corresponding Cash.
	 * 	
	 * @param amount - change amount.
	 * @return removed cash from register that corresponds to the change amount.
	 * @throws CannotProduceChangeException - in case if there is no sufficient amount of bills in the register to provide the change requested.
	 */
	Cash change(Integer amount) throws CannotProduceChangeException;
}