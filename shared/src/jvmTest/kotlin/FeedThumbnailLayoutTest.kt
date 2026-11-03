import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.runDesktopComposeUiTest
import androidx.compose.ui.unit.dp
import com.github.zly2006.zhihu.data.FeedDisplayItem
import com.github.zly2006.zhihu.navigation.LocalNavigator
import com.github.zly2006.zhihu.navigation.Navigator
import com.github.zly2006.zhihu.platform.desktopSettingsStore
import com.github.zly2006.zhihu.ui.components.FeedCard
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class FeedThumbnailLayoutTest {
    @Test
    fun thumbnailReservesSquareBeforeImageLoadsInBothLayouts() {
        val settings = desktopSettingsStore()
        for (duo in listOf(false, true)) {
            settings.putBoolean("duo3_card_layout", duo)
            settings.putBoolean("showFeedThumbnail", true)
            runDesktopComposeUiTest(width = 320, height = 640) {
                setContent {
                    CompositionLocalProvider(LocalNavigator provides Navigator({}, {})) {
                        MaterialTheme {
                            FeedCard(
                                FeedDisplayItem("标题", "简短摘要", "128 赞同", null),
                                thumbnailUrl = "file:///missing-thumbnail.png",
                            )
                        }
                    }
                }
                onNodeWithContentDescription("Thumbnail", useUnmergedTree = true)
                    .assertWidthIsEqualTo(80.dp)
                    .assertHeightIsEqualTo(80.dp)
            }
        }
        settings.remove("duo3_card_layout")
        settings.remove("showFeedThumbnail")
    }
}
