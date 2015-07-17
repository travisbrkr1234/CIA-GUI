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
package csv.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import csv.TableReader;

/**
 * Reads beans from the underlying table stream.
 * @author ralph
 *
 */
public class BeanReader implements Iterator<Object> {

	private TableReader reader;
	private boolean evaluateHeaderRow = false;
	private String attributes[] = null;
	private Map<String, Method> methods = new HashMap<String, Method>();
	private Class<?> beanClass;
	
	/**
	 * Constructor.
	 * Use this constructor when underlying reader will deliver the attribute names in
	 * first record.
	 * @param beanClass the beanClass
	 * @param reader the underlying reader to read bean properties from
	 */
	public BeanReader(Class<?> beanClass, TableReader reader) {
		this(beanClass, reader, true, null);
	}

	/**
	 * Constructor.
	 * Use this constructor if underlying reader does NOT deliver attribute names.
	 * @param beanClass the beanClass
	 * @param reader the underlying reader to read bean properties from
	 * @param attributes list of attribute names that will be used to create the beans
	 */
	public BeanReader(Class<?> beanClass, TableReader reader, String attributes[]) {
		this(beanClass, reader, false, attributes);
	}

	/**
	 * Internal Constructor.
	 * @param beanClass the beanClass
	 * @param reader the underlying reader to read bean properties from
	 * @param attributes list of attribute names that will be used to create the beans
	 * @param evaluateHeaderRow whether header row will be delivered by reader
	 */
	protected BeanReader(Class<?> beanClass, TableReader reader, boolean evaluateHeaderRow, String attributes[]) {
		this.reader = reader;
		this.evaluateHeaderRow = evaluateHeaderRow;
		if (!evaluateHeaderRow) setAttributes(attributes);
		this.beanClass = beanClass;
	}

	/**
	 * Returns true when there are more beans to be returned.
	 * @see java.util.Iterator#hasNext()
	 * @see csv.TableReader#hasNext()
	 */
	@Override
	public boolean hasNext() {
		if (isEvaluateHeaderRow() && (attributes == null)) {
			// read header row first
			readHeaderRow();
		}
		return reader.hasNext();
	}

	/**
	 * Returns the next bean from the table reader.
	 * @see java.util.Iterator#next()
	 * @see csv.TableReader#next()
	 * @see #convertToBean(Object[])
	 */
	@Override
	public Object next() {
		if (!hasNext()) throw new IllegalStateException("End of stream");
		return convertToBean(reader.next());
	}

	/**
	 * Resets the reader.
	 */
	public void reset() {
		reader.reset();
	}
	
	/**
	 * Closes the reader.
	 */
	public void close() {
		reader.close();
	}
	
	/**
	 * Constructs new bean from values in array. 
	 * @param columns attribute values
	 * @return new bean
	 */
	public Object convertToBean(Object columns[]) {
		// Create new bean
		String attribute = null;
		try {
			Object rc = beanClass.newInstance();
			for (int i=0; i<columns.length; i++) {
				attribute = getAttributeName(i);
				// Ignore attribute if not known
				if (attribute == null) continue;
				
				// Get the method and set attribute
				Method m = getMethod(attribute);
				m.invoke(rc, columns[i]);
			}
			
			return rc;
		} catch (InvocationTargetException e) {
			throw new IllegalStateException("Cannot set attribute: "+attribute, e);
		} catch (InstantiationException e) {
			throw new IllegalStateException("Cannot create JavaBean", e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("Cannot create JavaBean", e);
		}
	}
	
	/**
	 * Method not supported.
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		throw new IllegalStateException("Remove not supported");
	}

	/**
	 * Returns true if attribute names will be evaluated from header row.
	 * @return true if attribute names will be evaluated
	 */
	public boolean isEvaluateHeaderRow() {
		return evaluateHeaderRow;
	}

	/**
	 * Returns the attributes that will be used for each column index.
	 * @return the attributes array where attribute name stands at respective index.
	 */
	public String[] getAttributes() {
		return attributes;
	}

	/**
	 * Sets the attribute names to be set for each column.
	 * @param attributes attribute names to set
	 */
	protected void setAttributes(String attributes[]) {
		if (attributes == null) {
			throw new IllegalArgumentException("attribute names cannot be null");
		}
		this.attributes = attributes;
	}
	
	/**
	 * Reads the next row from stream and sets the attribute names.
	 */
	public void readHeaderRow() {
		if (reader.hasHeaderRow()) {
	        setAttributes(CSVUtils.convertArray(reader.getHeaderRow(), 0));
		}
	}
	
	/**
	 * Returns the attribute name of specified column
	 * @param columnIndex index of column
	 * @return name or null if it was not set
	 */
	protected String getAttributeName(int columnIndex) {
		String attr[] = getAttributes();
		if (attr == null) return null;
		if (attr.length <= columnIndex) return null;
		return attr[columnIndex];
	}
	
	/**
	 * Returns the correct setter method object for the given attribute.
	 * The method will be found by inspection of the JavaBean class.
	 * @param attribute attribute to be set
	 * @return setter method
	 */
	protected Method getMethod(String attribute) {
		// Check if method already found
		Method rc = methods.get(attribute);
		if (rc == null) {
			String mName = getMethodName(attribute);

			// Use first method that match the setter
			// This is half-perfect as it would be better
			// to have the parameter class. However, underlying
			// streams may return null values in columns
			Method arr[] = beanClass.getMethods();
			for (Method m : arr) {
				if (m.getName().equals(mName) && isValidSetterMethod(m)) {
					rc = m;
					break;
				}
			}
			
			if (rc == null) {
				throw new IllegalArgumentException("No setter found for: "+attribute);
			}
			
			methods.put(attribute, rc);
		}
		return rc;
	}
	
	/**
	 * Returns true if method conforms to JavaBean style of a Setter.
	 * @param method method
	 * @return true if method is a setter.
	 */
	protected boolean isValidSetterMethod(Method method) {
		// Has method return type?
		Class<?> returnType = method.getReturnType();
		if (!returnType.equals(Void.TYPE)) return false;
		
		// Is method public?
		if (!Modifier.isPublic(method.getModifiers())) return false;
		
		// Has method arguments?
		if (method.getParameterTypes().length != 1) return false;
		
		// Method is a setter?
		if (!method.getName().startsWith("set")) return false;

		return true;
	}
	
	/**
	 * This implementation returns the name of setter method for the given attribute
	 * @param attribute attribute
	 * @return the name of the setter method for this attribute
	 */
	protected String getMethodName(String attribute) {
		return "set"+attribute.substring(0, 1).toUpperCase() + attribute.substring(1);		
	}
}
