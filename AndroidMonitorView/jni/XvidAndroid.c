
/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include <string.h>
#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <sys/time.h>

#define LOG_TAG "Xvid" //自定义的变量，相当于logcat函数中的tag
#undef LOG

#include <android/log.h>

#include "xvid.h"

#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)  
   
static int XDIM = 0;
static int YDIM = 0;
static int CSP = XVID_CSP_RGBA;
static int BPP = 4;
static void *dec_handle = NULL;

//decode one frame
static int dec_main(unsigned char *istream,
                    unsigned char *ostream,
                    int istream_size,
                    xvid_dec_stats_t *xvid_dec_stats);


char* jstringTostring(JNIEnv* env, jstring jstr)
{        
    char* rtn = NULL;
    jclass clsstring = (*env)->FindClass(env, "java/lang/String");
    jstring strencode = (*env)->NewStringUTF(env, "utf-8");
    jmethodID mid = (*env)->GetMethodID(env, clsstring, "getBytes", "(Ljava/lang/String;)[B");
    jbyteArray barr= (jbyteArray)(*env)->CallObjectMethod(env, jstr, mid, strencode);
    jsize alen = (*env)->GetArrayLength(env, barr);
    jbyte* ba = (*env)->GetByteArrayElements(env, barr, JNI_FALSE);
    if (alen > 0)
    {
        rtn = (char*)malloc(alen + 1);
        memcpy(rtn, ba, alen);
        rtn[alen] = 0;
    }
    (*env)->ReleaseByteArrayElements(env, barr, ba, 0);
    return rtn;
}

jint Java_com_mega_VideoView_InitDecoder(JNIEnv* env, jobject thiz, jint xdim, jint ydim)
{
    int ret;
    XDIM = xdim;
    YDIM = ydim;
    int w = 15;
    int h = 20;
    
	xvid_gbl_init_t   xvid_gbl_init;
	xvid_dec_create_t xvid_dec_create;
	xvid_gbl_info_t   xvid_gbl_info;
	
	__android_log_print(ANDROID_LOG_INFO, "SanAngeles", "resize w=%d h=%d", w, h);
	__android_log_print(ANDROID_LOG_ERROR, "hexing", "resize1 w=%d h=%d", 155, 233);
	
    LOGD("init1111111111111111111111\n");
    __android_log_print(ANDROID_LOG_ERROR, "LOG_TAG", "CCCCCCCCCCCCCCCCCCCCC");
	/* Reset the structure with zeros */
	memset(&xvid_gbl_init, 0, sizeof(xvid_gbl_init_t));
	memset(&xvid_dec_create, 0, sizeof(xvid_dec_create_t));
	memset(&xvid_gbl_info, 0, sizeof(xvid_gbl_info));
    
	/*------------------------------------------------------------------------
	 * Xvid core initialization
	 *----------------------------------------------------------------------*/
    
	xvid_gbl_info.version = XVID_VERSION;
	xvid_global(NULL, XVID_GBL_INFO, &xvid_gbl_info, NULL);
    
	/* Version */
	xvid_gbl_init.version = XVID_VERSION;
    
	/* Assembly setting */
	xvid_gbl_init.cpu_flags = 0;
	xvid_gbl_init.debug = 0;
	xvid_global(NULL, 0, &xvid_gbl_init, NULL);
    
	/*------------------------------------------------------------------------
	 * Xvid decoder initialization
	 *----------------------------------------------------------------------*/
    
	/* Version */
	xvid_dec_create.version = XVID_VERSION;
    
	/*
	 * Image dimensions -- set to 0, xvidcore will resize when ever it is
	 * needed
	 */
	xvid_dec_create.width = 0;
	xvid_dec_create.height = 0;
    
	xvid_dec_create.num_threads = 1;
    
	ret = xvid_decore(NULL, XVID_DEC_CREATE, &xvid_dec_create, NULL);
    
	dec_handle = xvid_dec_create.handle;
    
	return(ret);
}

jint Java_com_mega_VideoView_StopDecoder(JNIEnv* env, jobject thiz)
{
    int ret;
    
	ret = xvid_decore(dec_handle, XVID_DEC_DESTROY, NULL, NULL);
    
	return(ret);
}

jint Java_com_mega_VideoView_Decode(JNIEnv* env, jobject thiz, jbyteArray in, jint startpos, jint inSize, jbyteArray out)
{
    xvid_dec_stats_t xvid_dec_stats;
    char *istream = (*env)->GetByteArrayElements(env, in, JNI_FALSE);
    char *ostream = (*env)->GetByteArrayElements(env, out, JNI_FALSE);
    int used_bytes = 0;
    int useful_bytes = inSize;
    
    char *streamPtr = istream;
    
    streamPtr += startpos;
    do {
        used_bytes = dec_main(streamPtr, ostream, useful_bytes, &xvid_dec_stats);
        LOGD("Hello LIB!\n");
        __android_log_print(ANDROID_LOG_DEBUG, "xvid", "w=%d h=%d", xvid_dec_stats.data.vol.width, xvid_dec_stats.data.vol.height);
        if(xvid_dec_stats.type == XVID_TYPE_VOL) 
        {   
            /* Check if old buffer is smaller */
            if(XDIM*YDIM < xvid_dec_stats.data.vol.width*xvid_dec_stats.data.vol.height)
            {   
                /* Copy new witdh and new height from the vol structure */
                XDIM = xvid_dec_stats.data.vol.width;
                YDIM = xvid_dec_stats.data.vol.height;
            }
        }
        if (used_bytes > 0) {
            streamPtr += used_bytes;
            useful_bytes -= used_bytes;
        }
    } while (xvid_dec_stats.type <= 0); 
    
    (*env)->ReleaseByteArrayElements(env, in, istream, 0);
    (*env)->ReleaseByteArrayElements(env, out, ostream, 0);
    
    return used_bytes;
}
/* decode one frame  */
static int dec_main(unsigned char *istream,
		 unsigned char *ostream,
		 int istream_size,
		 xvid_dec_stats_t *xvid_dec_stats)
{
    
	int ret;
    
	xvid_dec_frame_t xvid_dec_frame;
    
	/* Reset all structures */
	memset(&xvid_dec_frame, 0, sizeof(xvid_dec_frame_t));
	memset(xvid_dec_stats, 0, sizeof(xvid_dec_stats_t));
    
	/* Set version */
	xvid_dec_frame.version = XVID_VERSION;
	xvid_dec_stats->version = XVID_VERSION;
    xvid_dec_frame.general          = 0;
    
	/* Input stream */
	xvid_dec_frame.bitstream        = istream;
	xvid_dec_frame.length           = istream_size;
    
	/* Output frame structure */
	xvid_dec_frame.output.plane[0]  = ostream;
    if (ostream == NULL)
        xvid_dec_frame.output.stride[0] = 0;
    else
        xvid_dec_frame.output.stride[0] = XDIM*BPP;
	xvid_dec_frame.output.csp = CSP;
    
	ret = xvid_decore(dec_handle, XVID_DEC_DECODE, &xvid_dec_frame, xvid_dec_stats);
    
	return(ret);
}

