/**
 * 
 */
package org.hellochange.cash;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Simple thread-safe implementation of cash register.
 * 
 * @author vladimir
 *
 */
public class SimpleCashRegister implements CashRegister {
	/** Mutable but protected state of the cash register. */
	private final AtomicReference<Cash> cashRef;
	
	/**
	 * Default constructor which creates empty cash register.
	 */
	public SimpleCashRegister() {
		this.cashRef = new AtomicReference<>(Cash.EMPTY);
	}

	/**
	 * Constructor which creates cash register with the cash passed in as parameter.
	 */
	public SimpleCashRegister(final Cash cash) {
		if (cash == null) {
			throw new IllegalArgumentException(" Null has been passed in as required parameter: cash");
		}
		
		this.cashRef = new AtomicReference<>(cash);
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
	public Cash remove(Cash cash) throws NotSufficientFundsException {
        Cash prev, next;
        do {
            prev = this.cashRef.get();
            next = prev.subtract(cash);
        } while (!this.cashRef.compareAndSet(prev, next));
        return next;
	}

	/* (non-Javadoc)
	 * @see org.hellochange.cash.CashRegister#change(java.lang.Integer)
	 */
	@Override
	public Cash change(Integer amount) throws CannotProduceChangeException {
		// TODO Auto-generated method stub
		return null;
	}

}
