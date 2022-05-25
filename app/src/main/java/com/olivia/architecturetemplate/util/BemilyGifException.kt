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

import java.io.Serializable

/**
 * Created by Naver on 2016-09-29.
 */
class BemilyGifException(error: BemilyGifError) :
    RuntimeException(), Serializable {
    private val error: com.olivia.architecturetemplate.util.BemilyGifError
    fun getError(): com.olivia.architecturetemplate.util.BemilyGifError {
        return error
    }

    override val message: String
        get() = error.message
    val code: Int
        get() = error.code

    init {
        this.error = error
    }
}