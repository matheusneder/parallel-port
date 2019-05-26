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
 * Provide Data Masking for JParallelPort with writeControl, readControl,
 * readStatus and readStatusWithSign static methods that takes JParallelPort
 * object as the first parameter
 * 
 * @author Matheus Neder <matheusneder@gmail.com>
 * 
 */
abstract public class DataMasking {

	/**
	 * Apply a xxxx 1011 xor on value and call the JParallelPort.writeControl
	 * with masked value. The xxxx nible is replaced with 0000
	 */
	public static void writeControl(JParallelPort parport, byte value)
			throws IOException {
		parport.writeControl((byte) ((value ^ 0x0b) & 0x0f));
	}

	/**
	 * Apply xxxx 1011 xor mask on returned value from control pins. The xxxx
	 * nible is replaced with 0000
	 */
	public static byte readControl(JParallelPort parport) throws IOException {
		return (byte) ((parport.readControl() ^ 0x0b) & 0x0f);
	}

	/**
	 * Apply 1000 0000 xor and shit 3 times right
	 */
	public static byte readStatus(JParallelPort parport) throws IOException {
		return (byte) (((parport.readStatus() ^ 0x80) >> 3) & 0x0f);
	}

	/**
	 * Do the same thing as readStatus but with sign correction
	 */
	public static byte readStatusWithSign(JParallelPort parport)
			throws IOException {
		byte ret = readStatus(parport);
		// check for sign bit
		if ((ret | 0xf7) == 0xff)
			// set the 4 higher bits
			ret |= 0xf0;
		return ret;
	}
}
