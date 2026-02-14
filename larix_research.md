# Larix Player Research Findings

## Core Features
- **Protocols Supported**: SLDP, SRT (Pull/Caller, Listen, Rendezvous), RTMP, RTSP, HLS, MPEG-DASH, Icecast.
- **Low Latency**: Specifically designed for low-latency streaming (SLDP and SRT).
- **Architecture**: Built on top of **Android Jetpack Media3** (ExoPlayer).
- **Open Source Components**: Softvelum provides SLDP Playback Library and Media3 DataSource.

## Implementation Plan for "Larix-like" Player
1. **Framework**: Native Android (Kotlin).
2. **Media Engine**: Jetpack Media3 (ExoPlayer).
3. **Key Libraries**:
    - `androidx.media3:media3-exoplayer`
    - `androidx.media3:media3-ui`
    - `androidx.media3:media3-datasource-rtmp` (for RTMP support)
    - SRT support might require a custom DataSource or a 3rd party library like `srt-android-sdk` or integrating libsrt via JNI.
4. **UI Components**:
    - URL input field.
    - Play/Pause/Stop controls.
    - Protocol selection (optional, auto-detect preferred).
    - Status/Metadata display.

## Technical Constraints
- SRT is a key feature of Larix. Standard ExoPlayer doesn't support SRT out-of-the-box.
- SLDP is proprietary but Softvelum provides a library.
- RTMP/HLS/DASH are natively supported by Media3.

## Decision
For the initial version, I will focus on a player that supports **RTMP, HLS, and DASH** using Media3, as these are the most common. I will investigate if I can easily add **SRT** support as it's a signature feature of Larix.
