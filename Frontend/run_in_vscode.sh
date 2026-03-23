#!/bin/bash

clear
# Resolve Android SDK path for macOS
ANDROID_SDK=$HOME/Library/Android/sdk

# 1. Find the first available emulator
AVD_NAME=$($ANDROID_SDK/emulator/emulator -list-avds 2>/dev/null | grep -E '^[a-zA-Z0-9_-]+$' | head -n 1)

if [ -z "$AVD_NAME" ]; then
    echo "No Android emulators found! Please create an emulator (AVD) first."
    exit 1
fi

echo "Starting emulator: $AVD_NAME..."
# 2. Start emulator in the background, but print errors if it fails to launch
$ANDROID_SDK/emulator/emulator -avd "$AVD_NAME" > /dev/null &

echo "Waiting for emulator to fully boot (this may take a minute)..."
# 3. Wait for the device to connect
$ANDROID_SDK/platform-tools/adb wait-for-device

# Wait for the Android OS to finish booting
while [ "$($ANDROID_SDK/platform-tools/adb shell getprop sys.boot_completed | tr -d '\r')" != "1" ]; do
    sleep 2
done

echo "Emulator is ready! Building and launching the app..."
# 4. Run the Gradle build and start the app
./gradlew installDebug && $ANDROID_SDK/platform-tools/adb shell am start -n com.example.androidexample/.MainActivity