/* -*- Mode: C; indent-tabs-mode: t; c-basic-offset: 4; tab-width: 4 -*- */
/*
 * j-parallel-port-native
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
 * @brief j-parallel-port-native /j-parallel-port-native/src/matheus_jparallelport_JParallelPort.cpp
 * @file matheus_jparallelport_JParallelPort.cpp
 * @date 12/09/2010
 * @author Matheus Neder <matheusneder@gmail.com>
 */

#include "matheus_jparallelport_JParallelPort.h"
#include <ParallelPort.h>
#include <map>
#include <stdexcept>
#include <string>
#include <cstdlib>

using namespace std;

struct JParallelPort
{
	ParallelPort parport;
	string errorMessage;
	bool error;
	JParallelPort() :
		error(false)
	{
	}
};

typedef map<int, JParallelPort*> JParallelPortInstances;

static JParallelPortInstances instances;

JNIEXPORT void JNICALL Java_matheus_jparallelport_JParallelPort_nConstruct(
		JNIEnv *, jobject, jint instanceID)
{
	//pointer reference that ins't exists eat
	if (instances[instanceID] == NULL)
	{
		instances[instanceID] = new JParallelPort();
	}
}

JNIEXPORT void JNICALL Java_matheus_jparallelport_JParallelPort_nDestruct(
		JNIEnv *, jobject, jint instanceID)
{
	if ((size_t) instanceID < instances.size() && instances[instanceID] != NULL)
	{
		delete instances[instanceID];
		instances.erase(instanceID);
	}
}

JNIEXPORT void JNICALL Java_matheus_jparallelport_JParallelPort_nOpen(JNIEnv *,
		jobject, jint instanceID, jshort port)
{
	if (!instances[instanceID]->parport.isOpen())
	{
		try
		{
			instances[instanceID]->parport.open(port);
		} catch (runtime_error& e)
		{
			instances[instanceID]->errorMessage = e.what();
			instances[instanceID]->error = true;
		}
	}
	else
	{
		instances[instanceID]->errorMessage = "The port is already open";
		instances[instanceID]->error = true;
	}
}

JNIEXPORT jboolean
JNICALL Java_matheus_jparallelport_JParallelPort_nIsOpen(JNIEnv *, jobject,
		jint instanceID)
{
	return instances[instanceID]->parport.isOpen();
}

JNIEXPORT void JNICALL Java_matheus_jparallelport_JParallelPort_nClose(
		JNIEnv *, jobject, jint instanceID)
{
	if (instances[instanceID]->parport.isOpen())
		instances[instanceID]->parport.close();
}

JNIEXPORT jboolean
JNICALL Java_matheus_jparallelport_JParallelPort_nHasException(JNIEnv *,
		jobject, jint instanceID)
{
	return instances[instanceID]->error;
}

JNIEXPORT jstring
JNICALL Java_matheus_jparallelport_JParallelPort_nExceptionGetMessage(
		JNIEnv * env, jobject, jint instanceID)
{
	return env->NewStringUTF(instances[instanceID]->errorMessage.c_str());
}

#define CHECK_INSTANCE(instanceID,ret)					\
	if(!instances[instanceID]->parport.isOpen())		\
	{													\
		instances[instanceID]->error = true;			\
		instances[instanceID]->errorMessage =			\
		"The port isnt open";							\
		return ret;										\
	}

JNIEXPORT jbyte JNICALL Java_matheus_jparallelport_JParallelPort_nReadData(
		JNIEnv *, jobject, jint instanceID)
{
	CHECK_INSTANCE(instanceID, 1)

	try
	{
		char ret = instances[instanceID]->parport.readData();
		return ret;
	} catch (runtime_error& e)
	{
		instances[instanceID]->error = true;
		instances[instanceID]->errorMessage = e.what();
	}

	return -1;
}

JNIEXPORT jbyte JNICALL Java_matheus_jparallelport_JParallelPort_nReadControl(
		JNIEnv *, jobject, jint instanceID)
{
	CHECK_INSTANCE(instanceID, 1)

	try
	{
		char ret = instances[instanceID]->parport.readControl();
		return ret;
	} catch (runtime_error& e)
	{
		instances[instanceID]->error = true;
		instances[instanceID]->errorMessage = e.what();
	}
	return -1;
}

JNIEXPORT jbyte JNICALL Java_matheus_jparallelport_JParallelPort_nReadStatus(
		JNIEnv *, jobject, jint instanceID)
{
	CHECK_INSTANCE(instanceID, 1)

	try
	{
		char ret = instances[instanceID]->parport.readStatus();
		return ret;
	} catch (runtime_error& e)
	{
		instances[instanceID]->error = true;
		instances[instanceID]->errorMessage = e.what();
	}
	return -1;
}

JNIEXPORT void JNICALL Java_matheus_jparallelport_JParallelPort_nWriteData(
		JNIEnv *, jobject, jint instanceID, jbyte value)
{
	CHECK_INSTANCE(instanceID, )
	try
	{
		instances[instanceID]->parport.writeData(value);
	} catch (runtime_error& e)
	{
		instances[instanceID]->error = true;
		instances[instanceID]->errorMessage = e.what();
	}
}

JNIEXPORT void JNICALL Java_matheus_jparallelport_JParallelPort_nWriteControl(
		JNIEnv *, jobject, jint instanceID, jbyte value)
{
	CHECK_INSTANCE(instanceID, )
	try
	{
		instances[instanceID]->parport.writeControl(value);
	} catch (runtime_error& e)
	{
		instances[instanceID]->error = true;
		instances[instanceID]->errorMessage = e.what();
	}
}
