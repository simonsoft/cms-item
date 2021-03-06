/**
 * Copyright (C) 2009-2017 Simonsoft Nordic AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.simonsoft.cms.item.stream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * This class extends the ByteArrayOutputStream by
 * providing a method that returns a new ByteArrayInputStream
 * which uses the internal byte array buffer. This buffer
 * is not copied, so no additional memory is used. After
 * creating the ByteArrayInputStream the instance of the
 * ByteArrayInOutStream can not be used anymore.
 * <p>
 * The ByteArrayInputStream can be retrieved using <code>getInputStream()</code>.
 * 
 * @author Nick Russler
 */
public class ByteArrayInOutStream extends ByteArrayOutputStream {
	
	private byte[] bufRead = null;
	private int countRead;
	
	/**
	 * Creates a new ByteArrayInOutStream. The buffer capacity is
	 * initially 32 bytes, though its size increases if necessary.
	 */
	public ByteArrayInOutStream() {
		super();
	}

	/**
	 * Creates a new ByteArrayInOutStream, with a buffer capacity of
	 * the specified size, in bytes.
	 *
	 * @param size
	 *            the initial size.
	 * @exception IllegalArgumentException
	 *                if size is negative.
	 */
	public ByteArrayInOutStream(int size) {
		super(size);
	}
	
	/**
	 * Creates a new ByteArrayInOutStream containing the supplied String.
	 * @param source String containing the data
	 */
	public ByteArrayInOutStream(String source) {
		super(0);
		// Bypass the OutputStream stage.
		this.bufRead = source.getBytes();
		this.countRead = source.getBytes().length;
			
		// set the buffer of the ByteArrayOutputStream to null so it can never be written.
		this.buf = null;
	}

	/**
	 * Creates a new ByteArrayInputStream that uses the internal byte array buffer
	 * of this ByteArrayInOutStream instance as its buffer array. The initial value
	 * of pos is set to zero and the initial value of count is the number of bytes
	 * that can be read from the byte array. The buffer array is not copied. This
	 * instance of ByteArrayInOutStream can not be used anymore after calling this
	 * method.
	 * 
	 * @return the ByteArrayInputStream instance
	 */
	public ByteArrayInputStream getInputStream() {
		
		if (this.bufRead == null && this.buf == null) {
			throw new IllegalStateException();
		}
		
		// Store the buffer so we can return multiple InputStreams based on a single buffer.
		if (this.bufRead == null) {
			this.bufRead = this.buf;
			this.countRead = this.count;
			
			// set the buffer of the ByteArrayOutputStream 
			// to null so it can't be altered anymore
			this.buf = null;
		}
		
		// create new ByteArrayInputStream that respects the current count
		ByteArrayInputStream in = new ByteArrayInputStream(this.bufRead, 0, this.countRead);

		return in;
	}
}
