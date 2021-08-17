//
// Created by VenD on 20/07/2020.
//

#include <jni.h>
//
JNIEXPORT jstring JNICALL
Java_com_onelink_nrlp_android_utils_LukaKeRakk_dap(JNIEnv *env, jobject instance) {
    return (*env)->NewStringUTF(
            env,
            "JDJiJDEwJGxGenhXZzlTY29xN2gxUnpHVmpUb08uV2ZDTWt5MEI2WUYucWw1ajExWVhqbS93MFNnMklx"
    );
}
