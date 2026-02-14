# Professional SRT Player: Technical Specifications

This build is engineered for **ultra-low latency** and **minimal footprint**, specifically optimized for high-quality SRT streaming.

## 1. Core Engine: Optimized Media3
The player uses a custom-configured **Jetpack Media3 (ExoPlayer)** instance with a specialized `LoadControl` strategy:
- **Min Buffer**: 500ms
- **Max Buffer**: 1000ms
- **Buffer for Playback**: 250ms
- **Buffer for Rebuffer**: 500ms
- **Strategy**: Prioritizes time over size to maintain live stream synchronization.

## 2. Size Optimization (Target: < 10 MB)
To achieve a size comparable to or better than Larix Player (7-15MB), the following production techniques were used:
- **ABI Stripping**: The APK only includes `armeabi-v7a` and `arm64-v8a` libraries, covering 99% of modern Android devices while removing ~10MB of x86/x86_64 bloat.
- **Aggressive R8/ProGuard**: Unused code from the Media3 and SRTDroid libraries is stripped.
- **Dependency Isolation**: All non-SRT protocols (HLS, DASH, RTMP) were removed from the build to ensure the smallest possible binary.

## 3. Professional Features
- **Keep Screen On**: Automatically prevents the device from sleeping during active playback.
- **Buffered UI**: Integrated `ProgressBar` for seamless state transitions.
- **ViewBinding**: Type-safe UI architecture for crash prevention.
- **SRT Query Support**: Supports `passphrase` parameter directly in the URL (e.g., `srt://host:port?passphrase=mysecret`).

## 4. Build Instructions
To generate the final APK:
1. Open the project in Android Studio.
2. Select the **Release** build variant.
3. Build the APK via **Build > Build APK(s)**.
4. The resulting APK will be located in `app/build/outputs/apk/release/`.
