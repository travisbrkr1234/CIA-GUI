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

import java.io.IOException;

/**
 * Introduces an interface for other implementations
 * of table writing interfaces.
 * @author RalphSchuster
 *
 */
public interface TableWriter {
	
	/**
	 * Prints the columns into the table writer.
	 * @param columns columns to be written in row
	 * @throws IOException when an exception occurs
	 */
	public void printRow(Object[] columns) throws IOException;
	
    /**
     * Prints a comment into the stream.
     * Note that not all implementations support comments.
     * @param comment comment to write
     * @throws IOException when an exception occurs
     */
    public void printComment(String comment) throws IOException;
    
    /**
     * Prints a comment into the stream.
     * Note that not all implementations support comments.
     * @param comment comment to write
     * @param row index of row for comment
     * @param column index of column for comment
     * @throws IOException when an exception occurs
     */
    public void printComment(String comment, int row, int column) throws IOException;

	/**
	 * Closes the writer.
	 */
	public void close();
}
