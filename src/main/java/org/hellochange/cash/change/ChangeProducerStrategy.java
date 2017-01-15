/**
 * 
 */
package org.hellochange.cash.change;

import org.hellochange.cash.Cash;

/**
 * Strategy interface for the change computation.
 * 
 * @author vladimir
 *
 */
public interface ChangeProducerStrategy {
  /**
   * For the register contents and change amount passed in computes the cash required to provide this change amount 
   * with minimal amount of bills possible and returns it as a solution. If it is not possible to allocate this cash - returns null
   * 
   * @param cashAvailable - cash available to produce the change amount requested.
   * @param changeAmount - amount of change to produce.
   * @return solution computed for the change or null if it is not possible to allocate appropriate amount of cash.
   */
  Solution computeChange(Cash cashAvailable, int changeAmount);
}