/*
 * Copyright 2022 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.blockchain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class NftAssetsResponseItem(

    @field:SerializedName("one_day_average_price")
    val oneDayAveragePrice: String,

    @field:SerializedName("total_volume")
    val totalVolume: String,

    @field:SerializedName("image_thumbnail_url")
    val imageThumbnailUrl: String,

    @field:SerializedName("one_day_volume")
    val oneDayVolume: String,

    @field:SerializedName("image_preview_url")
    val imagePreviewUrl: String,

    @field:SerializedName("payment_token")
    val paymentToken: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("one_day_sales")
    val oneDaySales: String,

    @field:SerializedName("usd_sale_price")
    val usdSalePrice: String,

    @field:SerializedName("large_image_url")
    val largeImageUrl: String,

    @field:SerializedName("one_day_change")
    val oneDayChange: String,

    @field:SerializedName("market_cap")
    val marketCap: String,

    @field:SerializedName("token_id")
    val tokenId: String,

    @field:SerializedName("collection_slug")
    val collectionSlug: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("total_sales")
    val totalSales: Int,

    @field:SerializedName("slug")
    val slug: String,

    @field:SerializedName("owner")
    val owner: String,

    @field:SerializedName("creator")
    val creator: String,

    @field:SerializedName("animation_url")
    val animationUrl: String?,

    @field:SerializedName("sale_timestamp")
    val saleTimestamp: String,

    @field:SerializedName("num_sales")
    val numSales: Int,

    @field:SerializedName("total_supply")
    val totalSupply: Int,

    @field:SerializedName("image_url")
    val imageUrl: String,

    @field:SerializedName("display_style")
    val displayStyle: String,

    @field:SerializedName("count")
    val count: Int,

    @field:SerializedName("average_price")
    val averagePrice: String,

    @field:SerializedName("created_timestamp")
    val createdTimestamp: String,

    @field:SerializedName("crypto_sale_price")
    val cryptoSalePrice: String,

    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("image_original_url")
    val imageOriginalUrl: String,

    @field:SerializedName("animation_original_url")
    val animationOriginalUrl: String?,

    @field:SerializedName("num_owners")
    val numOwners: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("added_timestamp")
    val addedTimestamp: String,

    @field:SerializedName("floor_price")
    val floorPrice: String
) : Parcelable
