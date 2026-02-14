# Professional SRT Player ProGuard Rules

# 1. Media3/ExoPlayer - Keep only necessary parts
-keep class androidx.media3.common.** { *; }
-keep class androidx.media3.exoplayer.** { *; }
-keep class androidx.media3.ui.** { *; }

# 2. SRTDroid - Critical for SRT functionality
-keep class io.github.thibaultbee.srtdroid.** { *; }
-keepclassmembers class io.github.thibaultbee.srtdroid.** { *; }

# 3. Native code optimization
-keepclasseswithmembernames class * {
    native <methods>;
}

# 4. General Shrinking
-optimizationpasses 5
-allowaccessmodification
-dontpreverify
-repackageclasses ''
-overloadaggressively
-dontwarn androidx.media3.**
-dontwarn io.github.thibaultbee.srtdroid.**
