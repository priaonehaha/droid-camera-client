mingw编译xvid方法:
$ cd /e/droid-camera-client/AndroidMonitorView/
$ /d/android-ndk-r6b/ndk-build

cygwin编译xvid方法:
$ cd  /cygdrive/e/droid-camera-client/AndroidMonitorView
$ /cygdrive/d/android-ndk-r6b/ndk-build

cmd.exe 编译方法:
E:\droid-camera-client\AndroidMonitorView>d:\android-ndk-r7\ndk-build.cmd

ndk-build.cmd的 
set NDK_ROOT=%~dp0 这行有问题，要修改为
set NDK_ROOT=D:/android-ndk-r7/

生成体系结构相关的动态链接库:
ndk-build APP_ABI="armeabi armeabi-v7a x86"