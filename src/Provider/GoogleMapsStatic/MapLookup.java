/*
 * Part 2: Explore and Comprehend the Source Code.

1. Explore and comprehend the following methods in the MapLookup class:

    getMap( ) // obtain the value, set and pass the value into getURI
    getURI( )
    getDataFromURI( ) 

2. Explain how the StringBuffer object is used in the MapLookup class.

3. Explain how "static final" is used in the MapLookup class.

4. Explain how BufferedImage is used in SampleApp.java.

5. Explain how the Runnable interface is used here.

6. Explain the functionality of the following classes and how they are used.

    org.apache.commons.httpclient.HttpClient
    Task.Support.CoreSupport.ByteBuffer 

7. Explore and comprehend the SampleApp class. 
 */

package Provider.GoogleMapsStatic;

import Provider.GoogleMapsStatic.MapMarker.MarkerColor;
import Task.Support.CoreSupport.*;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;


import java.io.*;

/**
 * MapLookup
 * <p/>
 * http://code.google.com/apis/maps/documentation/staticmaps/index.html
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Apr 16, 2008, 10:55:50 PM
 */
public class MapLookup {

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// constants
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	// change static URI here
public static final String GmapStaticURI = "http://maps.google.com/maps/api/staticmap";
public static final String GmapLicenseKey = "key";

public static final String CenterKey = "center";

public static final String ZoomKey = "zoom";
public static final int ZoomMax = 19;
public static final int ZoomMin = 0;
public static final int ZoomDefault = 10;

public static final String SizeKey = "size";
public static final String SizeSeparator = "x";
public static final int SizeMin = 10;
public static final int SizeMax = 512;
public static final int SizeDefault = SizeMax;

public static final String MarkerSeparator = "|";
public static final String MarkersKey = "markers";

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// data
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public static final MapLookup _map = new MapLookup();
public static String GmapLicense = "";

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// set the license key
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public static void setLicenseKey(String lic) {
  GmapLicense = lic;
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// methods
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public static String getMap(String address, String pCode) {
	return getMap(address, pCode, SizeMax, SizeMax);
}

public static String getMap(String address, String pCode, int sizeW, int sizeH) {
	return getMap(address, pCode, sizeW, sizeH, ZoomDefault);
}

public static String getMap(String address, String pCode, int sizeW, int sizeH, int zoom) {
	return _map.getURI(address, pCode, sizeW, sizeH, zoom);
}

public static String getMap(String address, String pCode, int sizeW, int sizeH, MapMarker... markers) {
	return _map.getURI(address, pCode, sizeW, sizeH, markers);
}

public static String getMap(String address, String pCode, MapMarker... markers) { // ... zero to multiple Object or a Object[]
	return getMap(address, pCode, SizeMax, SizeMax, markers);
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// param handling and uri generation
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public String getURI(String address, String pCode, int sizeW, int sizeH, MapMarker... markers) {
	_validateParams(sizeW, sizeH, ZoomDefault);

	// generate the URI
	StringBuilder sb = new StringBuilder();
	sb.append(GmapStaticURI);

	// size key
	sb.
		append("?").
		append(SizeKey).append("=").append(sizeW).append(SizeSeparator).append(sizeH);
	
	// markers key
	sb.
      append("&").
      append(MarkerUtils.toString(markers));
	
  	// maps key
  	sb.
      	append("&").
      	append(GmapLicenseKey).append("=").append(GmapLicense);
	
	// sensor key
	sb.append("&sensor=true");

  	return sb.toString();
}

public String getURI(String address, String pCode, int sizeW, int sizeH, int zoom) {
	_validateParams(sizeW, sizeH, zoom);
	
	String [] token1 = address.split("\\s+");
	String [] token2 = pCode.split("\\s+");
	address = token1[0];
	for ( int i = 1; i < token1.length; i++ )
		address += ";" + token1[i];
	pCode = token2[0];
	for ( int i = 1; i < token2.length; i++ )
		pCode += ";" + token2[i];
		
	// generate the URI
	StringBuilder sb = new StringBuilder();
	sb.append(GmapStaticURI);

	// center key
	sb.
		append("?").
		append(CenterKey).append("=").append(address).append(",").append(pCode);

	// zoom key
	sb.
      	append("&").
      	append(ZoomKey).append("=").append(zoom);

	// size key
	sb.
      	append("&").
      	append(SizeKey).append("=").append(sizeW).append(SizeSeparator).append(sizeH);
	
  	// markers key
  	sb.
      	append("&").
      	append(MarkerUtils.toString(new MapMarker(address, pCode)));

  	// maps key
  	sb.
      	append("&").
      	append(GmapLicenseKey).append("=").append(GmapLicense);
	
	
	// sensor key
	sb.append("&sensor=true");
	
  	return sb.toString();
}

private void _validateParams(int sizeW, int sizeH, int zoom) {
  if (zoom < ZoomMin || zoom > ZoomMax)
    throw new IllegalArgumentException("zoom value is out of range [" + ZoomMin + "-" + ZoomMax + "]");

  if (sizeW < SizeMin || sizeW > SizeMax)
    throw new IllegalArgumentException("width is out of range [" + SizeMin + "-" + SizeMax + "]");

  if (sizeH < SizeMin || sizeH > SizeMax)
    throw new IllegalArgumentException("height is out of range [" + SizeMin + "-" + SizeMax + "]");
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// actually get the map from Google
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
/** use httpclient to get the data */
public static ByteBuffer getDataFromURI(String uri) throws IOException {

  GetMethod get = new GetMethod(uri);

  try {
    new HttpClient().executeMethod(get);
    return new ByteBuffer(get.getResponseBodyAsStream());
  }
  finally {
    get.releaseConnection();
  }

}


/** markers=40.702147,-74.015794,blues|40.711614,-74.012318,greeng&key=MAPS_API_KEY */
public static class MarkerUtils {
  public static String toString(MapMarker... markers) {
    if (markers.length > 0) {
      StringBuilder sb = new StringBuilder();

      sb.append(MarkersKey).append("=");

      for (int i = 0; i < markers.length; i++) {
        sb.append(markers[i].toString());
        if (i != markers.length - 1) sb.append(MarkerSeparator);
      }

      return sb.toString();
    }
    else {
      return "";
    }
  }
}// class MarkerUtils

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// self test method
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public static void main(String[] args) {

  // make sure to set a valid license key
  setLicenseKey("");

  String address = "70 The Pond Road";
  String pCode = "M3J 3M6";

  String address1 = "1750 Finch Ave. East";
  String pCode1 = "M2J 2X5";

  String u1 = getMap(address, pCode);
  System.out.println(u1);

  String u2 = getMap(address, pCode, 256, 256);
  System.out.println(u2);

  String u3 = getMap(address, pCode, new MapMarker(address, pCode, MapMarker.MarkerColor.blue, 'a'));
  System.out.println(u3);

  String u4 = getMap(address, pCode,
                     250, 500,
                     new MapMarker(address, pCode, MapMarker.MarkerColor.green, 'v'),
                     new MapMarker(address1, pCode1, MapMarker.MarkerColor.red, 'n')
  );
  System.out.println(u4);

}

}//end class MapLookup
