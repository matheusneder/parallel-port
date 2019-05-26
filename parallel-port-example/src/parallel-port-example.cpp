/* -*- Mode: C; indent-tabs-mode: t; c-basic-offset: 4; tab-width: 4 -*- */
/*
 * parallel-port-example
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
/**
 * @brief parallel-port-example /parallel-port-example/src/parallel-port-example.cpp
 * @file parallel-port-example.cpp
 * @date 13/09/2010
 * @author Matheus Neder <matheusneder@gmail.com>
 */

#include <iostream>
#include <ParallelPort.h>

using namespace std;

int main()
{
	ParallelPort parport; // instantiate ParallelPort object
	try
	{
		parport.open(); // open port

		// write 0 value to data pins (usually 0x378) put all pins to low
		parport.writeData(0);

		//write 0 value to control pins (usually 0x37a) put C0, C1 and C3 to high
		//and C2 to low
		parport.writeControl(0);

		//read from status and print it as an integer
		cout << "Status: " << (int) parport.readStatus() << endl;

		parport.close(); // close port
	} catch (runtime_error& e)
	{
		cout << "Error: " << e.what() << endl;
	}
	return 0;
}
