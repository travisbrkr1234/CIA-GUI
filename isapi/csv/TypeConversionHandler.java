/*
 * This file is part of CSV package.
 *
 *  CSV is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  CSV is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with CSV.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package csv;

/**
 * A type conversion handler can take any string and convert it to an appropriate
 * object and vice versa.
 * @author ralph
 *
 */
public interface TypeConversionHandler {

	/**
	 * Returns the types that this handler is responsible for.
	 * @return type strings
	 */
	public String[] getTypes();
	
	/**
	 * Converts the string (from stream) into corresponding object.
	 * @param s string representation.
	 * @return object
	 */
	public Object toObject(String s);
	
	/**
	 * Converts an object into its string representation.
	 * @param o Object to convert
	 * @return string representation.
	 */
	public String toString(Object o);
}
