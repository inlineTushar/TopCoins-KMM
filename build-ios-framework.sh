#!/bin/bash

# Script to build the iOS framework for the shared module
# This script should be run from the project root directory

set -e  # Exit on error

echo "🔨 Building iOS Framework for Top Coins"
echo "========================================"
echo ""

# Check if we're in the right directory
if [ ! -f "gradlew" ]; then
    echo "❌ Error: gradlew not found. Please run this script from the project root."
    exit 1
fi

# Determine configuration (Debug or Release)
CONFIGURATION="${CONFIGURATION:-Debug}"
echo "📦 Configuration: $CONFIGURATION"
echo ""

# Determine which task to run based on configuration
if [ "$CONFIGURATION" == "Release" ]; then
    TASK_PREFIX="linkRelease"
else
    TASK_PREFIX="linkDebug"
fi

# Build the framework for all iOS targets
echo "⚙️  Building shared framework for all iOS targets..."
echo ""

echo "📱 Building for iOS Simulator (Apple Silicon)..."
./gradlew :shared:${TASK_PREFIX}FrameworkIosSimulatorArm64

echo "📱 Building for iOS Simulator (Intel)..."
./gradlew :shared:${TASK_PREFIX}FrameworkIosX64

echo "📱 Building for iOS Devices..."
./gradlew :shared:${TASK_PREFIX}FrameworkIosArm64

echo ""
echo "✅ Framework build complete for all targets!"
echo ""
echo "📍 Framework locations:"
echo "   • Simulator (M1/M2): shared/build/bin/iosSimulatorArm64/${CONFIGURATION}Framework/shared.framework"
echo "   • Simulator (Intel): shared/build/bin/iosX64/${CONFIGURATION}Framework/shared.framework"
echo "   • Devices:           shared/build/bin/iosArm64/${CONFIGURATION}Framework/shared.framework"
echo ""
echo "🎉 You can now:"
echo "   1. Open iosApp/iosApp.xcodeproj in Xcode"
echo "   2. Select a simulator or device"
echo "   3. Run the app (⌘R)"
echo ""
echo "ℹ️  Note: Xcode will automatically build the correct framework variant"
echo "   when you run the app. This script is optional for testing."
echo ""
