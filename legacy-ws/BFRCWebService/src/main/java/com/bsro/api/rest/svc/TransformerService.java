package com.bsro.api.rest.svc;

import java.util.List;

/**
 * Helper for transforming data from once class to another
 * @author Brad Balmer
 *
 * @param <T>
 * @param <O>
 */
public interface TransformerService<T, O> {

	public T transform(O o);
	public List<T> transform(List<O> o);
}
