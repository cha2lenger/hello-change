package org.hellochange.cash;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

/**
 * Cash class unit test.
 * 
 * @author vladimir
 *
 */
public class CashTest {

  /**
   * Tests EMPTY instance of Cash.
   */
  @Test
  public void testEmpty() {
    assertTrue(Cash.EMPTY.getContents().isEmpty());
    assertEquals(0, Cash.EMPTY.getMoneyAmount());
    assertEquals(0, Cash.EMPTY.getBillsAmount());
    
    Cash emptyCash = Cash.newInstance(Collections.emptyMap());
    
    assertEquals(emptyCash, Cash.EMPTY);
    assertTrue(emptyCash == Cash.EMPTY);
    
    emptyCash = Cash.newInstance(ImmutableMap.of(
        BillDenomination.ONE, 0,
        BillDenomination.TWO, 0,
        BillDenomination.FIVE, 0, 
        BillDenomination.TEN, 0, 
        BillDenomination.TWENTY, 0));
    
    assertEquals(emptyCash, Cash.EMPTY);
    assertTrue(emptyCash == Cash.EMPTY);

    for (final BillDenomination denomination : BillDenomination.values()) {
      final Cash nonEmptyCash1 = Cash.EMPTY.addBills(denomination, 3);
      
      assertFalse(nonEmptyCash1.getContents().isEmpty());
      assertEquals(3 * denomination.getDenomination(), nonEmptyCash1.getMoneyAmount());
      assertEquals(3, nonEmptyCash1.getBillsAmount());
    }
    
    final Cash cashToAdd = Cash.newInstance(ImmutableMap.of(
        BillDenomination.ONE, 8,
        BillDenomination.TWO, 2,
        BillDenomination.FIVE, 6,
        BillDenomination.TWENTY, 1));
    
      final Cash nonEmptyCash1 = Cash.EMPTY.add(cashToAdd);
      
      assertFalse(nonEmptyCash1.getContents().isEmpty());
      assertEquals(cashToAdd.getBillsAmount(), nonEmptyCash1.getBillsAmount());
      assertEquals(cashToAdd.getMoneyAmount(), nonEmptyCash1.getMoneyAmount());
  }
  
  /**
   * Tests constructing with negative bill numbers.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructionNegativeValues() {
    Cash.newInstance(ImmutableMap.of(
        BillDenomination.ONE, -4,
        BillDenomination.TWO, 0));
  }
  
  /**
   * Tests adding null cash.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddNull() {
    final Cash cash = Cash.newInstance(ImmutableMap.of(
        BillDenomination.ONE, 5,
        BillDenomination.TWO, 5));
    
    cash.add(null);
  }  
  
  /**
   * Tests subtracting null cash.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testSubtractNull() throws NoSufficientFundsException {
    final Cash cash = Cash.newInstance(ImmutableMap.of(
        BillDenomination.ONE, 5,
        BillDenomination.TWO, 5));
    
    cash.subtract(null);
  }
  
  /**
   * Tests adding null denomination.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddNullDenomination() {
    final Cash cash = Cash.newInstance(ImmutableMap.of(
        BillDenomination.ONE, 5,
        BillDenomination.TWO, 5));
    
    cash.addBills(null, 1);
  }      
  
  /**
   * Tests subtracting null denomination.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testSubtractNullDenomination() throws NoSufficientFundsException {
    final Cash cash = Cash.newInstance(ImmutableMap.of(
        BillDenomination.ONE, 5,
        BillDenomination.TWO, 5));
    
    cash.subtractBills(null, 1);
  }
  
  /**
   * Tests adding negative amount of bills.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddNegativeBills() {
    final Cash cash = Cash.newInstance(ImmutableMap.of(
        BillDenomination.ONE, 5,
        BillDenomination.TWO, 5));
    
    cash.addBills(BillDenomination.FIVE, -3);
  }
  
  /**
   * Tests subtracting negative amount of bills.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testSubtractNegativeBills() throws NoSufficientFundsException {
    final Cash cash = Cash.newInstance(ImmutableMap.of(
        BillDenomination.ONE, 5,
        BillDenomination.TWO, 5));
    
    cash.subtractBills(BillDenomination.TEN, -8);
  }  
  
  /**
   * Tests construction of non-empty Cash object.
   */
  @Test
  public void testNonEmptyConstruction() {
    final Cash cash = Cash.newInstance(ImmutableMap.of(
        BillDenomination.ONE, 12, 
        BillDenomination.FIVE, 6, 
        BillDenomination.TEN, 4, 
        BillDenomination.TWENTY, 2));
    
    assertFalse(cash.getContents().isEmpty());
    assertEquals(24, cash.getBillsAmount());
    assertEquals(122, cash.getMoneyAmount());
  }

  /**
   * Tests add/subtract operations.
   * 
   * @throws NoSufficientFundsException
   */
  @Test
  public void testAdditionSubtraction() throws NoSufficientFundsException {
    final Cash cash = Cash.newInstance(ImmutableMap.of(
        BillDenomination.ONE, 12, 
        BillDenomination.FIVE, 6, 
        BillDenomination.TEN, 4));   
    
    final Cash cashToAdd = Cash.newInstance(ImmutableMap.of(
        BillDenomination.ONE, 8,
        BillDenomination.FIVE, 6,
        BillDenomination.TWENTY, 1));
    
    final Cash addResult = cash.add(cashToAdd);
    assertFalse(addResult.getContents().isEmpty());
    assertEquals(cash.getBillsAmount() + cashToAdd.getBillsAmount(), addResult.getBillsAmount());
    assertEquals(cash.getMoneyAmount() + cashToAdd.getMoneyAmount(), addResult.getMoneyAmount());
    
    assertEquals(Integer.valueOf(20), addResult.getContents().get(BillDenomination.ONE));
    assertNull(addResult.getContents().get(BillDenomination.TWO));
    assertEquals(Integer.valueOf(12), addResult.getContents().get(BillDenomination.FIVE));
    assertEquals(Integer.valueOf(4), addResult.getContents().get(BillDenomination.TEN));
    assertEquals(Integer.valueOf(1), addResult.getContents().get(BillDenomination.TWENTY));
    
    assertEquals(cash, addResult.subtract(cashToAdd));
    assertEquals(cashToAdd, addResult.subtract(cash));
    
    assertEquals(Cash.EMPTY, cash.subtract(cash));
    assertEquals(cash, cash.add(Cash.EMPTY));
    assertEquals(cash, cash.subtract(Cash.EMPTY));
  }
  
  /**
   * Tests restricted subtraction when there is no enough cash available.
   * 
   * @throws NoSufficientFundsException
   */
  @Test(expected = NoSufficientFundsException.class)
  public void testRestrictedSubstraction() throws NoSufficientFundsException {
    final Cash cash = Cash.newInstance(ImmutableMap.of(
        BillDenomination.ONE, 12, 
        BillDenomination.FIVE, 6, 
        BillDenomination.TEN, 4));   
    
    final Cash cashToSubtract = Cash.newInstance(ImmutableMap.of(
        BillDenomination.ONE, 8,
        BillDenomination.FIVE, 6,
        BillDenomination.TWENTY, 1));
    
    cash.subtract(cashToSubtract);
  }
  
  /**
   * Tests add bills operation.
   * 
   * @throws NoSufficientFundsException
   */
  @Test
  public void testBillsAddition() {
    Cash cash = Cash.newInstance(ImmutableMap.of(
        BillDenomination.ONE, 2, 
        BillDenomination.TEN, 1));
    
    for (final BillDenomination nextDenomination : BillDenomination.values()) {
      Cash newCash = cash.addBills(nextDenomination, 3);
      assertEquals(cash.getBillsAmount() + 3, newCash.getBillsAmount());
      assertEquals(cash.getMoneyAmount() + 3 * nextDenomination.getDenomination(), newCash.getMoneyAmount());     
    } 
  }  
  
  /**
   * Tests subtract bills operations.
   * 
   * @throws NoSufficientFundsException
   */
  @Test
  public void testBillsSubtraction() throws NoSufficientFundsException {
    Cash cash = Cash.newInstance(ImmutableMap.of(
        BillDenomination.ONE, 2, 
        BillDenomination.TWO, 3, 
        BillDenomination.FIVE, 2, 
        BillDenomination.TEN, 4, 
        BillDenomination.TWENTY, 2));
    
    for (final BillDenomination nextDenomination : BillDenomination.values()) {
      Cash newCash = cash.subtractBills(nextDenomination, 2);
      assertEquals(cash.getBillsAmount() - 2, newCash.getBillsAmount());
      assertEquals(cash.getMoneyAmount() - 2 * nextDenomination.getDenomination(), newCash.getMoneyAmount());     
    }   
  }
  
  /**
   * Tests restricted bills subtraction when there is no enough cash available.
   * 
   * @throws NoSufficientFundsException
   */
  @Test(expected = NoSufficientFundsException.class)
  public void testRestrictedBillsSubstraction() throws NoSufficientFundsException {  
    Cash cash = Cash.newInstance(ImmutableMap.of(
        BillDenomination.ONE, 2, 
        BillDenomination.TEN, 1));
    
    cash.subtractBills(BillDenomination.ONE, 5);
  }
}