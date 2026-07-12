# Gallery L Glass

Gallery L Glass is a Jetpack Compose Android gallery prototype that uses Kyant0's AndroidLiquidGlass repository (`https://github.com/Kyant0/AndroidLiquidGlass`) through its published `io.github.kyant0:backdrop-android:2.0.0` artifact for liquid-glass overlays.

## Features

- Album filtering for All, Camera, Travel, Favorites, and Screenshots.
- Phone-gallery style photo grid and full-screen-ish viewer.
- Basic editing controls for brightness, contrast, warmth, and crop presets.
- Liquid-glass bottom navigation built with `rememberLayerBackdrop`, `Modifier.layerBackdrop`, and `Modifier.drawBackdrop` from AndroidLiquidGlass.

## Build

```bash
gradle --no-daemon :app:assembleDebug
```

## Automatic APK builds

Every push, pull request, and manual `workflow_dispatch` run executes `.github/workflows/build-apk.yml`. The workflow installs JDK 17 and Gradle 8.7, builds `:app:assembleDebug`, and uploads `GalleryLGlass-debug-apk` containing the generated debug APK.

The debug APK is produced at `app/build/outputs/apk/debug/app-debug.apk` when the Android Gradle plugin, AndroidX Compose dependencies, and AndroidLiquidGlass can be resolved from Maven repositories.
