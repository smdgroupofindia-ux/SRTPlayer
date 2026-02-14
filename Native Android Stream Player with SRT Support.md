# Native Android Stream Player with SRT Support

A lightweight, native Android stream player built with Kotlin and Jetpack Media3 (ExoPlayer), featuring native SRT protocol support.

## Features
- **SRT Support**: Native implementation using `srtdroid` for Secure Reliable Transport playback.
- **Protocol Support**: RTMP, HLS (.m3u8), DASH (.mpd), and standard MP4/progressive streams.
- **Low Latency**: Optimized for live streaming with minimal delay.
- **Native Performance**: Built with Kotlin and Android Jetpack components.

## How to Use
1. Install the provided `app-debug.apk` on your Android device.
2. Open the "Stream Player" app.
3. Enter your stream URL in the input field:
   - For SRT: `srt://your-host:port`
   - For others: Standard URLs (e.g., `rtmp://...`, `https://...`)
4. Click "Play" to start the stream.

## Technical Details
- **Language**: Kotlin
- **Media Engine**: Android Jetpack Media3 (ExoPlayer)
- **SRT Library**: io.github.thibaultbee:srtdroid
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
