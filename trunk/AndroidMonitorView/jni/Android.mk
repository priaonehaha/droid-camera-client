# Copyright (C) 2009 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := XvidAndroid
LOCAL_SRC_FILES := XvidAndroid.c xvid.c encoder.c decoder.c utils/emms.c utils/mbtransquant.c utils/mem_align.c utils/mem_transfer.c utils/timer.c quant/quant_h263.c quant/quant_matrix.c quant/quant_mpeg.c prediction/mbprediction.c plugins/plugin_2pass1.c plugins/plugin_2pass2.c plugins/plugin_dump.c plugins/plugin_lumimasking.c plugins/plugin_psnr.c plugins/plugin_psnrhvsm.c plugins/plugin_single.c plugins/plugin_ssim motion/estimation_bvop.c motion/estimation_common.c motion/estimation_gmc.c motion/estimation_pvop.c motion/estimation_rd_based_bvop.c motion/estimation_rd_based.c motion/gmc.c motion/motion_comp.c motion/sad.c motion/vop_type_decision.c image/colorspace.c image/font.c image/image.c image/interpolate8x8.c image/postprocessing.c image/qpel.c image/reduced.c dct/fdct.c dct/idct.c dct/simple_idct.c bitstream/bitstream.c bitstream/cbp.c bitstream/mbcoding.c
LOCAL_LDLIBS :=  -llog
LOCAL_SHARED_LIBRARIES := libcutils
LOCAL_C_INCLUDES  += system/core/include/cutils

include $(BUILD_SHARED_LIBRARY)
