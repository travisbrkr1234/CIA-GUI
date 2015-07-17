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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import csv.CommentCallback;
import csv.TableReader;
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
 * Abstract implementation that shall be suitable for most implementations.
 * @author ralph
 *
 */
public abstract class AbstractTableReader implements TableReader {

	private List<CommentCallback> commentCallbacks = new ArrayList<CommentCallback>();
    private int rowCount = 0;
    private int lineCount = 0;
	private boolean hasHeaderRow = false;
	private Object headerRow[] = null;
	private boolean headerRowRead = false;
	private int minimumColumnCount = 0;
	private Map<String,TypeConversionHandler> typeConversionHandlers = new HashMap<String, TypeConversionHandler>();
	private Map<Integer,String> columnTypes = new HashMap<Integer,String>();
	
	/**
	 * Default Constructor.
	 */
	public AbstractTableReader() {
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
     * Opens the CSV reader.
     */
    @Override
    public void open() {
    	rowCount = 0;
    	lineCount = 0;
    	headerRow = null;
    	setHeaderRowRead(false);
    }
    
    /**
     * Resets the CSV reader and its underlying stream.
     */
    @Override
    public void reset() {
    	rowCount = 0;
    	lineCount = 0;
    	headerRow = null;
    	setHeaderRowRead(false);
    }
    
	/**
	 * Returns the header row.
	 * @return header row if such was defined.
	 */
	@Override
	public Object[] getHeaderRow() {
		if (!hasHeaderRow()) return null;
		if (!isHeaderRowRead()) readHeaderRow();
		return headerRow;
	}

	/**
	 * Reads the header row if required.
	 * This is an empty method. Subclasses must override to correctly read the header row.
	 */
	protected void readHeaderRow() {
	}
	
	/**
	 * Sets the header rows.
	 * @param names names to be set
	 */
	protected void setHeaderRow(String names[]) {
		setHeaderRowRead(true);
		this.headerRow = names;
	}
	
	/**
	 * @param headerRowRead the headerRowRead to set
	 */
	protected void setHeaderRowRead(boolean headerRowRead) {
		this.headerRowRead = headerRowRead;
	}

	
	/**
	 * Returns the value in column with specified name.
	 * Returns null if row has no such column.
	 * @param name name of column (from header row)
	 * @param row row of values
	 * @return value in row for specified column.
	 */
	@Override
	public Object get(String name, Object row[]) {
		if (row == null) return null;
		int column = getColumnIndex(name);
		if ((column < 0) || (column >= row.length)) return null;
		return row[column];
	}

	/**
	 * Returns the column index of given column name.
	 * The first column with given name will be returned.
	 * @param name name of column
	 * @return index of column or -1 if it does not exist.
	 */
	@Override
	public int getColumnIndex(String name) {
		if (!hasHeaderRow()) throw new IllegalStateException("TableReader has no header row (property hasHeaderRow is false)");
		readHeaderRow();
		if (getHeaderRow() == null) throw new IllegalArgumentException("Stream is empty");
		for (int i=0; i<headerRow.length; i++) {
			if ((headerRow[i] != null) && headerRow[i].toString().equalsIgnoreCase(name)) return i;
		}
		return -1;
	}
	
	/**
	 * Explicitely set the type of a column.
	 * This information will be used to convert the value of this column.
	 * @param columnIndex index of column
	 * @param type type of column
	 * @see #getTypeConversionHandler(String)
	 * @see #convert(int, String)
	 */
	public void setColumnType(int columnIndex, Class<?> type) {
		columnTypes.put(columnIndex, type.getName());
	}
	
	/**
	 * Returns the type of a column.
	 * This information will be used to convert the value of this column.
	 * @param columnIndex index of column.
	 * @return type of values in column
	 * @see #getTypeConversionHandler(String)
	 * @see #convert(int, String)
	 */
	public String getColumnType(int columnIndex) {
		String rc = columnTypes.get(columnIndex);
		if (rc == null) rc = "java.lang.String";
		return rc;
	}
	
	/**
	 * Tells whether the underlying stream has a header row or not
	 * @return true if there is a header row.
	 */
	@Override
	public boolean hasHeaderRow() {
		return hasHeaderRow;
	}

	/**
	 * Tells the reader whether the underlying stream will treat
	 * first row as header row.
	 * @param hasHeaderRow true if there is a header row.
	 */
	@Override
	public void setHasHeaderRow(boolean hasHeaderRow) {
		this.hasHeaderRow = hasHeaderRow;
	}

    /**
     * Adds a comment callback.
     * @param callback the callback
     * @deprecated Use {@link TableReader#registerCommentCallBack(CommentCallback)} instead.
     */
    public void addCommentCallBack(CommentCallback callback) {
    	commentCallbacks.add(callback);
    }
    
    /**
     * Adds a comment callback.
     * @param callback the callback
     */
    public void registerCommentCallBack(CommentCallback callback) {
    	commentCallbacks.add(callback);
    }
    
    
    /**
     * Removes a comment callback.
     * @param callback the callback
     * @deprecated Use {@link TableReader#unregisterCommentCallBack(CommentCallback)} instead
     */
    public void removeCommentCallBack(CommentCallback callback) {
    	commentCallbacks.remove(callback);
    }
    
    /**
     * Removes a comment callback.
     * @param callback the callback
     */
    public void unregisterCommentCallBack(CommentCallback callback) {
    	commentCallbacks.remove(callback);
    }
    
    /**
     * Notifies all comment callbacks about a comment.
     * @param s the comment to notify about
     * @param row row number
     * @param cell cell number in row
     */
    protected void notifyComment(String s, int row, int cell) {
    	for (CommentCallback callback : commentCallbacks) {
    		callback.comment(this, s, row, cell);
    	}
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
     * Converts the string back to correct object.
     * This method will retrieve the column type from {@link #getColumnType(int)} and then
     * then forward the transformation to {@link #convert(String, String)}.
     * @param columnIndex index of column of this value
     * @param value string representation of object
     * @return object the converted object
     * @see #convert(String, String)
     * @see #registerTypeConversionHandler(TypeConversionHandler)
     * @see #getColumnType(int)
     */
    protected Object convert(int columnIndex, String value) {
    	String columnType = getColumnType(columnIndex);
    	if (columnType == null) columnType = "java.lang.String";
    	
    	return convert(columnType, value);
    }
    
    /**
     * Converts the string back to correct object.
     * @param type type of object being returned
     * @param value string representation of object
     * @return object
     */
    protected Object convert(String type, String value) {
    	if (value == null) return null;
    	
    	TypeConversionHandler handler = getTypeConversionHandler(type);
    	if (handler != null) return handler.toObject(value);
    	
    	return value;
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
     * Increases the line count.
     * Line count reflects the lines in an input file.
     * @return lines read so far
     */
    protected int incrementLineCount() {
    	lineCount++;
    	return getLineCount();
    }
    
	/**
     * Line count reflects the lines in an input file.
     * @return lines read so far
	 */
	public int getLineCount() {
		return lineCount;
	}

	/**
	 * Increments the row Count.
	 * Row count is the number of netto rows (<= line count) meaning rows
	 * delivered by {@link TableReader#next()}.
	 * @return rows delivered so far
	 */
    protected int incrementRowCount() {
    	rowCount++;
    	return getRowCount();
    }
    
    /**
     * Returns the row count.
	 * Row count is the number of netto rows (<= line count) meaning rows
	 * delivered by {@link TableReader#next()}.
	 * @return rows delivered so far
	 */
	public int getRowCount() {
		return rowCount;
	}


	/**
	 * Does nothing
	 * @see csv.TableReader#close()
	 */
	@Override
	public void close() {
	}


	/**
	 * @see csv.TableReader#setMinimumColumnCount(int)
	 */
	@Override
	public void setMinimumColumnCount(int length) {
		this.minimumColumnCount = length;
	}


	/**
	 * @return the minimumLineCount
	 */
	@Override
	public int getMinimumColumnCount() {
		return minimumColumnCount;
	}

    /**
     * Returns an array from the columns.
     * This function exists for convinience to take care of minimum column count.
     * @param columns columns to return
     * @return arrray with column values
     */
    protected Object[] convertArray(List<String> columns) {
        int colcount = columns.size();
        Object rc[] = new Object[Math.max(colcount, getMinimumColumnCount())];
        if (colcount > 0) {
            for (int i=0; i<colcount; i++) {
            	rc[i] = convert(i, columns.get(i));
            }
        }
        return rc;
    }
    
    /**
     * Returns an array from the columns.
     * This function exists for convinience to take care of minimum column count.
     * @param columns columns to return
     * @return arrray with column values
     */
    protected Object[] convertArray(String columns[]) {
        int colcount = getMinimumColumnCount();
        if (columns != null) colcount = columns.length;
        Object rc[] = new Object[Math.max(colcount, getMinimumColumnCount())];
        if (colcount > 0) {
            for (int i=0; i<colcount; i++) {
            	rc[i] = convert(i, columns[i]);
            }
        }
        return rc;
    }

	/**
	 * @return the headerRowRead
	 */
	public boolean isHeaderRowRead() {
		return headerRowRead;
	}
    
}
