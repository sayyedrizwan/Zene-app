package com.rizwansayyed.zene.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.SerializedName
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class MusicQRData(
    val type: String,
    val timestamp: Long,
    val song: SongInfo,
    @SerializedName("app_info")
    val appInfo: AppInfo
)

@Serializable
data class SongInfo(
    val title: String,
    val artist: String,
    val album: String,
    val duration: Int,
    val genre: String,
    val url: String?
)

@Serializable
data class AppInfo(
    @SerializedName("generated_by")
    val generatedBy: String,
    val version: String
)


private val SpotifyGreen = Color(0xFF1DB954)
private val SpotifyDarkGreen = Color(0xFF1ED760)
private val SpotifyBlack = Color(0xFF191414)
private val SpotifyDarkGray = Color(0xFF121212)
private val SpotifyGray = Color(0xFF535353)

private val DarkColorScheme = darkColorScheme(
    primary = SpotifyGreen,
    secondary = SpotifyDarkGreen,
    tertiary = SpotifyGray,
    background = SpotifyBlack,
    surface = SpotifyDarkGray,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = SpotifyGreen,
    secondary = SpotifyDarkGreen,
    tertiary = SpotifyGray,
    background = Color.White,
    surface = Color(0xFFF5F5F5),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = SpotifyBlack,
    onSurface = SpotifyBlack,
)

@Composable
fun MusicQRScannerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicQRScannerApp(
    onRequestPermission: (String) -> Unit,
    hasPermission: (String) -> Boolean,
    viewModel: MusicQRViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.QrCodeScanner,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Music QR Scanner",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        when (val v = uiState) {
            is MusicQRUiState.Initial -> {
                InitialScreen(
                    onScanClick = {
                        if (hasPermission(Manifest.permission.CAMERA)) {
                            viewModel.startScanning()
                        } else {
                            onRequestPermission(Manifest.permission.CAMERA)
                        }
                    }
                )
            }

            is MusicQRUiState.Scanning -> {
                QRScannerScreen(
                    onQRScanned = { qrContent ->
                        viewModel.handleQRResult(qrContent)
                    },
                    onCancel = {
                        viewModel.cancelScanning()
                    }
                )
            }

            is MusicQRUiState.MusicFound -> {
                MusicFoundScreen(
                    musicData = v.musicData,
                    onPlayMusic = { viewModel.playMusic(v.musicData) },
                    onScanAgain = { viewModel.resetToInitial() }
                )
            }

            is MusicQRUiState.History -> {
                HistoryScreen(
                    scannedMusic = v.scannedMusic,
                    onBackToScanner = { viewModel.resetToInitial() },
                    onPlayMusic = { musicData -> viewModel.playMusic(musicData) }
                )
            }

            is MusicQRUiState.Error -> {
                ErrorScreen(
                    message = v.message,
                    onRetry = { viewModel.resetToInitial() }
                )
            }
        }
    }
}

// Initial Screen
@Composable
fun InitialScreen(
    onScanClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Large QR Icon with gradient background
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Filled.QrCode2,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Scan Music QR Codes",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Discover and play music by scanning QR codes from other users or music platforms",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Scan Button
        Button(
            onClick = onScanClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Icon(
                Icons.Filled.CameraAlt,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Start Scanning",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Features list
        FeaturesList()
    }
}

@Composable
fun FeaturesList() {
    val features = listOf(
        Triple(Icons.Filled.MusicNote, "Song Info", "Get title, artist, album details"),
        Triple(Icons.Filled.PlayArrow, "Instant Play", "Play music directly from QR codes"),
        Triple(Icons.Filled.History, "Scan History", "Keep track of all scanned music")
    )

    features.forEach { (icon, title, description) ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun QRScannerScreen(
    onQRScanned: (String) -> Unit,
    onCancel: () -> Unit
) {
    val scanLauncher = rememberLauncherForActivityResult(
        contract = ScanContract()
    ) { result ->
        if (result.contents == null) {
            onCancel()
        } else {
            onQRScanned(result.contents)
        }
    }

    LaunchedEffect(Unit) {
        val options = ScanOptions().apply {
            setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            setPrompt("Scan a Music QR Code")
            setCameraId(0)
            setBeepEnabled(true)
            setBarcodeImageEnabled(true)
            setOrientationLocked(false)
        }
        scanLauncher.launch(options)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Opening Camera...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

// Music Found Screen
@Composable
fun MusicFoundScreen(
    musicData: MusicQRData,
    onPlayMusic: () -> Unit,
    onScanAgain: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Success Icon
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(MaterialTheme.colorScheme.primary)
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Music Found!",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Music Info Card
        MusicInfoCard(musicData = musicData)

        Spacer(modifier = Modifier.height(32.dp))

        // Play Button
        Button(
            onClick = onPlayMusic,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Icon(
                Icons.Filled.PlayArrow,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Play Music",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Scan Again Button
        OutlinedButton(
            onClick = onScanAgain,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp)
        ) {
            Icon(
                Icons.Filled.QrCodeScanner,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Scan Another",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun MusicInfoCard(musicData: MusicQRData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Album art placeholder
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        )
                    )
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Album,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                musicData.song.title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                musicData.song.artist,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            if (musicData.song.album.isNotEmpty()) {
                Text(
                    musicData.song.album,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Music details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MusicDetailItem(
                    icon = Icons.Filled.Schedule,
                    label = "Duration",
                    value = "${musicData.song.duration / 60}:${
                        String.format(
                            "%02d",
                            musicData.song.duration % 60
                        )
                    }"
                )
                MusicDetailItem(
                    icon = Icons.Filled.Category,
                    label = "Genre",
                    value = musicData.song.genre.capitalize()
                )
            }
        }
    }
}

@Composable
fun MusicDetailItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

// History Screen
@Composable
fun HistoryScreen(
    scannedMusic: List<MusicQRData>,
    onBackToScanner: () -> Unit,
    onPlayMusic: (MusicQRData) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackToScanner) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                "Scan History",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        if (scannedMusic.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Filled.MusicOff,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No music scanned yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(scannedMusic) { musicData ->
                    HistoryMusicItem(
                        musicData = musicData,
                        onPlay = { onPlayMusic(musicData) }
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryMusicItem(
    musicData: MusicQRData,
    onPlay: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Album placeholder
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.MusicNote,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    musicData.song.title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
                Text(
                    musicData.song.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 1
                )
            }

            IconButton(onClick = onPlay) {
                Icon(
                    Icons.Filled.PlayArrow,
                    contentDescription = "Play",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun ErrorScreen(
    message: String, onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Filled.Error,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Oops! Something went wrong",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Icon(
                Icons.Filled.Refresh,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Try Again")
        }
    }
}

sealed class MusicQRUiState {
    object Initial : MusicQRUiState()
    object Scanning : MusicQRUiState()
    data class MusicFound(val musicData: MusicQRData) : MusicQRUiState()
    data class History(val scannedMusic: List<MusicQRData>) : MusicQRUiState()
    data class Error(val message: String) : MusicQRUiState()
}

class MusicQRViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<MusicQRUiState>(MusicQRUiState.Initial)
    val uiState: StateFlow<MusicQRUiState> = _uiState.asStateFlow()

    private val _scannedMusic = mutableListOf<MusicQRData>()
    private val gson = Gson()

    fun startScanning() {
        _uiState.value = MusicQRUiState.Scanning
    }

    fun cancelScanning() {
        _uiState.value = MusicQRUiState.Initial
    }

    fun handleQRResult(qrContent: String) {
        viewModelScope.launch {
            try {
                val musicData = gson.fromJson(qrContent, MusicQRData::class.java)

                if (musicData.type == "music_track") {
                    // Add to history
                    _scannedMusic.add(0, musicData) // Add to beginning
                    if (_scannedMusic.size > 50) { // Keep only last 50 items
                        _scannedMusic.removeAt(_scannedMusic.size - 1)
                    }

                    _uiState.value = MusicQRUiState.MusicFound(musicData)
                } else {
                    _uiState.value = MusicQRUiState.Error("This QR code doesn't contain music data")
                }

            } catch (e: JsonSyntaxException) {
                _uiState.value = MusicQRUiState.Error("Invalid QR code format")
            } catch (e: Exception) {
                _uiState.value = MusicQRUiState.Error("Error reading QR code: ${e.message}")
            }
        }
    }

    fun playMusic(musicData: MusicQRData) {
        // TODO: Implement music playback logic
        // This is where you'd integrate with your music player
        viewModelScope.launch {
            try {
                // Example: Start music playback
                // musicPlayer.play(musicData.song)

                // For now, just show a success message
                // You can replace this with actual music playback
                println("Playing: ${musicData.song.title} by ${musicData.song.artist}")

                // If you have a URL, you might open it
                musicData.song.url?.let { url ->
                    // Intent to open URL or start your music player
                    println("Music URL: $url")
                }

            } catch (e: Exception) {
                _uiState.value = MusicQRUiState.Error("Failed to play music: ${e.message}")
            }
        }
    }

    fun showHistory() {
        _uiState.value = MusicQRUiState.History(_scannedMusic.toList())
    }

    fun resetToInitial() {
        _uiState.value = MusicQRUiState.Initial
    }

    fun clearHistory() {
        _scannedMusic.clear()
    }
}

// Utility object for QR operations
object MusicQRUtils {
    private val gson = Gson()

    /**
     * Generate QR data string for a song
     */
    fun generateMusicQRData(
        title: String,
        artist: String,
        album: String = "",
        duration: Int = 0,
        genre: String = "unknown",
        url: String? = null
    ): String {
        val musicData = MusicQRData(
            type = "music_track",
            timestamp = System.currentTimeMillis(),
            song = SongInfo(title, artist, album, duration, genre, url),
            appInfo = AppInfo("your_music_app", "1.0")
        )
        return gson.toJson(musicData)
    }

    /**
     * Parse QR content to music data
     */
    fun parseMusicQRData(qrContent: String): MusicQRData? {
        return try {
            val musicData = gson.fromJson(qrContent, MusicQRData::class.java)
            if (musicData.type == "music_track") musicData else null
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Validate if QR content is a music QR code
     */
    fun isMusicQRCode(qrContent: String): Boolean {
        return parseMusicQRData(qrContent) != null
    }

    /**
     * Format duration from seconds to MM:SS
     */
    fun formatDuration(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%d:%02d", minutes, remainingSeconds)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicQRScannerApp(
    onRequestPermission: (String) -> Unit,
    hasPermission: (String) -> Boolean,
) {
    val viewModel: MusicQRViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            when (uiState) {
                is MusicQRUiState.Initial -> {
                    TopAppBar(
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Filled.QrCodeScanner,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    "Music QR Scanner",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = { viewModel.showHistory() }
                            ) {
                                Icon(
                                    Icons.Filled.History,
                                    contentDescription = "History",
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent
                        )
                    )
                }

                else -> {
                    // No top bar for other screens
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(vertical = 30.dp)
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)
                        )
                    )
                )
        ) {
            when (val v = uiState) {
                is MusicQRUiState.Initial -> {
                    InitialScreen(
                        onScanClick = {
                            if (hasPermission(Manifest.permission.CAMERA)) {
                                viewModel.startScanning()
                            } else {
                                onRequestPermission(Manifest.permission.CAMERA)
                            }
                        }
                    )
                }

                is MusicQRUiState.Scanning -> {
                    QRScannerScreen(
                        onQRScanned = { qrContent ->
                            viewModel.handleQRResult(qrContent)
                        },
                        onCancel = {
                            viewModel.cancelScanning()
                        }
                    )
                }

                is MusicQRUiState.MusicFound -> {
                    MusicFoundScreen(
                        musicData = v.musicData,
                        onPlayMusic = { viewModel.playMusic(v.musicData) },
                        onScanAgain = { viewModel.resetToInitial() }
                    )
                }

                is MusicQRUiState.History -> {
                    HistoryScreen(
                        scannedMusic = v.scannedMusic,
                        onBackToScanner = { viewModel.resetToInitial() },
                        onPlayMusic = { musicData -> viewModel.playMusic(musicData) },
                        onClearHistory = { viewModel.clearHistory() }
                    )
                }

                is MusicQRUiState.Error -> {
                    ErrorScreen(
                        message = v.message,
                        onRetry = { viewModel.resetToInitial() }
                    )
                }
            }
        }
    }
}

// Enhanced History Screen with clear option
@Composable
fun HistoryScreen(
    scannedMusic: List<MusicQRData>,
    onBackToScanner: () -> Unit,
    onPlayMusic: (MusicQRData) -> Unit,
    onClearHistory: () -> Unit
) {
    var showClearDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackToScanner) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    "Scan History",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            if (scannedMusic.isNotEmpty()) {
                IconButton(onClick = { showClearDialog = true }) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Clear History",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        if (scannedMusic.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Filled.MusicOff,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No music scanned yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Start scanning QR codes to see your music history",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(scannedMusic) { musicData ->
                    HistoryMusicItem(
                        musicData = musicData,
                        onPlay = { onPlayMusic(musicData) }
                    )
                }
            }
        }
    }

    // Clear history confirmation dialog
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = {
                Text("Clear History")
            },
            text = {
                Text("Are you sure you want to clear all scan history? This action cannot be undone.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onClearHistory()
                        showClearDialog = false
                    }
                ) {
                    Text("Clear", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showClearDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

// Enhanced Music Info Card with additional details
@Composable
fun EnhancedMusicInfoCard(musicData: MusicQRData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            // Album art placeholder with gradient
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        )
                    )
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Album,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Song title
            Text(
                musicData.song.title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            // Artist
            Text(
                musicData.song.artist,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            // Album (if available)
            if (musicData.song.album.isNotEmpty()) {
                Text(
                    musicData.song.album,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Music details in a grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MusicDetailItem(
                    icon = Icons.Filled.Schedule,
                    label = "Duration",
                    value = MusicQRUtils.formatDuration(musicData.song.duration)
                )
                MusicDetailItem(
                    icon = Icons.Filled.Category,
                    label = "Genre",
                    value = musicData.song.genre.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase() else it.toString()
                    }
                )
            }

            // Scan timestamp
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Scanned: ${
                    java.text.SimpleDateFormat(
                        "MMM dd, yyyy 'at' HH:mm",
                        java.util.Locale.getDefault()
                    ).format(java.util.Date(musicData.timestamp))
                }",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}