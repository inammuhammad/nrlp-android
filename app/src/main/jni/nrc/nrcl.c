#include <jni.h>
#include "mg.h"


JNIEXPORT jboolean

JNICALL
Java_com_onelink_nrlp_android_utils_LukaKeRakk_isDetectedTestKeys(
        JNIEnv *env,
        jobject this) {

    return (jboolean)
            isDetectedTestKeys();
}

JNIEXPORT jboolean

JNICALL
Java_com_onelink_nrlp_android_utils_LukaKeRakk_isDetectedDevKeys(
        JNIEnv *env,
        jobject this) {

    return (jboolean)
            isDetectedDevKeys();
}

JNIEXPORT jboolean

JNICALL
Java_com_onelink_nrlp_android_utils_LukaKeRakk_isNotFoundReleaseKeys(
        JNIEnv *env,
        jobject this) {

    return (jboolean)
            isNotFoundReleaseKeys();
}

JNIEXPORT jboolean

JNICALL
Java_com_onelink_nrlp_android_utils_LukaKeRakk_isFoundDangerousProps(
        JNIEnv *env,
        jobject this) {

    return (jboolean)
            isFoundDangerousProps();
}

JNIEXPORT jboolean

JNICALL
Java_com_onelink_nrlp_android_utils_LukaKeRakk_isPermissiveSelinux(
        JNIEnv *env,
        jobject this) {

    return (jboolean)
            isPermissiveSelinux();
}

JNIEXPORT jboolean

JNICALL
Java_com_onelink_nrlp_android_utils_LukaKeRakk_isSuExists(
        JNIEnv *env,
        jobject this) {

    return (jboolean)
            isSuExists();
}

JNIEXPORT jboolean

JNICALL
Java_com_onelink_nrlp_android_utils_LukaKeRakk_isAccessedSuperuserApk(
        JNIEnv *env,
        jobject this) {

    return (jboolean)
            isAccessedSuperuserApk();
}

JNIEXPORT jboolean

JNICALL
Java_com_onelink_nrlp_android_utils_LukaKeRakk_isFoundSuBinary(
        JNIEnv *env,
        jobject this) {

    return (jboolean)
            isFoundSuBinary();
}

JNIEXPORT jboolean

JNICALL
Java_com_onelink_nrlp_android_utils_LukaKeRakk_isFoundBusyboxBinary(
        JNIEnv *env,
        jobject this) {

    return (jboolean)
            isFoundBusyboxBinary();
}


JNIEXPORT jboolean

JNICALL
Java_com_onelink_nrlp_android_utils_LukaKeRakk_isFoundXposed(
        JNIEnv *env,
        jobject this) {

    return (jboolean)
            isFoundXposed();
}

JNIEXPORT jboolean

JNICALL
Java_com_onelink_nrlp_android_utils_LukaKeRakk_isFoundResetprop(
        JNIEnv *env,
        jobject this) {

    return (jboolean)
            isFoundResetprop();
}

JNIEXPORT jboolean

JNICALL
Java_com_onelink_nrlp_android_utils_LukaKeRakk_isFoundWrongPathPermission(
        JNIEnv *env,
        jobject this) {

    return (jboolean)
            isFoundWrongPathPermission();
}

JNIEXPORT jboolean

JNICALL
Java_com_onelink_nrlp_android_utils_LukaKeRakk_isFoundHooks(
        JNIEnv *env,
        jobject this) {

    return (jboolean)
            isFoundHooks();
}
