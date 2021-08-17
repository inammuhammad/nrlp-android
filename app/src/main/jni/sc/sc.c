//
// Created by VenD on 20/07/2020.
//

#include <jni.h>
//
JNIEXPORT jstring JNICALL
Java_com_onelink_nrlp_android_utils_SgTils_gs(JNIEnv *env, jobject instance) {
    return (*env)->NewStringUTF(
            env,
            "QjE6QzU6RkY6M0Y6QUQ6Mzc6ODQ6NzM6M0E6OTg6Njg6Q0M6Rjg6NDA6QjA6MEU6NUM6MDE6Mzk6Q0Q="
    );
}
