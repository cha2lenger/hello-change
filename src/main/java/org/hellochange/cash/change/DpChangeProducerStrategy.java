/**
 * 
 */
package org.hellochange.cash.change;

import java.util.HashMap;
import java.util.Map;

import org.hellochange.cash.BillDenomination;
import org.hellochange.cash.Cash;
import org.hellochange.cash.NoSufficientFundsException;

/**
 * Dynamic programming bottom up approach for combining a change cash for the change amount requested. 
 * I'm not sure if greedy algorithm will work for limited number of bills - therefore I use fully-fledged dynamic programming approach.
 * 
 * @author vladimir
 */
public class DpChangeProducerStrategy implements ChangeProducerStrategy {

  /**
   * {@inheritDoc}
   */
  @Override
  public Solution computeChange(final Cash registerContents, final int changeAmount) {
    if (registerContents == null) {
      throw new IllegalArgumentException(" Null has been passed in as required parameter: registerContents");
    }
    if (changeAmount <= 0) {
      throw new IllegalArgumentException(String.format(" Zero or negative value has been passed in for changeAmount parameter: %1$d", changeAmount));
    }

    // check some margin scenarios first
    if (changeAmount > registerContents.getMoneyAmount()) {
      return null;
    }

    final Map<Integer, Solution> subProblemSolutions = new HashMap<>();
    for (int problemSize = 1; problemSize <= changeAmount; problemSize++) {
      
      Solution optimalSolution = null;
      
      for (BillDenomination nextDenomination : BillDenomination.values()) {
        final int subProblemSize = problemSize - nextDenomination.getDenomination();

        if (subProblemSize < 0) {
          continue;
        }

        if (subProblemSize == 0) {
          final Solution newSolution = nextSolution(new Solution(registerContents), nextDenomination);
          if (newSolution != null) {
            optimalSolution = newSolution;
          }
          continue;
        }

        final Solution subProblemSolution = subProblemSolutions.get(subProblemSize);
        if (subProblemSolution == null) {
          continue;
        }

        // add a bill to sub problem solution
        final Solution newSolution = nextSolution(subProblemSolution, nextDenomination);
        if (newSolution == null) {
          continue;
        }
        
        if ((optimalSolution == null) || (optimalSolution.getPriority() > newSolution.getPriority())) {
          optimalSolution = newSolution;
        }
      }
      
      if(optimalSolution != null) {
        subProblemSolutions.put(problemSize, optimalSolution);
      }
    }

    return subProblemSolutions.get(changeAmount);
  }
  
  /**
   * Tries to create the next solution for the bill denomination and sub problem solution passed in provided that we have enough cash available. 
   * If it is not possible due to a cash shortage - returns null.
   * 
   * @param subSolution - sub-problem solution to derive next solution from.
   * @param denomination - bill denomination to use to derive the new solution.
   * @return next solution.
   */
  private Solution nextSolution(Solution subSolution, final BillDenomination denomination) {
    try {
      final Cash cashAvail = subSolution.getRemainingCash();
      final Cash cashRemaining = cashAvail.subtractBills(denomination, 1);
      
      final Cash change = subSolution.getChange().addBills(denomination, 1);
      
      return new Solution(change, cashRemaining);
    } catch (NoSufficientFundsException nsfEx) {
      return null;
    }
  }
}