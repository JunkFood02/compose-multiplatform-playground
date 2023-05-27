import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kyant.monet.PaletteStyle
import com.kyant.monet.TonalPalettes
import com.kyant.monet.TonalPalettes.Companion.toTonalPalettes
import component.VideoCard
import io.material.hct.Hct
import org.jetbrains.compose.resources.ExperimentalResourceApi
import theme.AppTheme
import theme.Surfaces


val colorList = ((4..10) + (1..3)).map { it * 35.0 }.map {
    Hct.from(it, 40.0, 40.0).toInt()
}.map {
    println(it)
    Color(it)
}

@OptIn(
    ExperimentalResourceApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun App() {
    AppTheme {
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
            rememberTopAppBarState(),
            canScroll = { true })

        Scaffold(modifier = Modifier.fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                LargeTopAppBar(title = {
                    Text(
                        modifier = Modifier,
                        text = "Display",
                    )
                }, navigationIcon = {
                    IconButton(modifier = Modifier, onClick = {}) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                }, scrollBehavior = scrollBehavior
                )
            },
            content = {
                Column(
                    Modifier.padding(it).verticalScroll(rememberScrollState())
                ) {
                    VideoCard(
                        modifier = Modifier.padding(18.dp), thumbnailUrl = "sample3.png"
                    )
                    val pageCount = colorList.size

                    val pagerState = rememberPagerState(initialPage = 0)
                    HorizontalPager(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clearAndSetSemantics { },
                        state = pagerState,
                        pageCount = pageCount,
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) { page ->
                        Row { ColorButtons(colorList[page]) }
                    }
                    /*                    LazyRow(
                                            modifier = Modifier
                                        ) {
                                            items(colorList) { color ->
                                                Row {
                                                    ColorButtons(color)
                                                }
                                            }
                                        }*/

                    val isDarkTheme = isSystemInDarkTheme()
                    PreferenceSwitchWithDivider(title = "Dark theme",
                        icon = if (isDarkTheme) Icons.Outlined.DarkMode else Icons.Outlined.LightMode,
                        isChecked = isDarkTheme,
                        description = "System",
                        onChecked = { },
                        onClick = { })
                }
            })
    }
}

val paletteStyles = listOf(
    PaletteStyle.TonalSpot,
    PaletteStyle.Spritz,
    PaletteStyle.FruitSalad,
    PaletteStyle.Vibrant,
)

const val STYLE_TONAL_SPOT = 0
const val STYLE_SPRITZ = 1
const val STYLE_FRUIT_SALAD = 2
const val STYLE_VIBRANT = 3

@Composable
fun RowScope.ColorButtons(color: Color) {
    paletteStyles.forEachIndexed { index, style ->
        ColorButton(color = color, index = index, tonalStyle = style)
    }
}

@Composable
fun RowScope.ColorButton(
    modifier: Modifier = Modifier,
    color: Color = Color.Green,
    index: Int = 0,
    tonalStyle: PaletteStyle = PaletteStyle.TonalSpot,
) {
    val tonalPalettes = remember {
        color.toTonalPalettes(tonalStyle)
    }
    val isSelect = false
    ColorButtonImpl(modifier = modifier,
        tonalPalettes = tonalPalettes,
        isSelected = { isSelect }) {
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowScope.ColorButtonImpl(
    modifier: Modifier = Modifier,
    isSelected: () -> Boolean = { false },
    tonalPalettes: TonalPalettes,
    cardColor: Color = Surfaces.surfaceContainer(),
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    onClick: () -> Unit = {}
) {

    val containerSize by animateDpAsState(targetValue = if (isSelected.invoke()) 28.dp else 0.dp)
    val iconSize by animateDpAsState(targetValue = if (isSelected.invoke()) 16.dp else 0.dp)

    Surface(
        modifier = modifier
            .padding(4.dp)
            .sizeIn(maxHeight = 80.dp, maxWidth = 80.dp, minHeight = 64.dp, minWidth = 64.dp)
            .weight(1f, false)
            .aspectRatio(1f),
        shape = RoundedCornerShape(16.dp),
        color = cardColor,
        onClick = onClick
    ) {
        val color1 = tonalPalettes.accent1(80.0)
        val color2 = tonalPalettes.accent2(90.0)
        val color3 = tonalPalettes.accent3(60.0)
        Box(Modifier.fillMaxSize()) {
            Box(modifier = modifier
                .size(48.dp)
                .clip(CircleShape)
                .drawBehind { drawCircle(color1) }
                .align(Alignment.Center)) {
                Surface(
                    color = color2, modifier = Modifier
                        .align(Alignment.BottomStart)
                        .size(24.dp)
                ) {}
                Surface(
                    color = color3, modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(24.dp)
                ) {}
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .size(containerSize)
                        .drawBehind { drawCircle(containerColor) },
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = null,
                        modifier = Modifier
                            .size(iconSize)
                            .align(Alignment.Center),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                }
            }
        }
    }
}

object Colors {
    val background = Color(0xFFF7F8FA)
    val container = Color.White
    val titleText = Color.Black.copy(alpha = 0.90f)
    val bodyText = Color.Gray
    val primaryContainer = Color(252, 237, 214)
    val secondaryContainer = Color(0xFFE4EBFD)
    val tertiaryContainer = Color(0xFFE1F3C9)
    val badgeContainer = Color(0xFFF8DFB6)
    val badgeContent = Color(0xFFE98D42)
}

object Typography {
    @Composable
    fun title(): TextStyle = MaterialTheme.typography.titleSmall.copy(
        color = Colors.titleText, fontWeight = FontWeight.Bold
    )

    @Composable
    fun description(): TextStyle = MaterialTheme.typography.bodySmall.copy(
        color = Colors.bodyText, textAlign = TextAlign.Center
//            fontWeight = FontWeight.Bold
    )

    @Composable
    fun badge(): TextStyle = MaterialTheme.typography.labelSmall.copy(
        color = Colors.badgeContent
    )

    @Composable
    fun subtitle(): TextStyle = MaterialTheme.typography.titleSmall.copy(
        color = Colors.bodyText, textAlign = TextAlign.Start
    )
}


expect fun getPlatformName(): String