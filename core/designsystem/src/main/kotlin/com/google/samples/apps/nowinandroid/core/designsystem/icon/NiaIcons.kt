/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.nowinandroid.core.designsystem.icon

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Upcoming
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Grid3x3
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.RepeatOne
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.ShortText
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.material.icons.rounded.ViewDay
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.samples.apps.nowinandroid.core.designsystem.R
import com.google.samples.apps.nowinandroid.core.designsystem.icon.Icon.DrawableResourceIcon
import com.google.samples.apps.nowinandroid.core.designsystem.icon.Icon.ImageVectorIcon

/**
 * Now in Android icons. Material icons are [ImageVector]s, custom icons are drawable resource IDs.
 */
object NiaIcons {
    val Add = Icons.Rounded.Add
    val ArrowBack = Icons.Rounded.ArrowBack
    val Bookmark = Icons.Rounded.Bookmark
    val BookmarkBorder = Icons.Rounded.BookmarkBorder
    val Bookmarks = Icons.Rounded.Bookmarks
    val BookmarksBorder = Icons.Outlined.Bookmarks
    val Check = Icons.Rounded.Check
    val Close = Icons.Rounded.Close
    val Grid3x3 = Icons.Rounded.Grid3x3
    val MoreVert = Icons.Default.MoreVert
    val Person = Icons.Rounded.Person
    val Search = Icons.Rounded.Search
    val Settings = Icons.Rounded.Settings
    val ShortText = Icons.Rounded.ShortText
    val Upcoming = Icons.Rounded.Upcoming
    val UpcomingBorder = Icons.Outlined.Upcoming
    val ViewDay = Icons.Rounded.ViewDay
}

object PodIcons {
    val Home = ImageVectorIcon(Rounded.Home)
    val Search = ImageVectorIcon(Icons.Rounded.Search)
    val Favorite = ImageVectorIcon(Icons.Rounded.Favorite)
    val FavoriteBorder = ImageVectorIcon(Icons.Rounded.FavoriteBorder)
    val FavoriteDrawable = ImageVectorIcon(Icons.Rounded.Favorite)
    val FavoriteBorderDrawable = ImageVectorIcon(Icons.Rounded.FavoriteBorder)
    val Settings = ImageVectorIcon(Icons.Rounded.Settings)
    val ArrowBack = ImageVectorIcon(Icons.Rounded.ArrowBack)
    val Clear = ImageVectorIcon(Icons.Rounded.Clear)
    val Close = ImageVectorIcon(Icons.Rounded.Close)
    val Music = ImageVectorIcon(Icons.Rounded.MusicNote)
    val Repeat = ImageVectorIcon(Icons.Rounded.Repeat)
    val RepeatOne = ImageVectorIcon(Icons.Rounded.RepeatOne)
    val Shuffle = ImageVectorIcon(Icons.Rounded.Shuffle)
    val SkipPrevious = ImageVectorIcon(Icons.Rounded.SkipPrevious)
    val Play = ImageVectorIcon(Icons.Outlined.PlayArrow)
    val Pause = ImageVectorIcon(Icons.Rounded.Pause)
    val SkipNext = ImageVectorIcon(Icons.Rounded.SkipNext)
    val Sort = ImageVectorIcon(Icons.Rounded.Sort)
    val Palette = ImageVectorIcon(Icons.Rounded.Palette)
    val DarkMode = ImageVectorIcon(Icons.Rounded.DarkMode)
    val Info = ImageVectorIcon(Icons.Rounded.Info)
    val Security = ImageVectorIcon(Icons.Rounded.Security)
}

sealed interface Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon
    data class DrawableResourceIcon(@DrawableRes val resourceId: Int) : Icon
}
