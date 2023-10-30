/*
 * Copyright 2023 The Android Open Source Project
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

plugins {
    id("nowinandroid.android.library")
}

android {
    namespace = "com.podcast.core"
}

dependencies {
    api(projects.podcast.model)
    implementation(projects.podcast.event)
    implementation(projects.podcast.net.common)
    implementation(projects.podcast.net.download)
    implementation(projects.podcast.net.ssl)
    implementation(projects.podcast.storage.database)
    api(projects.podcast.parser.feed)
    implementation(projects.podcast.storage.preferences)

    implementation(libs.okhttp)
    implementation(libs.okhttp.urlconnection)
    implementation(libs.okio)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.appcompat)
    implementation(libs.apache.common)
    implementation(libs.commons.io)
    implementation(libs.jsoup)

}
