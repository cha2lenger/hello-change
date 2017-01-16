package org.hellochange.cash.change;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hellochange.cash.BillDenomination;
import org.hellochange.cash.Cash;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Tests change computation strategy.
 * 
 * @author vorlov
 *
 */
public class ChangeProducerStrategyTest {
  /** Strategy to test. */
  private final ChangeProducerStrategy strategy = new DpChangeProducerStrategy();
  
  /**
   * Tests the case when null is passed in as available cash.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNullAvailCash() {
    this.strategy.computeChange(null, 15);
  }
  
  /**
   * Tests the case when negative value is passed in as change amount.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNegativeCash() {
    this.strategy.computeChange(Cash.EMPTY, -16);
  } 

  /**
   * Tests the case when zero is passed in as change amount.
   */
  @Test
  public void testZeroCash() {
    final Cash availCash = Cash.newInstance(ImmutableMap.of(BillDenomination.FIVE, 5, BillDenomination.ONE, 3));
    
    final Solution solution = this.strategy.computeChange(availCash, 0);
    assertNotNull(solution);
    
    final Cash change = solution.getChange();
    final Cash remainingCash = solution.getRemainingCash();  
    
    assertEquals(Cash.EMPTY, change);
    assertEquals(availCash, remainingCash);
    assertEquals(availCash, remainingCash.add(change));
  }  
  
  /**
   * Tests change computation for multiples of five.
   */
  @Test
  public void testEveryMultOfFiveChangeAmount() {
    final Cash availCash = Cash.newInstance(ImmutableMap.of(BillDenomination.FIVE, 5));

    // available solutions
    for (int i = 5; i <= 25; i += 5) {
      final Solution solution = strategy.computeChange(availCash, i);
      assertNotNull(solution);
      
      final Cash change = solution.getChange();
      assertEquals(i / 5, change.getBillsAmount());
      assertEquals(i, change.getMoneyAmount());
      
      final Cash remainingCash = solution.getRemainingCash();
      assertEquals(availCash, remainingCash.add(change));
    }

    // ...but not in case if we exceed available amount
    Solution solution = strategy.computeChange(availCash, 30);
      assertNull(solution);


    // solution is NOT available for non-multiple-of-5 amounts
    for (int i = 1; i < 30; i += 2) {
      if (i%5 == 0) {
        continue;
      }
      solution = strategy.computeChange(availCash, i);
      assertNull(solution);
    }
  }

  /**
   * Tests the case when change is available for event change amounts only.
   */
  @Test
  public void testEvenChangeAmount() {
    final Cash availCash = Cash.newInstance(ImmutableMap.of(BillDenomination.TWO, 4, BillDenomination.TEN, 1));

    // solution is available for even amounts
    for (int i = 2; i < 18; i += 2) {
      final Solution solution = strategy.computeChange(availCash, i);
      assertNotNull(solution);
      
      final Cash change = solution.getChange();
      assertEquals(i, change.getMoneyAmount());
      
      final Cash remainingCash = solution.getRemainingCash();
      assertEquals(availCash, remainingCash.add(change));
    }

    // ...but not in case if we exceed available amount
    Solution solution = strategy.computeChange(availCash, 20);
    assertNull(solution);

    // solution is NOT available for odd amounts
    for (int i = 1; i < 20; i += 2) {
      solution = strategy.computeChange(availCash, i);
      assertNull(solution);
    }
  }
  
  /**
   * Tests a few five dollar bills plus single one dollar bill. 
   */
  @Test
  public void testChangeAmountForFivesAndOne() {
    final Cash availCash = Cash.newInstance(ImmutableMap.of(BillDenomination.FIVE, 3, BillDenomination.ONE, 1));
    final List<Integer> changeAvailCases = ImmutableList.of(1, 5, 6, 10, 11, 15, 16);
    
    for (int amount = 1; amount<= 20; amount++) {
      final Solution solution = strategy.computeChange(availCash, amount);
      
      if(changeAvailCases.contains(amount)) {
        assertNotNull(solution);
        
        final Cash change = solution.getChange();
        assertEquals(amount, change.getMoneyAmount());
        
        final Cash remainingCash = solution.getRemainingCash();
        assertEquals(availCash, remainingCash.add(change));
      } else {
        assertNull(solution);
      }
    }
  }
  
  /**
   * Tests with only tens and enough ones to populate gaps between tens.
   */
  @Test
  public void testWithTenAndOnes() {
    final Cash availCash = Cash.newInstance(ImmutableMap.of(BillDenomination.TEN, 10, BillDenomination.ONE, 9));
    
    for (int amount = 1; amount < 110; amount ++) {
      final Solution solution = strategy.computeChange(availCash, amount);
      
      assertNotNull(solution);
      
      final Cash change = solution.getChange();
      final Cash remainingCash = solution.getRemainingCash();
      
      final int tens = amount / 10;
      final int ones = amount % 10;
      
      final Cash expectedChange = Cash.newInstance(ImmutableMap.of(BillDenomination.ONE, ones, BillDenomination.TEN, tens));
      
      assertEquals(expectedChange, change);
      assertEquals(availCash, change.add(remainingCash));
    }
    
    assertNull(strategy.computeChange(availCash, 110));
  }
  
  /**
   * Tests selecting 8 dollars of change out of 13 dollars of cash available.
   */
  @Test
  public void testEightOutOfThirteen() {
    final Cash cashAvail1 = Cash.newInstance(ImmutableMap.of(
        BillDenomination.TEN, 1, BillDenomination.TWO, 1, BillDenomination.ONE, 1));
    final Cash cashAvail2 = Cash.newInstance(ImmutableMap.of(
        BillDenomination.TEN, 1, BillDenomination.ONE, 3));
    final Cash cashAvail3 = Cash.newInstance(ImmutableMap.of(
        BillDenomination.FIVE, 2, BillDenomination.TWO, 1, BillDenomination.ONE, 1));
    final Cash cashAvail4 = Cash.newInstance(ImmutableMap.of(
        BillDenomination.FIVE, 2, BillDenomination.ONE, 3));
    final Cash cashAvail5 = Cash.newInstance(ImmutableMap.of(
        BillDenomination.FIVE, 1, BillDenomination.TWO, 4));
    final Cash cashAvail6 = Cash.newInstance(ImmutableMap.of(
        BillDenomination.FIVE, 1, BillDenomination.TWO, 3, BillDenomination.ONE, 2));
    final Cash cashAvail7 = Cash.newInstance(ImmutableMap.of(
        BillDenomination.FIVE, 1, BillDenomination.TWO, 2, BillDenomination.ONE, 4));
    final Cash cashAvail8 = Cash.newInstance(ImmutableMap.of(
        BillDenomination.FIVE, 1, BillDenomination.TWO, 1, BillDenomination.ONE, 6));
    final Cash cashAvail9 = Cash.newInstance(ImmutableMap.of(
        BillDenomination.FIVE, 1, BillDenomination.ONE, 8));
    // skip the rest of the cases
    
    final List<Cash> allCases = ImmutableList.of(
        cashAvail1, cashAvail2, cashAvail3, cashAvail4, cashAvail5, 
        cashAvail6, cashAvail7, cashAvail8, cashAvail9);
  
    final List<Cash> treatableCases = ImmutableList.of(
        cashAvail3, cashAvail4, cashAvail5, cashAvail6, cashAvail7, cashAvail8, cashAvail9);
    
    final List<Cash> untreatableCases = ImmutableList.of(
        cashAvail1, cashAvail2);  
    
    for (final Cash nextCash : allCases) {
      assertEquals(13, nextCash.getMoneyAmount());
    }
    
    for (final Cash nextCash : untreatableCases ) {
      assertNull(this.strategy.computeChange(nextCash, 8));
    }
    
    for (final Cash nextCash : treatableCases ) {
      final Solution solution = this.strategy.computeChange(nextCash, 8);
      
      assertNotNull(solution);
      
      final Cash change = solution.getChange();
      final Cash remainingCash = solution.getRemainingCash();  
      
      assertEquals(8, change.getMoneyAmount());
      assertEquals(5, remainingCash.getMoneyAmount());
      
      assertEquals(nextCash, remainingCash.add(change));
    }    
  }
  
  /**
   * Test when all bills are available.
   */
  @Test
  public void testWithAllBillsAvailable() {
    final Cash availCash = Cash.newInstance(ImmutableMap.of(
        BillDenomination.ONE, 1024,
        BillDenomination.TWO, 1024,
        BillDenomination.FIVE, 1024, 
        BillDenomination.TEN, 1024, 
        BillDenomination.TWENTY, 1024));
    
    for (int amount = 1; amount <= 1024; amount++) {
      final Cash changeExpected = getLeastPossibleAmountOfBillsFor(amount);
      final Solution solution = this.strategy.computeChange(availCash, amount);
      
      assertNotNull(solution);
      
      final Cash change = solution.getChange();
      final Cash remainingCash = solution.getRemainingCash();
      
      assertEquals(changeExpected, change);
      assertEquals(availCash, change.add(remainingCash));
    }
  }
  
  /**
   * Helper method: gets a cash object with the least possible amount of bills needed for the amount passed in.
   * 
   * @param amount - amount of money to prepare cash object for.
   * @return cash object computed.
   */
  private Cash getLeastPossibleAmountOfBillsFor(final int amount) {
    final Map<BillDenomination, Integer> contents = new HashMap<>();
    
    final BillDenomination[] denominations = BillDenomination.values();
    int remainingAmount = amount;
    for (int i = (denominations.length - 1); (i >=0) && (remainingAmount > 0) ; i--) {
      final BillDenomination denomination = denominations[i];
      
      final int billsAmount =  remainingAmount / denomination.getDenomination();
      remainingAmount = remainingAmount % denomination.getDenomination();
      
      contents.put(denomination, billsAmount);
    }
    
    return Cash.newInstance(contents);
  }
}