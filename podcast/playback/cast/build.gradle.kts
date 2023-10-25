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
    kotlin("kapt")
}
android {
    namespace = "com.podcast.playback.cast"
}

dependencies {
    implementation(libs.androidx.core)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.appcompat)
    implementation("androidx.mediarouter:mediarouter:1.6.0")
    implementation("com.google.android.gms:play-services-cast-framework:21.3.0")
    implementation("org.greenrobot:eventbus:3.3.1")
    kapt("org.greenrobot:eventbus-annotation-processor:3.3.1")
    implementation(project(":podcast:model"))
    implementation(project(":podcast:event"))
    implementation(project(":podcast:playback:base"))
}
