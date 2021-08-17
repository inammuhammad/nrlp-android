LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_LDLIBS := -llog
LOCAL_MODULE    := see
LOCAL_SRC_FILES := ci/lahap.c \
                   ci/eatb.c \
                   ci/amgam.c \
                   ci/aeltd.c \
                   ci/lsponei.c \
                   ci/bhootni.c \
                   cs/wsete.c \
                   cs/pikuchi.c \
                   burl/asdf.c \
                   burl/sikka.c \
                   nrc/cg.c \
                   nrc/cp.c \
                   nrc/mg.c \
                   nrc/nrcl.c \
                   sc/sc.c \
                   pk/ak.c \
                   pk/bk.c\
                   pk/ck.c\
                   pk/dk.c \
                   pk/pak.c \
                   pk/pbk.c \
                   pk/pck.c \
                   pk/pdk.c \
                   dm/dm.c \
                   vap/dap.c \
                   vap/pap.c \
                   dm/pm.c  \
                   ks/aoen.c \
                   ks/atew.c \
                   ks/athw.c \
                   ks/doen.c \
                   ks/dtew.c \
                   ks/dthw.c

include $(BUILD_SHARED_LIBRARY)