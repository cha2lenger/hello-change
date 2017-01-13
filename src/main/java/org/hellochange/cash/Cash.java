/**
 * 
 */
package org.hellochange.cash;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MapUtils;

/**
 * Represents cash as the immutable value object class with a combination of certain amount of bills of different denominations enclosed.
 * Contains valid amount of bills of supported denominations.
 * 
 * @author vladimir
 *
 */
public final class Cash {
	/** Special case of empty cash object. */
	public static final Cash EMPTY = new Cash();
	
	/** Contents. */
	private final Map<BillDenomination, Integer> contents;
	
	/** Total amount of money. */
	private final long amount;
	
	/**
	 * Factory method.
	 * 
	 * @param contents - bills to create cash object for.
	 */
	public static Cash newInstance(final Map<BillDenomination, Integer> contents) {		
		if (MapUtils.isEmpty(contents)) {
			return EMPTY;
		}
		
		final Map<BillDenomination, Integer> filteredContents = new HashMap<>();
		
		for (Map.Entry<BillDenomination, Integer> nextEntry: contents.entrySet()) {
			final int amount = nextEntry.getValue();
			final BillDenomination denomination = nextEntry.getKey();
			if(amount <= 0) {
				throw new IllegalArgumentException(String.format(
						" Negative amount of bills [%1$d] was passed in" + " as input parameter for denomination %2$s", 
						amount, denomination));
			}
			
			if (amount == 0) {
				continue;
			}
			
			filteredContents.put(denomination, amount);
		}
		
		if (MapUtils.isEmpty(filteredContents)) {
			return EMPTY;
		}				
		
		return new Cash(filteredContents);
	}

	/** 
	 * Default constructor that produces empty cash object.
	 */
	private Cash() {
		this.contents = Collections.emptyMap();
		this.amount = 0l;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param contents - bills to create cash object for.
	 */
	private Cash(final Map<BillDenomination, Integer> contents) {
		this.contents = contents;
		this.amount = this.contents.entrySet().stream().collect(Collectors.summingLong(entry -> entry.getValue().longValue()));
	}
	
	/**
	 * Gets the actual bill denominations and their amount for this cash object.
	 * 
	 * @return the actual bill denominations and their amount for this cash object. 
	 */
	public Map<BillDenomination, Integer> getContents() {
		return Collections.unmodifiableMap(this.contents);
	}
	
	/**
	 * Get total amount of dollars for this cash object.
	 * 
	 * @return total amount of dollars for this cash object.
	 */
	public long getAmount() {
		return this.amount;
	}
	
	/**
	 * Adds passed in cash to this cash object and return the resulting cash object.
	 * 
	 * @param anotherCash - another cash object to add to this one.
	 * @return - cash object produced as a result of addition.
	 */
	public Cash add(Cash anotherCash) {
		if (anotherCash == null) {
			throw new IllegalArgumentException("Null has been passed in as required parameter: anotherCash");
		}
		
		final Map<BillDenomination, Integer> resultContents = new HashMap<>();
		for (BillDenomination nextDenomination : BillDenomination.values()) {
			final Integer thisAmount = this.contents.get(nextDenomination);
			final Integer otherAmount = anotherCash.contents.get(nextDenomination);
			
			if ((thisAmount == null) && (otherAmount == null)) {
				continue;
			}
			
			final int thisAmountToUse = thisAmount != null ? thisAmount : 0;
			final int otherAmountToUse = otherAmount != null ? otherAmount : 0;
			
			resultContents.put(nextDenomination, thisAmountToUse + otherAmountToUse);
		}
		
		return new Cash(resultContents);
	}
	
	/**
	 * Subtracts passed in cash from this cash object and return the resulting cash object.
	 * 
	 * @param anotherCash - another cash object to subtract from this one.
	 * @return - cash object produced as a result of subtraction.
	 * @throws NotSufficientFundsException - in case if subtraction operation can not be completed 
	 * due to insufficient amount of bills of certain denomination in this cash object.
	 */	
	public Cash subtract(Cash anotherCash) throws NotSufficientFundsException {
		if (anotherCash == null) {
			throw new IllegalArgumentException("Null has been passed in as required parameter: anotherCash");
		}
		
		final Map<BillDenomination, Integer> resultContents = new HashMap<>();
		for (BillDenomination nextDenomination : BillDenomination.values()) {
			final Integer thisAmount = this.contents.get(nextDenomination);
			final Integer otherAmount = anotherCash.contents.get(nextDenomination);
			
			if ((thisAmount == null) && (otherAmount == null)) {
				continue;
			}
			
			final int thisAmountToUse = thisAmount != null ? thisAmount : 0;
			final int otherAmountToUse = otherAmount != null ? otherAmount : 0;
			
			if(thisAmountToUse == otherAmountToUse) {
				continue;
			}
			
			if(thisAmountToUse < otherAmountToUse) {
				throw new NotSufficientFundsException(" Not sufficient amount of bills to complete subtraction. ", nextDenomination, otherAmountToUse, thisAmountToUse);
			}
			
			resultContents.put(nextDenomination, thisAmountToUse - otherAmountToUse);
		}
		
		return resultContents.isEmpty() ? EMPTY : new Cash(resultContents);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + contents.hashCode();
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Cash)) {
			return false;
		}
		Cash other = (Cash) obj;
		if (!contents.equals(other.contents)) {
			return false;
		}
		return true;
	}
}