package com.example.gallerylglass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { GalleryLGlassApp() }
    }
}

data class GalleryPhoto(val id: Int, val album: String, val title: String, val colors: List<Color>)

data class EditState(val brightness: Float = 0f, val contrast: Float = 1f, val warmth: Float = 0f, val crop: String = "Original")

private val photos = listOf(
    GalleryPhoto(1, "Camera", "Sunset Cove", listOf(Color(0xFFFF8A65), Color(0xFF5E35B1))),
    GalleryPhoto(2, "Camera", "Glass Tower", listOf(Color(0xFF80DEEA), Color(0xFF1565C0))),
    GalleryPhoto(3, "Travel", "Kyoto Rain", listOf(Color(0xFFA5D6A7), Color(0xFF00695C))),
    GalleryPhoto(4, "Travel", "Metro Lights", listOf(Color(0xFFFFD54F), Color(0xFFC2185B))),
    GalleryPhoto(5, "Favorites", "Quiet Desk", listOf(Color(0xFFE1BEE7), Color(0xFF455A64))),
    GalleryPhoto(6, "Favorites", "Blue Hour", listOf(Color(0xFF90CAF9), Color(0xFF1A237E))),
    GalleryPhoto(7, "Screenshots", "Palette", listOf(Color(0xFFFFF59D), Color(0xFF26A69A))),
    GalleryPhoto(8, "Screenshots", "Mockup", listOf(Color(0xFFFFCDD2), Color(0xFFAD1457)))
)

@Composable
fun GalleryLGlassApp() {
    var selectedAlbum by remember { mutableStateOf("All") }
    var selectedPhoto by remember { mutableStateOf<GalleryPhoto?>(null) }
    var editState by remember { mutableStateOf(EditState()) }
    val backdrop = rememberLayerBackdrop {
        drawRect(Color(0xFFF7FAFF))
        drawContent()
    }

    MaterialTheme(colorScheme = lightColorScheme(primary = Color(0xFF3D5AFE), secondary = Color(0xFF00B8D4))) {
        Box(Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxSize()
                    .layerBackdrop(backdrop)
                    .background(Brush.verticalGradient(listOf(Color(0xFFEAF2FF), Color(0xFFFFF7FB))))
                    .padding(20.dp)
            ) {
                Header()
                AlbumChips(selectedAlbum) { selectedAlbum = it }
                PhotoGrid(selectedAlbum) { selectedPhoto = it; editState = EditState() }
            }

            GlassBottomBar(
                modifier = Modifier.align(Alignment.BottomCenter).padding(20.dp),
                backdrop = backdrop
            )

            selectedPhoto?.let { photo ->
                PhotoViewer(photo, editState, { editState = it }, { selectedPhoto = null }, Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
private fun Header() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Column {
            Text("Gallery L Glass", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Albums, viewer, and quick edits", color = Color(0xFF516070))
        }
        Icon(Icons.Default.Search, null, Modifier.clip(CircleShape).background(Color.White.copy(.55f)).padding(12.dp), tint = Color(0xFF3D5AFE))
    }
}

@Composable
private fun AlbumChips(selectedAlbum: String, onSelect: (String) -> Unit) {
    Row(Modifier.padding(vertical = 18.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        (listOf("All") + photos.map { it.album }.distinct()).forEach { album ->
            FilterChip(selected = selectedAlbum == album, onClick = { onSelect(album) }, label = { Text(album) })
        }
    }
}

@Composable
private fun PhotoGrid(selectedAlbum: String, onPhoto: (GalleryPhoto) -> Unit) {
    val visible = if (selectedAlbum == "All") photos else photos.filter { it.album == selectedAlbum }
    LazyVerticalGrid(columns = GridCells.Adaptive(150.dp), contentPadding = PaddingValues(bottom = 110.dp), verticalArrangement = Arrangement.spacedBy(14.dp), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        items(visible) { photo -> PhotoTile(photo, onPhoto) }
    }
}

@Composable
private fun PhotoTile(photo: GalleryPhoto, onPhoto: (GalleryPhoto) -> Unit) {
    Column(Modifier.clip(RoundedCornerShape(28.dp)).background(Color.White.copy(.45f)).clickable { onPhoto(photo) }.padding(10.dp)) {
        Box(Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(22.dp)).background(Brush.linearGradient(photo.colors)))
        Text(photo.title, Modifier.padding(top = 10.dp), fontWeight = FontWeight.SemiBold)
        Text(photo.album, color = Color(0xFF667085), style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun GlassBottomBar(modifier: Modifier = Modifier, backdrop: com.kyant.backdrop.Backdrop) {
    Row(
        modifier.drawBackdrop(
            backdrop = backdrop,
            shape = { RoundedCornerShape(32.dp) },
            effects = { vibrancy(); blur(8.dp.toPx()); lens(16.dp.toPx(), 28.dp.toPx()) },
            onDrawSurface = { drawRect(Color.White.copy(alpha = .48f)) }
        ).height(72.dp).fillMaxWidth().padding(horizontal = 18.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavIcon(Icons.Default.PhotoLibrary, "Photos")
        NavIcon(Icons.Default.Folder, "Albums")
        NavIcon(Icons.Default.Favorite, "Favorites")
        NavIcon(Icons.Default.Tune, "Edit")
    }
}

@Composable
private fun NavIcon(icon: ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, label, tint = Color(0xFF334155))
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color(0xFF334155))
    }
}

@Composable
private fun PhotoViewer(photo: GalleryPhoto, state: EditState, onState: (EditState) -> Unit, onClose: () -> Unit, modifier: Modifier) {
    Surface(modifier.padding(18.dp), shape = RoundedCornerShape(34.dp), color = Color.White.copy(.88f), tonalElevation = 6.dp) {
        Column(Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(photo.title, fontWeight = FontWeight.Bold)
                IconButton(onClick = onClose) { Icon(Icons.Default.Close, "Close") }
            }
            Box(Modifier.fillMaxWidth().height(320.dp).clip(RoundedCornerShape(28.dp)).background(Brush.linearGradient(photo.colors)))
            EditSlider("Brightness", state.brightness, -1f..1f) { onState(state.copy(brightness = it)) }
            EditSlider("Contrast", state.contrast, .5f..1.8f) { onState(state.copy(contrast = it)) }
            EditSlider("Warmth", state.warmth, -1f..1f) { onState(state.copy(warmth = it)) }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Original", "1:1", "4:3", "16:9").forEach { crop -> AssistChip(onClick = { onState(state.copy(crop = crop)) }, label = { Text(crop) }) }
            }
        }
    }
}

@Composable
private fun EditSlider(label: String, value: Float, range: ClosedFloatingPointRange<Float>, onValue: (Float) -> Unit) {
    Column(Modifier.fillMaxWidth().padding(top = 8.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge)
        Slider(value = value, onValueChange = onValue, valueRange = range)
    }
}
