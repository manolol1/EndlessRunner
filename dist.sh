#!/bin/bash

KEYSTORE_PATH="/home/mario/Android/Keystores/key0.jks"
KEY_ALIAS="key0"

FILE_NAME="EndlessRunner"

OUTPUT_DIR="out"

# Create the output directory if it doesn't exist
mkdir -p $OUTPUT_DIR

# Build the signed APK
./gradlew android:assembleRelease
apksigner sign --ks $KEYSTORE_PATH --ks-key-alias $KEY_ALIAS --out $OUTPUT_DIR/$FILE_NAME.apk android/build/outputs/apk/release/android-release-unsigned.apk

# Build the desktop jar
./gradlew desktop:dist
cp desktop/build/libs/desktop-1.0.jar $OUTPUT_DIR/$FILE_NAME.jar
