/*
 * Zhihu++ - Free & Ad-Free Zhihu client for all platforms.
 * Copyright (C) 2024-2026, zly2006 <i@zly2006.me>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation (version 3 only).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.zly2006.zhihu.glass

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.backdrop.isRuntimeShaderSupported

/**
 * 透镜折射需要运行时着色器：Android 13 起支持，iOS、macOS 和桌面端的 Skia 始终支持。
 *
 * 不支持时调用方应回退到普通的 Material 底栏。
 */
val isLiquidGlassSupported: Boolean
    get() = isRuntimeShaderSupported()

/**
 * 玻璃表面采样的背景内容。内容宿主用 [glassBackdropSource] 录制，同一宿主里的玻璃表面再读取它。
 */
@Stable
class GlassBackdrop internal constructor(
    internal val layer: LayerBackdrop,
)

/**
 * 创建一份可供玻璃表面采样的背景。
 *
 * 录制时先铺满 [background]：内容透明的地方如果不铺底，玻璃下方未折射的原始画面会透出来，形成重影。
 */
@Composable
fun rememberGlassBackdrop(background: Color): GlassBackdrop {
    val layer = rememberLayerBackdrop {
        drawRect(background)
        drawContent()
    }
    return remember(layer) { GlassBackdrop(layer) }
}

/**
 * 把这个节点绘制的内容录进 [backdrop]，[backdrop] 为 null 时不做任何事。
 *
 * 玻璃表面必须是这个节点的兄弟而不是子节点，否则会采样到自己。
 */
fun Modifier.glassBackdropSource(backdrop: GlassBackdrop?): Modifier = if (backdrop == null) this else layerBackdrop(backdrop.layer)

/**
 * 给任意控件蒙一层液态玻璃，采样 [backdrop] 录制的背景内容。
 *
 * 只应挂在 [backdrop] 的兄弟节点上，挂成子节点会采样到自己。
 */
fun Modifier.liquidGlass(
    backdrop: GlassBackdrop?,
    shape: Shape,
    surfaceColor: Color,
    fallbackColor: Color = surfaceColor,
    blurRadius: Dp = 16.dp,
    lensRadius: Dp = 12.dp,
): Modifier = if (backdrop == null) {
    background(fallbackColor, shape)
} else {
    drawBackdrop(
        backdrop = backdrop.layer,
        shape = { shape },
        effects = {
            vibrancy()
            blur(blurRadius.toPx())
            lens(lensRadius.toPx(), lensRadius.toPx())
        },
        onDrawSurface = { drawRect(surfaceColor) },
    )
}

object LiquidGlassDefaults {
    /** 叠在玻璃上的表面色，统一使用 58% 表面容器色，降低正文穿透对按钮可读性的干扰。 */
    val surfaceColor: Color
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.58f)
}
