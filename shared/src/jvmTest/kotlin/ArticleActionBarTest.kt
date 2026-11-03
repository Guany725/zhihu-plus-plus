import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runDesktopComposeUiTest
import com.github.zly2006.zhihu.data.VoteUpState
import com.github.zly2006.zhihu.ui.article.ArticleActionBar
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class ArticleActionBarTest {
    @Test
    fun votingAndActionsStayAvailableAfterSelection() = runDesktopComposeUiTest(width = 320, height = 120) {
        var vote by mutableStateOf(VoteUpState.Neutral)
        var comments = 0
        var favorites = 0
        var more = 0
        setContent {
            MaterialTheme {
                ArticleActionBar(
                    vote,
                    123456,
                    65432,
                    false,
                    onVote = { vote = it },
                    onComments = { comments++ },
                    onFavorite = { favorites++ },
                    onMore = { more++ },
                )
            }
        }
        onNodeWithContentDescription("赞同").performClick().assertIsSelected()
        onNodeWithText("反对").assertIsDisplayed().performClick().assertIsSelected()
        onNodeWithContentDescription("赞同").assertIsNotSelected()
        onNodeWithText("反对").performClick().assertIsNotSelected()
        onNodeWithContentDescription("评论").assertIsDisplayed().performClick()
        onNodeWithContentDescription("收藏").assertIsDisplayed().performClick()
        onNodeWithContentDescription("更多选项").assertIsDisplayed().performClick()
        runOnIdle {
            assertEquals(VoteUpState.Neutral, vote)
            assertEquals(1, comments)
            assertEquals(1, favorites)
            assertEquals(1, more)
        }
    }
}
