#!/bin/bash

KEYSTORE_PATH="/home/mario/Android/Keystores/key0.jks"
KEY_ALIAS="key0"

FILE_NAME="EndlessRunner"

OUTPUT_DIR="out"

# Create the output directory if it doesn't exist
mkdir -p $OUTPUT_DIR

# Build the signed APK
./gradlew android:assembleRelease
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore $KEYSTORE_PATH android/build/outputs/apk/release/android-release-unsigned.apk $KEY_ALIAS
mv android/build/outputs/apk/release/android-release-unsigned.apk $OUTPUT_DIR/$FILE_NAME.apk

# Build the desktop jar
./gradlew desktop:dist
mv desktop/build/libs/desktop-1.0.jar $OUTPUT_DIR/$FILE_NAME.jar
