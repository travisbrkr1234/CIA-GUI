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
 * Filters columns from underlying table reader.
 * This is an abstract implementation only that eases filtering. Most of the
 * methods are forwared to underlying reader. 
 * @author ralph
 *
 */
public abstract class AbstractColumnFilter implements TableReader {

	private TableReader reader;
	private int rowIndex = 0;
	
	/**
	 * Constructor.
	 */
	public AbstractColumnFilter(TableReader reader) {
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
	 * Closes the underlying reader.
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
	 * Returns the translated column index.
	 * @see #getFilteredIndex(int)
	 * @see csv.TableReader#getColumnIndex(java.lang.String)
	 */
	@Override
	public int getColumnIndex(String name) {
		return getFilteredIndex(getReader().getColumnIndex(name));
	}

	/**
	 * Returns the header row filtered.
	 * @see csv.TableReader#getHeaderRow()
	 */
	@Override
	public Object[] getHeaderRow() {
		return filter(getReader().getHeaderRow());
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
	 * Forwarded to underlying reader.
	 * @see csv.TableReader#open()
	 */
	@Override
	public void open() {
		getReader().open();
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
	 * Forwarded to underlying reader.
	 * @see csv.TableReader#reset()
	 */
	@Override
	public void reset() {
		getReader().reset();
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
	 * Forwarded to underlying reader.
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return getReader().hasNext();
	}

	/**
	 * Returns the index of current row (delivered rows only)
	 * @return the rowIndex
	 */
	protected int getRowIndex() {
		return rowIndex;
	}

	/**
	 * Returns the row returned by underlying reader and reshuffles according to definition.
	 * @see #getFilteredIndex(int)
	 * @see java.util.Iterator#next()
	 */
	@Override
	public Object[] next() {
		rowIndex++;
		return filter(getReader().next());
	}

	/**
	 * Filters columns within a row.
	 * Do not override here but override {@link #getFilteredIndex(int)} instead.
	 * @param row row to be filtered
	 * @return row with filtered column sonly
	 * @see #getFilteredIndex(int)
	 */
	protected Object[] filter(Object row[]) {
		if (row == null) return null;
		Object rc[] = new Object[row.length];
		for (int i=0; i<rc.length; i++) {
			int newIndex = getFilteredIndex(i);
			if ((newIndex >= 0) && (newIndex < rc.length)) {
				rc[newIndex] = row[i];
			}
		}
		return rc;
	}
	
	/**
	 * Returns the index in filtered row of the specified original column index.
	 * @param originalIndex index of value in row from underlying reader
	 * @return index of value in filtered row or -1 if column will not be visible.
	 */
	protected abstract int getFilteredIndex(int originalIndex);
	
	/**
	 * Forwarded to underlying reader.
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		getReader().remove();
	}

}
