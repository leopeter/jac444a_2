package Provider.GoogleMapsStatic;

import java.io.*;
import java.util.regex.*;

/**
 * {latitude} (required) specifies a latitudinal value with precision to 6 decimal places.
 * {longitude} (required) specifies a longitudinal value with precision to 6 decimal places.
 * {color} (optional) specifies a color from the set {red,blue,green}.
 * {alpha-character} (optional) specifies a single lowercase alphabetic character from the set {a-z}.
 * <p/>
 * An example marker declaration is of the form {latitude},{longitude},{color}{alpha-character}. Note
 * in particular that the color and alpha-character values of the string are not separated by a comma.
 * A sample marker declaration is shown below.
 * <p/>
 * markers=40.702147,-74.015794,blues|40.711614,-74.012318,greeng&key=MAPS_API_KEY
 */
public class MapMarker implements Serializable {
static final long serialVersionUID = 5805831996822361347L;

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// enum for marker colors
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
enum MarkerColor {
  red, green, blue
}// enum MarkerColor

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// data
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
private char _alpha = '1';
private MarkerColor _color = null;
//private double _lat = -1;
//private double _lon = -1;
private String _address = "";
private String _pCode = "";

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// constructor
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public MapMarker( String address, String pCode, MarkerColor color, char alpha ) {

	_address = address;
	_pCode = pCode;
	_color = color;
	_alpha = alpha;
	/*
	StringBuffer buf = new StringBuffer();
	buf.append(alpha);
	if (!Pattern.matches("[a-zA-Z]", buf))
		throw new IllegalArgumentException("marker alpha is not a char between a-z");
	*/
	if (color == null) throw new IllegalArgumentException("marker color can not be null");
}

public MapMarker( String address, String pCode ) {
	_address = address;
	_pCode = pCode;
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// generate Google Maps uri
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public String toString() {
	StringBuilder sb = new StringBuilder();

	sb.append(_address).append(",").append(_pCode);

	if (_color != null && _alpha != '1') {
		sb.
        append(",").
        append(_color.toString()).
        append(_alpha);
	}

	return sb.toString();
}

}// class Marker
