//
// Created by VenD on 20/07/2020.
//

#include <jni.h>
//
JNIEXPORT jstring JNICALL
Java_com_onelink_nrlp_android_utils_LukaKeRakk_pap(JNIEnv *env, jobject instance) {
    return (*env)->NewStringUTF(
            env,
            "JDJiJDEwJFI0SjBSVGdob1hENEFLTi9DZW9tWXV4Y1FUSG94N1VSTUcxSE9BM25qbWlsUG5zZEtJZkZT"
    );
}
