/*
 * j-parallel-port
 * Copyright (C) 2010 Matheus Neder <matheusneder@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package matheus.jparallelport;

import java.io.IOException;

/**
 * Communicate with parallel port using native ParallelPort library that
 * supports both Linux and Win32 operation systems
 * 
 * @author Matheus Neder <matheusneder@gmail.com>
 * 
 */
public class JParallelPort {

	static {
		System.loadLibrary("j-parallel-port-native");
	}

	// native methods
	private native void nConstruct(int instanceID);

	private native void nDestruct(int instanceID);

	private native boolean nHasException(int instanceID);

	private native boolean nIsOpen(int instanceID);

	private native String nExceptionGetMessage(int instanceID);

	private native void nOpen(int instanceID, short port);

	private native void nClose(int instanceID);

	private native byte nReadData(int instanceID);

	private native byte nReadControl(int instanceID);

	private native byte nReadStatus(int instanceID);

	private native void nWriteData(int instanceID, byte value);

	private native void nWriteControl(int instanceID, byte value);

	// end native methods

	private static int instanceCount = 0;
	private int instanceID;

	public JParallelPort() {
		instanceID = instanceCount;
		nConstruct(instanceID);
		instanceCount++;
	}

	@Override
	protected void finalize() throws Throwable {
		nDestruct(instanceID);
		super.finalize();
	}

	private short port = (short) 0;

	/**
	 * Open port
	 * 
	 * @param port
	 *            Port number, usually port number 0 opens lpt1 (/dev/parport0
	 *            on linux with 0x378 base address)
	 * @throws IOException
	 */

	public void open(short port) throws IOException {
		this.port = port;
		nOpen(instanceID, port);
		if (nHasException(port))
			throw new IOException(nExceptionGetMessage(instanceID));
	}

	/**
	 * @see JParallelPort JParallelPort.open(short port)
	 * 
	 * @throws IOException
	 */
	public void open() throws IOException {
		this.port = 0;
		open(port);
	}

	/**
	 * Verify if port for this instance is open
	 * 
	 */
	public boolean isOpen() {
		return nIsOpen(instanceID);
	}

	/**
	 * Close port
	 */
	public void close() {
		nClose(instanceID);
	}

	/**
	 * Read a byte from status pins (usually 0x379)
	 * 
	 * @return Byte read
	 * @throws IOException
	 */
	public byte readData() throws IOException {
		byte ret = nReadData(instanceID);
		if (nHasException(instanceID))
			throw new IOException(nExceptionGetMessage(instanceID));
		return ret;
	}

	/**
	 * Read a byte from control pins (usually 0x37a).
	 * <p>
	 * Control pins is unidirectional (you can't use as input) and this method
	 * just return the data that you put to device with writeControl method or
	 * the data that was there before you perform a writeControl
	 * 
	 * @return Byte read
	 * @throws IOException
	 */
	public byte readControl() throws IOException {
		byte ret = nReadControl(instanceID);
		if (nHasException(instanceID))
			throw new IOException(nExceptionGetMessage(instanceID));
		return ret;
	}

	/**
	 * Read a byte from status pins (usually 0x379)
	 * 
	 * @return Byte read
	 * @throws IOException
	 */
	public byte readStatus() throws IOException {
		byte ret = nReadStatus(instanceID);
		if (nHasException(instanceID))
			throw new IOException(nExceptionGetMessage(instanceID));
		return ret;
	}

	/**
	 * Write data to data pins (usually 0x378)
	 * 
	 * @param value
	 *            Byte to be sent
	 * @throws IOException
	 */
	public void writeData(byte value) throws IOException {
		nWriteData(instanceID, value);
		if (nHasException(instanceID))
			throw new IOException(nExceptionGetMessage(instanceID));

	}

	/**
	 * Write data to status pins (usually 0x379)
	 * 
	 * @param value
	 *            Byte to be sent
	 * @throws IOException
	 */
	public void writeControl(byte value) throws IOException {
		nWriteControl(instanceID, value);
		if (nHasException(instanceID))
			throw new IOException(nExceptionGetMessage(instanceID));

	}
}
