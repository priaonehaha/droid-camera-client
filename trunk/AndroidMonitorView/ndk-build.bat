mingw����xvid����:
$ cd /e/droid-camera-client/AndroidMonitorView/
$ /d/android-ndk-r6b/ndk-build

cygwin����xvid����:
$ cd  /cygdrive/e/droid-camera-client/AndroidMonitorView
$ /cygdrive/d/android-ndk-r6b/ndk-build

cmd.exe ���뷽��:
E:\droid-camera-client\AndroidMonitorView>d:\android-ndk-r7\ndk-build.cmd

ndk-build.cmd�� 
set NDK_ROOT=%~dp0 ���������⣬Ҫ�޸�Ϊ
set NDK_ROOT=D:/android-ndk-r7/

������ϵ�ṹ��صĶ�̬���ӿ�:
ndk-build APP_ABI="armeabi armeabi-v7a x86"