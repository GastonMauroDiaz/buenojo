package com.ciis.buenojo.domain.util;

import java.lang.reflect.Array;
import java.util.Optional;
import java.util.Random;
import java.util.Collection;

public class BuenOjoRandomUtils {
	/**
	 * Taken From http://www.javamex.com/tutorials/random_numbers/random_sample.shtml
	 * we want to pick n random elements from a given list or array. For example, a quiz program might have 1000 
	 * possible questions and need to pick 20 of them. We would generally like each element to have an equal chance of being picked.
	 * @param population
	 * @param nSamplesNeeded
	 * @param r
	 * @return
	 */
	public static <T> T[] pickSample(T[] population, int nSamplesNeeded, Random r) {
		  @SuppressWarnings("unchecked")
		T[] ret = (T[]) Array.newInstance(population.getClass().getComponentType(),
		                                    nSamplesNeeded);
		  int nPicked = 0, i = 0, nLeft = population.length;
		  while (nSamplesNeeded > 0) {
		    int rand = r.nextInt(nLeft);
		    if (rand < nSamplesNeeded) {
		      ret[nPicked++] = population[i];
		      nSamplesNeeded--;
		    }
		    nLeft--;
		    i++;
		  }
		  return ret;
	}
	
	/**
	 * Get a random element from a Collection
	 * @param e
	 * @return an optional with the element
	 */
			
	public static <E> Optional<E> getRandom (Collection<E> e) {

	    return e.stream()
	            .skip((int) (e.size() * Math.random()))
	            .findFirst();
	}
}
