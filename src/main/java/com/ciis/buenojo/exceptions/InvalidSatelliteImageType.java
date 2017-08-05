package com.ciis.buenojo.exceptions;

import com.ciis.buenojo.domain.enumeration.SatelliteImageType;

/**
 * Exeption that indicate an attempt to parse a string into an invalid {@link SatelliteImageType}
 * @author franciscogindre
 *
 */
public class InvalidSatelliteImageType extends Exception {

	public InvalidSatelliteImageType() {
		// TODO Auto-generated constructor stub
	}

	public InvalidSatelliteImageType(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public InvalidSatelliteImageType(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidSatelliteImageType(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public InvalidSatelliteImageType(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
