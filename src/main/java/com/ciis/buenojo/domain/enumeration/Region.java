package com.ciis.buenojo.domain.enumeration;

public enum Region {
	 Norte, Sur, Medio, General;
	 
	 
	 
	 /**
	  * Returns the upper latitude threshold for this region
	  * @return Double.NaN if  General Double.POSITIVE_INFINITY if error
	  */
	 public Double getUpperLatitudeThreshold() {
	 	switch (this) {
	 	case Norte:
	 		return -40.666666666667;
	 		
	 	case Medio:
	 		return -44.75;
	 	case Sur:
	 		return -55.0;
	 	case General:
	 		return Double.NaN;
	 		
	 	}	
	 	return Double.POSITIVE_INFINITY;
	 }
	 /**
	  * Returns the lower latitude threshold for this region
	  * @return Double.NaN if General Double.POSITIVE_INFINITY if error
	  */
	 public Double getLowerLatitudeThreshold() {
		switch (this) {
		case Norte:
			return -36.0;
		case Medio:
			return -40.666666666667;
		case Sur:
			return -45.75;
		case General:
			return Double.NaN;
		}
		return Double.POSITIVE_INFINITY;
	 }
	 
	 /**
	  * see table 3 
	  * https://docs.google.com/document/d/1BHBtwGcmYtks0fKvZuIZAeJKvTWbNw2W1F8_DHLWStk
	  * @param latitude
	  * @return
	  */
	 public static Region regionFromLatitude(Double latitude) {
		 if (latitude <= Region.Norte.getLowerLatitudeThreshold() && latitude > Region.Norte.getUpperLatitudeThreshold()) {
			 return Region.Norte;
		 }else if (latitude <= Region.Medio.getLowerLatitudeThreshold() && latitude > Region.Medio.getUpperLatitudeThreshold()){
			 return Region.Medio;
		 }else if (latitude <= Region.Sur.getLowerLatitudeThreshold() && latitude > Region.Sur.getUpperLatitudeThreshold()){
			 return Region.Sur;
		 }else return Region.General;
	 }
}


