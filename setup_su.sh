SUPER_SU_DIR=./SuperSU-v2.68-M

adb root

echo setting SELinux to
adb shell setenforce 0
adb shell getenforce

echo Remounting /system RW
adb shell mount -o remount,rw /system

echo Pushing SU binary
adb push $SUPER_SU_DIR/x64/su /system/bin/su
adb push $SUPER_SU_DIR/x64/su /system/xbin/su

echo Setting permissions
adb shell chmod 0755 /system/xbin/su
adb shell chmod 0755 /system/bin/su

echo Setting up superuser
adb shell su --install
adb shell "su --daemon&"

echo Remounting /system RO
adb shell mount -o remount,ro /system

echo Installing Superuser app
adb install $SUPER_SU_DIR/common/Superuser.apk

#echo setting SELinux to
#adb shell setenforce 1
#adb shell getenforce

