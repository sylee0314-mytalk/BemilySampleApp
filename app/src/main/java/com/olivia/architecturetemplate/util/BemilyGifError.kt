/*
Copyright 2018 NAVER Corp.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.olivia.architecturetemplate.util

/**
 * Created by Naver on 2016-09-29.
 */
enum class BemilyGifError(val code: Int, val message: String) {
    FAILED_TO_LOAD_BACKGROUND(101, "failed to load background"), FAILED_TO_LOAD_IMAGE(
        102,
        "failed to load image"
    ),
    FAILED_TO_SAVE_IMAGE(103, "failed to save image"), FAILED_TO_COMPOSITE_IMAGE(
        105,
        "failed to composite image"
    ),
    FAILED_TO_CREATE_GIF(106, "failed to create gif"), FAILED_TO_CREATE_GIF_NO_FRAME(
        107,
        "failed to create gif. There is no frame to create a gif. must be at least one"
    ),
    UNKNOWN_META_TYPE(109, "unknown meta type"), FRAME_INDEX_OUT_OF_BOUND(
        110,
        "out of bound frame index"
    ),
    INVALID_COLOR_LEVEL_VALUE(120, "invalid color level. (6 to 8)"), INVALID_MAX_WIDTH_RATIO_VALUE(
        130,
        "invalid max width ratio. (0.5f to 0.9f)"
    ),
    INVALID_URI(140, "invalid uri");

}