#!/bin/bash

#   ./run_in_vscode.sh 

clear
# Resolve Android SDK path for macOS
ANDROID_SDK=$HOME/Library/Android/sdk

# 1. Find the first available emulator
AVD_NAME=$($ANDROID_SDK/emulator/emulator -list-avds 2>/dev/null | grep -E '^[a-zA-Z0-9_-]+$' | head -n 1)

if [ -z "$AVD_NAME" ]; then
    echo "No Android emulators found! Please create an emulator (AVD) first."
    exit 1
fi

if $ANDROID_SDK/platform-tools/adb devices | grep -E -q "emulator-[0-9]+[[:space:]]+device"; then
    echo "Emulator is already running. Skipping boot sequence..."
else
    echo "Starting emulator: $AVD_NAME..."
    # 2. Start emulator in the background, but print errors if it fails to launch
    $ANDROID_SDK/emulator/emulator -avd "$AVD_NAME" -dns-server 8.8.8.8 > /dev/null &

    echo "Waiting for emulator to fully boot (this may take a minute)..."
    # 3. Wait for the device to connect
    $ANDROID_SDK/platform-tools/adb wait-for-device

    # Wait for the Android OS to finish booting
    while [ "$($ANDROID_SDK/platform-tools/adb shell getprop sys.boot_completed | tr -d '\r')" != "1" ]; do
        sleep 2
    done
fi

echo "Emulator is ready! Uninstalling old app to free up space..."
$ANDROID_SDK/platform-tools/adb uninstall com.example.androidexample || true

echo "Building and launching the new app..."
# 4. Run the Gradle build and start the app
./gradlew installDebug && $ANDROID_SDK/platform-tools/adb shell am start -n com.example.androidexample/.MainActivity