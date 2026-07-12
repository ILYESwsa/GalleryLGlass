# Gallery L Glass

Gallery L Glass is a Jetpack Compose Android gallery prototype that uses Kyant0's AndroidLiquidGlass/Backdrop library for liquid-glass overlays.

## Features

- Album filtering for All, Camera, Travel, Favorites, and Screenshots.
- Phone-gallery style photo grid and full-screen-ish viewer.
- Basic editing controls for brightness, contrast, warmth, and crop presets.
- Liquid-glass bottom navigation built with `rememberLayerBackdrop`, `Modifier.layerBackdrop`, and `Modifier.drawBackdrop` from AndroidLiquidGlass.

## Build

```bash
gradle --no-daemon :app:assembleDebug
```

The debug APK is produced at `app/build/outputs/apk/debug/app-debug.apk` when the Android Gradle plugin, AndroidX Compose dependencies, and AndroidLiquidGlass can be resolved from Maven repositories.
