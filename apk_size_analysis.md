# APK Size Analysis Report

The current APK size is **32 MB**, while the target (Larix Player) is **7-15 MB**. After analyzing the `app-debug.apk`, the following factors contribute to the bloated size:

## 1. Multi-ABI Native Libraries (22.8 MB)
The APK includes native libraries (`libcrypto.so`, `libsrt.so`, `libssl.so`, `libsrtdroid.so`) for four different architectures:
- `x86_64`
- `x86`
- `arm64-v8a`
- `armeabi-v7a`

In a production environment, we should use **Android App Bundles (AAB)** or split the APK by ABI to ensure users only download the library for their specific device architecture.

## 2. Debug Build Bloat
The current APK is a **debug build** (`app-debug.apk`). Debug builds include extra metadata, are not obfuscated, and do not have unused code/resources removed. 
- **Classes.dex size**: 15.7 MB (No ProGuard/R8 optimization)

## 3. Unoptimized Dependencies
The project uses `androidx.media3`, which is the modern standard, but without R8/ProGuard, the entire library (including unused parts) is bundled into the APK.

## 4. Resource Bloat
While not the primary cause, the `resources.arsc` is 1.3 MB. Enabling resource shrinking can further reduce this.

# Optimization Strategy
1. **Enable R8/ProGuard**: Obfuscate code and remove unused classes/methods.
2. **Enable Resource Shrinking**: Remove unused resources.
3. **ABI Splitting/App Bundle**: Only include necessary native libraries per APK or use AAB.
4. **Release Build**: Generate a release APK which is significantly smaller than a debug one.
5. **Dependency Optimization**: Ensure only required Media3 modules are included.
