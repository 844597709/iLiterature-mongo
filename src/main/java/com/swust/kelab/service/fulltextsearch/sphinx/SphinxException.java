/*
 * $Id$
 */

package com.swust.kelab.service.fulltextsearch.sphinx;

/** Exception thrown on attempts to pass invalid arguments to Sphinx API methods. */
public class SphinxException extends Exception
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Trivial constructor. */
	public SphinxException()
	{
	}

	/** Constructor from error message string. */
	public SphinxException ( String message )
	{
		super ( message );
	}
}

/*
 * $Id$
 */
