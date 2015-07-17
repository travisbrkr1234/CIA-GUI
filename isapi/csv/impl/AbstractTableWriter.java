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
package csv.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import csv.TableWriter;
import csv.TypeConversionHandler;
import csv.impl.type.BooleanConversionHandler;
import csv.impl.type.ByteConversionHandler;
import csv.impl.type.CharConversionHandler;
import csv.impl.type.DateConversionHandler;
import csv.impl.type.DoubleConversionHandler;
import csv.impl.type.FloatConversionHandler;
import csv.impl.type.IntegerConversionHandler;
import csv.impl.type.LongConversionHandler;
import csv.impl.type.ShortConversionHandler;

/**
 * Abstract implementation of writer interface.
 * The interface provides basic functionality being needed regardless of underlying medium
 * to be written to.
 * @author ralph
 *
 */
public abstract class AbstractTableWriter implements TableWriter {

    private int rowCount;
	private Map<String,TypeConversionHandler> typeConversionHandlers = new HashMap<String, TypeConversionHandler>();
    
	/**
	 * General initialization.
	 * This implementation does nothing.
	 */
	protected void init() {
		rowCount = 0;
		registerTypeConversionHandler(BooleanConversionHandler.INSTANCE);
		registerTypeConversionHandler(ByteConversionHandler.INSTANCE);
		registerTypeConversionHandler(CharConversionHandler.INSTANCE);
		registerTypeConversionHandler(DoubleConversionHandler.INSTANCE);
		registerTypeConversionHandler(FloatConversionHandler.INSTANCE);
		registerTypeConversionHandler(IntegerConversionHandler.INSTANCE);
		registerTypeConversionHandler(LongConversionHandler.INSTANCE);
		registerTypeConversionHandler(ShortConversionHandler.INSTANCE);
		registerTypeConversionHandler(DateConversionHandler.INSTANCE);
	}
	
	/**
	 * Prints a comment into the output stream.
	 * This implementation does nothing by default.
	 * @param comment the comment to write
	 * @exception IOException when an exception occurs
	 */
	public void printComment(String comment) throws IOException {
	}
	
	/**
	 * Prints a comment into the output stream.
	 * This implementation does nothing by default.
	 * @param comment the comment to write
     * @param row index of row for comment
     * @param column index of column for comment
 	 * @exception IOException when an exception occurs
	 */
	public void printComment(String comment, int row, int column) throws IOException {
	}
	
	/**
	 * Closes the writer.
	 * This implementation does nothing.
	 */
	public void close() {
	}
	
	/**
	 * Returns the rows written.
	 * @return the rowCount
	 */
	public int getRowCount() {
		return rowCount;
	}

	/**
	 * Increments the row count.
	 * @return current row count
	 */
	protected int incrementRowCount() {
		rowCount++;
		return rowCount;
	}
	
    /**
     * Registers a type conversion handler.
     * @param handler handler to register
     */
    public void registerTypeConversionHandler(TypeConversionHandler handler) {
    	for (String type : handler.getTypes()) {
    		typeConversionHandlers.put(type, handler);
    	}
    }
    
    /**
     * Unregisters a type conversion handler.
     * @param handler handler to unregister
     */
    public void unregisterTypeConversionHandler(TypeConversionHandler handler) {
    	for (String type : handler.getTypes()) {
    		typeConversionHandlers.remove(type);
    	}
    }
    
    /**
     * Returns a type conversion handler for the given type.
     * @param type type to get a handler for
     * @return conversion handler
     */
    protected TypeConversionHandler getTypeConversionHandler(String type) {
    	return typeConversionHandlers.get(type);
    }
    
    /**
     * Converts the value to its string representation.
     * @param value object
     * @return string representation
     */
    protected String convert(Object value) {
    	if (value == null) return null;
    	return convert(value.getClass().getName(), value);
    }
    
    /**
     * Converts the value to its string representation.
     * @param type type of object being returned
     * @param value object
     * @return string representation
     */
    protected String convert(String type, Object value) {
    	if (value == null) return null;
    	
    	TypeConversionHandler handler = getTypeConversionHandler(type);
    	if (handler != null) return handler.toString(value);
    	
    	return value.toString();
    }
    

}
