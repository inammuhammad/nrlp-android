//
// Created by VenD on 7/30/20.
//

#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_onelink_nrlp_android_utils_LukaKeRakk_monika(JNIEnv *env, jobject instance) {
    return (*env)->NewStringUTF(env, "aHR0cHM6Ly9hcGkubnJscC5jb20ucGsvb25lbGluay9ucmxwLwo=");
}

