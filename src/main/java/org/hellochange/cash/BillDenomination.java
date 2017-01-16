/**
 * 
 */
package org.hellochange.cash;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * Represents supported bill denominations.
 * 
 * @author vladimir
 *
 */
public enum BillDenomination {
  ONE(1), TWO(2), FIVE(5), TEN(10), TWENTY(20);

  /** Actual denomination of the bill. */
  private final int denomination;
  
  /** Bill denominations in  their reversed order */
  public final static List<BillDenomination> denominationsReversed = Lists.reverse(Arrays.asList(values()));

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