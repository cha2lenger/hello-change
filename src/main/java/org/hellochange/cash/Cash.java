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
 * Represents cash as the immutable value object class with a combination of certain amount of bills of different denominations enclosed. Contains valid amount of bills of supported denominations.
 * 
 * @author vladimir
 */
public final class Cash {
  /** Special case of empty cash object. */
  public static final Cash EMPTY = new Cash();

  /** Contents. */
  private final Map<BillDenomination, Integer> contents;

  /** Total amount of money. */
  private final int moneyAmount;
  
  /** Total amount of bills. */
  private final int billsAmount;

  /**
   * Factory method.
   * 
   * @param contents - bills to create cash object for.
   * @return class instance.
   */
  public static Cash newInstance(final Map<BillDenomination, Integer> contents) {
    if (MapUtils.isEmpty(contents)) {
      return EMPTY;
    }

    final Map<BillDenomination, Integer> filteredContents = new HashMap<>();

    for (Map.Entry<BillDenomination, Integer> nextEntry : contents.entrySet()) {
      final int amount = nextEntry.getValue();
      final BillDenomination denomination = nextEntry.getKey();
      if (amount < 0) {
        throw new IllegalArgumentException(String.format("Negative amount of bills [%1$d] was passed in" + " as input parameter for denomination %2$s", amount, denomination));
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
   * Factory method. Returns instance of the class with the certain amount of bills put in.
   * 
   * @param billDenomination - bills denomination.
   * @param amount - amount of bills.
   * @return class instance.
   */
  public static Cash newInstance(final BillDenomination billDenomination, final int amount) {
    if(billDenomination == null) {
      throw new IllegalArgumentException("Null has been passed in as required parameter: billDenomination");
    }
    if(amount <= 0) {
      throw new IllegalArgumentException(String.format("Zero or negative value [%1$d] has been passed in as required parameter: amount", amount));
    }
    
    final Map<BillDenomination, Integer> resultContents = new HashMap<>();
    resultContents.put(billDenomination, amount);
    
    return new Cash(resultContents);
  }

  /**
   * Default constructor that produces empty cash object.
   */
  private Cash() {
    this.contents = Collections.emptyMap();
    this.moneyAmount = 0;
    this.billsAmount = 0;
  }

  /**
   * Constructor.
   * 
   * @param contents - bills to create cash object for.
   */
  private Cash(final Map<BillDenomination, Integer> contents) {
    this.contents = contents;
    this.billsAmount = this.contents.entrySet().stream().collect(Collectors.summingInt(
        entry -> entry.getValue()));
    this.moneyAmount = this.contents.entrySet().stream().collect(Collectors.summingInt(
        entry -> entry.getValue() * entry.getKey().getDenomination()));
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
  public int getMoneyAmount() {
    return this.moneyAmount;
  }
  
  /**
   * Get total amount of bills for this cash object.
   * 
   * @return total amount of bills for this cash object.
   */ 
  public int getBillsAmount() {
    return this.billsAmount;
  }
  
  /**
   * Adds passed in cash to this cash object and return the resulting cash object.
   * 
   * @param anotherCash - another cash object to add to this one.
   * @return - cash object produced as a result of addition.
   */
  public Cash add(final Cash anotherCash) {
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
   * Adds certain amount of bills to the cash object and returns the resulting cash object.
   * 
   * @param billDenomination - denomination of the bills to add.
   * @param amount - amount of bills to add.
   * @return cash object produced as a result of addition.
   */
  public Cash addBills(final BillDenomination billDenomination, final int amount) {
    if(billDenomination == null) {
      throw new IllegalArgumentException("Null has been passed in as required parameter: billDenomination");
    }
    if(amount < 0) {
      throw new IllegalArgumentException(String.format("Negative value [%1$d] has been passed in as required parameter: amount", amount));
    }
    
    final Map<BillDenomination, Integer> resultContents = new HashMap<>();
    resultContents.putAll(contents);
    
    final Integer origValue = resultContents.get(billDenomination);
    final int origValueToUse = origValue != null ? origValue : 0;
    final Integer newValue = origValueToUse + amount;
    
    resultContents.put(billDenomination, newValue);
    return new Cash(resultContents);
  }

  /**
   * Subtracts passed in cash from this cash object and return the resulting cash object.
   * 
   * @param anotherCash - another cash object to subtract from this one.
   * @return - cash object produced as a result of subtraction.
   * @throws NoSufficientFundsException - in case if subtraction operation can not be completed due to insufficient amount of bills of certain denomination in this cash object.
   */
  public Cash subtract(final Cash anotherCash) throws NoSufficientFundsException {
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

      if (thisAmountToUse < otherAmountToUse) {
        throw new NoSufficientFundsException("Not sufficient amount of bills to complete subtraction. ", nextDenomination, otherAmountToUse, thisAmountToUse);
      }

      if (thisAmountToUse == otherAmountToUse) {
        continue;
      }

      resultContents.put(nextDenomination, thisAmountToUse - otherAmountToUse);
    }

    return resultContents.isEmpty() ? EMPTY : new Cash(resultContents);
  }
  
  /**
   * Subtracts certain amount of bills from the cash object and returns the resulting cash object.
   * 
   * @param billDenomination - denomination of the bills to subtract.
   * @param amount - amount of bills to subtract.
   * @return cash object produced as a result of subtraction.
   */
  public Cash subtractBills(final BillDenomination billDenomination, final int amount) throws NoSufficientFundsException {
    if(billDenomination == null) {
      throw new IllegalArgumentException("Null has been passed in as required parameter: billDenomination");
    }
    if(amount < 0) {
      throw new IllegalArgumentException(String.format("Negative value [%1$d] has been passed in as required parameter: amount", amount));
    }
    
    final Map<BillDenomination, Integer> resultContents = new HashMap<>();
    resultContents.putAll(contents);
    
    final Integer origValue = resultContents.get(billDenomination);
    final int origValueToUse = origValue != null ? origValue : 0;
    
    if (origValueToUse < amount) {
      throw new NoSufficientFundsException("Not sufficient amount of bills to complete subtraction. ", billDenomination, amount, origValueToUse);
    }
    
    if (origValueToUse == amount) {
      resultContents.remove(billDenomination);
      return resultContents.isEmpty() ? EMPTY : new Cash(resultContents);
    }
    
    final Integer newValue = origValueToUse - amount;
    
    resultContents.put(billDenomination, newValue);
    return new Cash(resultContents);
  }  

  /**
   * {@inheritDoc}
   */
  @Override  
  public String toString() {
    final StringBuilder output = new StringBuilder();
    output.append(String.format("$%1$d", this.getMoneyAmount()));
    
    for (int i = 0; i < BillDenomination.denominationsReversed.size(); i++) {
      final Integer billsNumber = this.contents.get(BillDenomination.denominationsReversed.get(i));
      final int billsNumberToUse = billsNumber != null ? billsNumber : 0;
      output.append(String.format(" %1$d", billsNumberToUse));
    }
    
    return output.toString();
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
  public boolean equals(final Object obj) {
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