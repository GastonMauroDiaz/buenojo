package com.ciis.buenojo.domain.enumeration;

import com.ciis.buenojo.exceptions.InvalidSatelliteImageType;

/**
 * The SatelliteImageType enumeration.
 * landsatCR,   Mosaico Landsat color real
 * landsatFC,   Mosaico Landsat falso color
 * spotMS,      Mosaico Spot 5 multiespectral
 * spotPS,      Mosaico Spot 5 pansharp
 * bing, 		Descargas del servidor Bing
 * bing_inv,    Descargas del servidor Bing (invierno)
 * esriTopo,    Descargas del servidor ESRI
*/


public enum SatelliteImageType {
  LandsatCR,
  LandsatFC,
  SpotMS,
  SpotPS,
  Bing,
  BingInv,
  EsriTopo,
  General;
	
	public static SatelliteImageType typeFromString(String type) throws InvalidSatelliteImageType{
		if (type.equals("landsatCR")) 		return LandsatCR;
		else if (type.equals("landsatFC")) 	return LandsatFC;
		else if (type.equals("spotMS"))		return SpotMS;
		else if (type.equals("spotPS"))		return SpotPS;
		else if (type.equals("bing")) 		return Bing;
		else if (type.equals("bingInv"))	return BingInv;
		else if (type.equals("esriTopo"))	return EsriTopo;
		else throw new InvalidSatelliteImageType("Type \""+type+" is not a valid SatelliteImageType");
		
		
	}
	
}


