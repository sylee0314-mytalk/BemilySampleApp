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

package com.olivia.architecturetemplate.util.encoder;


import com.olivia.architecturetemplate.util.BemilyGifException;

/**
 * Created by GTPark on 2017-01-03.
 */

abstract public class EncodingListener {
    abstract public void onSuccess();

    abstract public void onError(BemilyGifException e);

    /**
     * if you need 'on progress' callback, must override this method
     */
    public void onProgress(double progress) {
    }

    /**
     * for queueing encoding
     */
    public void onStop(int total) {
    }

    public void onFrameProgress(int current, int total) {
    }
}



