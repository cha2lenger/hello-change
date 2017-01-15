package org.hellochange.cash.change;

import org.hellochange.cash.Cash;

/**
 * Defines the solution of the change computation problem.
 */
public class Solution {
  /** Change computed for the intermediate solution. */
  private final Cash change;
  /** Cash remaining after change. */
  private final Cash remainingCash;
  
  /**
   * Constructor.
   * 
   * @param change - change cash object computed.
   * @param remainingCash - remaining cash.
   */
  public Solution(final Cash change, final Cash remainingCash) {
    this.change = change;
    this.remainingCash = remainingCash;
  }
  
  /**
   * Constructor for the zero step solution.
   * 
   * @param remainingCash - remaining cash.
   */
  public Solution(final Cash remainingCash) {
    this.change = Cash.EMPTY;
    this.remainingCash = remainingCash;
  }   
  
  /**
   * Gets the priority of the solution.
   * 
   * @return the priority of the solution. 
   */
  public int getPriority() {
    return this.change.getBillsAmount();
  }

  /**
   * Gets change computed.
   * 
   * @return change computed.
   */
  public Cash getChange() {
    return change;
  }

  /**
   * Gets remaining cash.
   * 
   * @return remaining cash.
   */
  public Cash getRemainingCash() {
    return remainingCash;
  }
}