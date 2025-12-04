#!/bin/bash

# Script to clean and rebuild iOS framework
# Use this when Xcode doesn't pick up new Kotlin functions

set -e  # Exit on error

echo "🧹 Cleaning iOS Build Artifacts"
echo "================================"
echo ""

# Check if we're in the right directory
if [ ! -f "gradlew" ]; then
    echo "❌ Error: gradlew not found. Please run this script from the project root."
    exit 1
fi

# Step 1: Clean Gradle build
echo "📦 Step 1: Cleaning Gradle build..."
./gradlew clean
echo "✅ Gradle clean complete"
echo ""

# Step 2: Remove shared build directory
echo "🗑️  Step 2: Removing shared/build directory..."
rm -rf shared/build
echo "✅ Removed shared/build"
echo ""

# Step 3: Clean Xcode DerivedData (optional - ask user)
read -p "🤔 Clean Xcode DerivedData? This will remove ALL Xcode cached data. (y/N): " -n 1 -r
echo ""
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "🗑️  Step 3: Cleaning Xcode DerivedData..."
    rm -rf ~/Library/Developer/Xcode/DerivedData
    echo "✅ DerivedData cleaned"
else
    echo "⏭️  Step 3: Skipped DerivedData cleaning"
fi
echo ""

# Step 4: Rebuild framework
echo "🔨 Step 4: Rebuilding iOS framework..."
echo ""

# Determine which framework to build
echo "Select target:"
echo "  1) Simulator (M1/M2 Mac) - iosSimulatorArm64"
echo "  2) Simulator (Intel Mac) - iosX64"  
echo "  3) Device (iPhone/iPad) - iosArm64"
echo "  4) All targets"
echo ""
read -p "Enter choice [1-4] (default: 1): " choice
choice=${choice:-1}

case $choice in
    1)
        echo "Building for iOS Simulator (Apple Silicon)..."
        ./gradlew :shared:linkDebugFrameworkIosSimulatorArm64
        FRAMEWORK_PATH="shared/build/bin/iosSimulatorArm64/debugFramework/shared.framework"
        ;;
    2)
        echo "Building for iOS Simulator (Intel)..."
        ./gradlew :shared:linkDebugFrameworkIosX64
        FRAMEWORK_PATH="shared/build/bin/iosX64/debugFramework/shared.framework"
        ;;
    3)
        echo "Building for iOS Device..."
        ./gradlew :shared:linkDebugFrameworkIosArm64
        FRAMEWORK_PATH="shared/build/bin/iosArm64/debugFramework/shared.framework"
        ;;
    4)
        echo "Building all targets..."
        ./gradlew :shared:linkDebugFrameworkIosSimulatorArm64 \
                  :shared:linkDebugFrameworkIosX64 \
                  :shared:linkDebugFrameworkIosArm64
        FRAMEWORK_PATH="shared/build/bin/*/debugFramework/shared.framework"
        ;;
    *)
        echo "❌ Invalid choice. Exiting."
        exit 1
        ;;
esac

echo ""
echo "✅ Framework build complete!"
echo ""

# Step 5: Verify framework exists
echo "🔍 Step 5: Verifying framework..."
if ls $FRAMEWORK_PATH 1> /dev/null 2>&1; then
    echo "✅ Framework found at:"
    ls -d $FRAMEWORK_PATH
else
    echo "❌ Error: Framework not found!"
    exit 1
fi
echo ""

# Step 6: Instructions for Xcode
echo "📱 Next Steps in Xcode:"
echo "======================="
echo ""
echo "1. Open Xcode:"
echo "   open iosApp/iosApp.xcodeproj"
echo ""
echo "2. Clean Build Folder:"
echo "   Product → Clean Build Folder (⌘⇧K)"
echo ""
echo "3. Build:"
echo "   Product → Build (⌘B)"
echo ""
echo "4. Run:"
echo "   Product → Run (⌘R)"
echo ""
echo "✨ Done! Your framework is ready."
echo ""
