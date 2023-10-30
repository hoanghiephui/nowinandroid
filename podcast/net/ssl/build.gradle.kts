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
    namespace = "com.podcast.net.ssl"
}

dependencies {
    implementation(libs.androidx.annotation)
    implementation(libs.okhttp)
    implementation("com.google.android.gms:play-services-base:18.2.0")
    implementation("org.conscrypt:conscrypt-android:2.5.2")
}
