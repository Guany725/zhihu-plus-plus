package com.github.zly2006.zhihu.ui.article

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.zly2006.zhihu.data.VoteUpState
import com.github.zly2006.zhihu.util.formatCompactCount
import org.jetbrains.compose.resources.painterResource
import zhihu.shared.generated.resources.Res
import zhihu.shared.generated.resources.ic_vote_down_24dp
import zhihu.shared.generated.resources.ic_vote_up_24dp

@Composable
internal fun ArticleActionBar(
    voteState: VoteUpState,
    voteCount: Int,
    commentCount: Int,
    favorited: Boolean,
    emphasized: Boolean = false,
    onVote: (VoteUpState) -> Unit,
    onComments: () -> Unit,
    onFavorite: () -> Unit,
    onMore: () -> Unit,
) {
    Row(
        Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        ArticleAction(formatCompactCount(voteCount), voteState == VoteUpState.Up, {
            onVote(if (voteState == VoteUpState.Up) VoteUpState.Neutral else VoteUpState.Up)
        }, Modifier.weight(1f), emphasized) {
            Icon(painterResource(Res.drawable.ic_vote_up_24dp), "赞同", Modifier.size(22.dp))
        }
        ArticleAction("反对", voteState == VoteUpState.Down, {
            onVote(if (voteState == VoteUpState.Down) VoteUpState.Neutral else VoteUpState.Down)
        }, Modifier.weight(1f), emphasized) {
            Icon(painterResource(Res.drawable.ic_vote_down_24dp), null, Modifier.size(22.dp))
        }
        ArticleAction(formatCompactCount(commentCount), false, onComments, Modifier.weight(1f), emphasized) {
            Icon(Icons.AutoMirrored.Filled.Comment, "评论", Modifier.size(22.dp))
        }
        ArticleAction("收藏", favorited, onFavorite, Modifier.weight(1f), emphasized) {
            Icon(if (favorited) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder, "收藏", Modifier.size(22.dp))
        }
        ArticleAction("更多", false, onMore, Modifier.weight(1f), emphasized) {
            Icon(Icons.Filled.MoreVert, if (emphasized) "底栏更多选项" else "更多选项", Modifier.size(22.dp))
        }
    }
}

@Composable
private fun ArticleAction(
    label: String,
    active: Boolean,
    onClick: () -> Unit,
    modifier: Modifier,
    emphasized: Boolean,
    icon: @Composable () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.semantics { selected = active },
        shape = MaterialTheme.shapes.medium,
        color = if (active) {
            MaterialTheme.colorScheme.primaryContainer
        } else if (emphasized) {
            MaterialTheme.colorScheme.surfaceContainer
        } else {
            Color.Transparent
        },
        contentColor = if (active) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
    ) {
        Column(
            Modifier.heightIn(min = 56.dp).padding(horizontal = 2.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            icon()
            Text(label, style = MaterialTheme.typography.labelMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}
