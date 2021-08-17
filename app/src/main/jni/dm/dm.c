//
// Created by VenD on 7/30/20.
//

#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_onelink_nrlp_android_utils_LukaKeRakk_dm(JNIEnv *env, jobject instance) {
    return (*env)->NewStringUTF(env, "c2FuZGJveGFwaS4xbGluay5uZXQucGs=");
}

