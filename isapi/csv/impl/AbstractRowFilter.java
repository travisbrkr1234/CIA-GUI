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

import csv.CommentCallback;
import csv.TableReader;

/**
 * Filters rows from underlying table reader.
 * This is an abstract implementation only that eases filtering. 
 * @author ralph
 *
 */
public abstract class AbstractRowFilter implements TableReader {

	private TableReader reader;
	private Object nextRow[] = null;
	private int rawRowIndex = 0;
	private int rowIndex = 0;
	
	/**
	 * Constructor.
	 */
	public AbstractRowFilter(TableReader reader) {
		this.reader = reader;
	}

	/**
	 * Returns the underlying reader.
	 * @return the reader
	 */
	protected TableReader getReader() {
		return reader;
	}



	/**
	 * Forwarded to underlying reader.
	 * @see csv.TableReader#close()
	 */
	@Override
	public void close() {
		getReader().close();
	}

	/**
	 * Forwarded to underlying reader.
	 * @see csv.TableReader#get(java.lang.String, java.lang.Object[])
	 */
	@Override
	public Object get(String name, Object[] row) {
		return getReader().get(name, row);
	}

	/**
	 * Forwarded to underlying reader.
	 * @see csv.TableReader#getColumnIndex(java.lang.String)
	 */
	@Override
	public int getColumnIndex(String name) {
		return getReader().getColumnIndex(name);
	}

	/**
	 * Forwarded to underlying reader.
	 * @see csv.TableReader#getHeaderRow()
	 */
	@Override
	public Object[] getHeaderRow() {
		return getReader().getHeaderRow();
	}

	/**
	 * Forwarded to underlying reader.
	 * @see csv.TableReader#getMinimumColumnCount()
	 */
	@Override
	public int getMinimumColumnCount() {
		return getReader().getMinimumColumnCount();
	}

	/**
	 * Forwarded to underlying reader.
	 * @see csv.TableReader#hasHeaderRow()
	 */
	@Override
	public boolean hasHeaderRow() {
		return getReader().hasHeaderRow();
	}

	/**
	 * Opens this reader.
	 * @see csv.TableReader#open()
	 */
	@Override
	public void open() {
		getReader().open();
		nextRow = null;
		rawRowIndex = 0;
		rowIndex = 0;
	}

	/**
	 * Forwarded to underlying reader.
	 * @see csv.TableReader#registerCommentCallBack(csv.CommentCallback)
	 */
	@Override
	public void registerCommentCallBack(CommentCallback callback) {
		getReader().registerCommentCallBack(callback);
	}

	/**
	 * Resets the reader.
	 * @see csv.TableReader#reset()
	 */
	@Override
	public void reset() {
		getReader().reset();
		nextRow = null;
		rawRowIndex = 0;
		rowIndex = 0;
	}

	/**
	 * Forwarded to underlying reader.
	 * @see csv.TableReader#setHasHeaderRow(boolean)
	 */
	@Override
	public void setHasHeaderRow(boolean hasHeaderRow) {
		getReader().setHasHeaderRow(hasHeaderRow);
	}

	/**
	 * Forwarded to underlying reader.
	 * @see csv.TableReader#setMinimumColumnCount(int)
	 */
	@Override
	public void setMinimumColumnCount(int length) {
		getReader().setMinimumColumnCount(length);
	}

	/**
	 * Forwarded to underlying reader.
	 * @see csv.TableReader#unregisterCommentCallBack(csv.CommentCallback)
	 */
	@Override
	public void unregisterCommentCallBack(CommentCallback callback) {
		getReader().unregisterCommentCallBack(callback);
	}

	/**
	 * Checks whether there is another row to be delivered
	 * that is not filtered.
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		if (nextRow == null) findNextRow();
		return nextRow != null;
	}

	/**
	 * Read from underlying reader until there is a row not filtered away.
	 */
	protected void findNextRow() {
		while ((nextRow == null) && isMoreRowsExpected() && getReader().hasNext()) {
			Object row[] = getReader().next();
			if (isValidRow(row)) nextRow = row;
			rawRowIndex++;
		}
	}
	
	/**
	 * Tells whether the row can be delivered or will be filtered away.
	 * @param row row to be checked
	 * @return whether row is a valid row
	 * @see #getRawRowIndex()
	 * @see #getRowIndex()
	 */
	protected abstract boolean isValidRow(Object row[]);
	
	/**
	 * Tells whether more rows will be expected after current row.
	 * This method always returns true so all rows from underlying reader
	 * will be checked. However, you should override this method if you
	 * want to avoid checking more rows because you already know that
	 * no row will match your criteria anymore.
	 * @return true if another valid row can be expected
	 */
	protected boolean isMoreRowsExpected() {
		return true;
	}
	
	/**
	 * Returns the row index from the underlying reader (raw row index)
	 * @return the rawRowIndex
	 */
	protected int getRawRowIndex() {
		return rawRowIndex;
	}

	/**
	 * Returns the index of current row (delivered rows only)
	 * @return the rowIndex
	 */
	protected int getRowIndex() {
		return rowIndex;
	}

	/**
	 * Delivers next row.
	 * @see java.util.Iterator#next()
	 */
	@Override
	public Object[] next() {
		if (!hasNext()) throw new IllegalStateException("No more rows available");
		rowIndex++;
		Object rc[] = nextRow;
		nextRow = null;
		return rc;
	}

	/**
	 * Forwarded to underlying reader.
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		getReader().remove();
	}

}
