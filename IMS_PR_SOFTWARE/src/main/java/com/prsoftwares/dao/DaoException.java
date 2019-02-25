package com.prsoftwares.dao;

import java.sql.SQLException;

public class DaoException extends RuntimeException{
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	public DaoException() {
		super(); // To change body of overridden methods use File | Settings |
					// File Templates.
	}
	public DaoException(Exception e) {
		super(); // To change body of overridden methods use File | Settings |
					// File Templates.
	}
	
	public DaoException(SQLException e) {
		super(); // To change body of overridden methods use File | Settings |
					// File Templates.
	}
	public DaoException(String s) {
		super(s); // To change body of overridden methods use File | Settings |
					// File Templates.
	}
	

	public DaoException(String s, Throwable throwable) {
		super(s, throwable); // To change body of overridden methods use File |
								// Settings | File Templates.
	}

	public DaoException(Throwable throwable) {
		super(throwable); // To change body of overridden methods use File |
							// Settings | File Templates.
	}
	
}
