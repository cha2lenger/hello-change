/**
 * 
 */
package org.hellochange.cash;

import java.util.concurrent.atomic.AtomicReference;

import org.hellochange.cash.change.ChangeProducerStrategy;
import org.hellochange.cash.change.DpChangeProducerStrategy;
import org.hellochange.cash.change.Solution;

/**
 * Simple thread-safe implementation of cash register.
 * 
 * @author vladimir
 *
 */
public class SimpleCashRegister implements CashRegister {
  /** Mutable but protected state of the cash register. */
  private final AtomicReference<Cash> cashRef;
  /** Change producer strategy. */
  private final ChangeProducerStrategy changeProducer;

  /**
   * Default constructor which creates empty cash register.
   */
  public SimpleCashRegister() {
    this.cashRef = new AtomicReference<>(Cash.EMPTY);
    this.changeProducer = new DpChangeProducerStrategy();
  }

  /**
   * Constructor which creates cash register with the cash passed in as parameter.
   */
  public SimpleCashRegister(final Cash cash) {
    if (cash == null) {
      throw new IllegalArgumentException(" Null has been passed in as required parameter: cash");
    }

    this.cashRef = new AtomicReference<>(cash);
    this.changeProducer = new DpChangeProducerStrategy();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Cash getContents() {
    return this.cashRef.get();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Cash add(Cash cash) {
    Cash prev, next;
    do {
      prev = this.cashRef.get();
      next = prev.add(cash);
    } while (!this.cashRef.compareAndSet(prev, next));
    return next;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Cash remove(Cash cash) throws NoSufficientFundsException {
    Cash prev, next;
    do {
      prev = this.cashRef.get();
      next = prev.subtract(cash);
    } while (!this.cashRef.compareAndSet(prev, next));
    return next;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Cash change(Integer amount) {
    Cash prev, next, change;
    do {
      prev = this.cashRef.get();
      
      final Solution solution = this.changeProducer.computeChange(prev, amount); 
      next = solution.getRemainingCash();
      change = solution.getChange();
    } while (!this.cashRef.compareAndSet(prev, next));
    return change;
  }
}