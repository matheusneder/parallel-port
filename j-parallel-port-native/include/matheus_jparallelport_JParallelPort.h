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
 * @brief j-parallel-port-native /j-parallel-port-native/include/matheus_jparallelport_JParallelPort.h
 * @file matheus_jparallelport_JParallelPort.h
 * @date 12/09/2010
 * @author Matheus Neder <matheusneder@gmail.com>
 */

#ifndef MATHEUS_JPARALLELPORT_JPARALLELPORT_H_
#define MATHEUS_JPARALLELPORT_JPARALLELPORT_H_

#include <jni.h>
/* Header for class matheus_jparallelport_JParallelPort */

#ifndef _Included_matheus_jparallelport_JParallelPort
#define _Included_matheus_jparallelport_JParallelPort
#ifdef __cplusplus
extern "C"
{
#endif

/*
 * Class:     matheus_jparallelport_JParallelPort
 * Method:    nConstruct
 * Signature: (IS)V
 */
JNIEXPORT void JNICALL Java_matheus_jparallelport_JParallelPort_nConstruct(
		JNIEnv *, jobject, jint);

/*
 * Class:     matheus_jparallelport_JParallelPort
 * Method:    nDestruct
 * Signature: (IS)V
 */
JNIEXPORT void JNICALL Java_matheus_jparallelport_JParallelPort_nDestruct(
		JNIEnv *, jobject, jint);

/*
 * Class:     matheus_jparallelport_JParallelPort
 * Method:    nIsOpen
 * Signature: (I)Z
 */
JNIEXPORT jboolean
		JNICALL Java_matheus_jparallelport_JParallelPort_nIsOpen(JNIEnv *,
				jobject, jint);

/*
 * Class:     matheus_jparallelport_JParallelPort
 * Method:    nHasException
 * Signature: (I)Z
 */
JNIEXPORT jboolean
		JNICALL Java_matheus_jparallelport_JParallelPort_nHasException(JNIEnv *,
				jobject, jint);

/*
 * Class:     matheus_jparallelport_JParallelPort
 * Method:    nExceptionGetMessage
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring
		JNICALL Java_matheus_jparallelport_JParallelPort_nExceptionGetMessage(
				JNIEnv *, jobject, jint);

/*
 * Class:     matheus_jparallelport_JParallelPort
 * Method:    nOpen
 * Signature: (IS)Z
 */
JNIEXPORT void JNICALL Java_matheus_jparallelport_JParallelPort_nOpen(
		JNIEnv *, jobject, jint, jshort);

/*
 * Class:     matheus_jparallelport_JParallelPort
 * Method:    nClose
 * Signature: (IS)V
 */
JNIEXPORT void JNICALL Java_matheus_jparallelport_JParallelPort_nClose(
		JNIEnv *, jobject, jint);

/*
 * Class:     matheus_jparallelport_JParallelPort
 * Method:    nReadData
 * Signature: (I)B
 */
JNIEXPORT jbyte
		JNICALL Java_matheus_jparallelport_JParallelPort_nReadData(
				JNIEnv *, jobject, jint);

/*
 * Class:     matheus_jparallelport_JParallelPort
 * Method:    nReadControl
 * Signature: (I)B
 */
JNIEXPORT jbyte
		JNICALL Java_matheus_jparallelport_JParallelPort_nReadControl(
				JNIEnv *, jobject, jint);

/*
 * Class:     matheus_jparallelport_JParallelPort
 * Method:    nReadStatus
 * Signature: (I)B
 */
JNIEXPORT jbyte
		JNICALL Java_matheus_jparallelport_JParallelPort_nReadStatus(
				JNIEnv *, jobject, jint);

/*
 * Class:     matheus_jparallelport_JParallelPort
 * Method:    nWriteData
 * Signature: (IB)V
 */
JNIEXPORT void
		JNICALL Java_matheus_jparallelport_JParallelPort_nWriteData(
				JNIEnv *, jobject, jint, jbyte);

/*
 * Class:     matheus_jparallelport_JParallelPort
 * Method:    nWriteControl
 * Signature: (IB)V
 */
JNIEXPORT void
		JNICALL Java_matheus_jparallelport_JParallelPort_nWriteControl(
				JNIEnv *, jobject, jint, jbyte);

#ifdef __cplusplus
}
#endif
#endif

#endif /* MATHEUS_JPARALLELPORT_JPARALLELPORT_H_ */
