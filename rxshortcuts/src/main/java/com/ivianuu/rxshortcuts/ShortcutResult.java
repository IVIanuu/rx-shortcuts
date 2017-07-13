/*
 * Copyright 2017 Manuel Wrage
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ivianuu.rxshortcuts;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import static com.ivianuu.rxshortcuts.ShortcutResult.ResultCode.CANCELLED;
import static com.ivianuu.rxshortcuts.ShortcutResult.ResultCode.FAILED;
import static com.ivianuu.rxshortcuts.ShortcutResult.ResultCode.SUCCESS;

/**
 * Represents a shortcut result
 */
public class ShortcutResult {

    @IntDef(value = {SUCCESS, CANCELLED, FAILED})
    public @interface ResultCode {
        int SUCCESS = 0;
        int CANCELLED = 1;
        int FAILED = 2;
    }

    private int requestCode;
    private int resultCode;
    private Shortcut shortcut;

    /**
     * Instantiates a new shortcut result
     */
    public ShortcutResult(int requestCode, @ResultCode int resultCode, @Nullable Shortcut shortcut) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.shortcut = shortcut;
    }

    /**
     * Returns the request code of this result
     */
    public int getRequestCode() {
        return requestCode;
    }

    /**
     * Returns the result code of this result
     */
    public int getResultCode() {
        return resultCode;
    }

    /**
     * Returns the shortcut of the result
     * Is only non null if result code is success
     */
    @Nullable
    public Shortcut getShortcut() {
        return shortcut;
    }

    /**
     * Returns if the request code of this result equals the passed one
     */
    public boolean isRequestCode(int requestCode) {
        return this.requestCode == requestCode;
    }

    /**
     * Returns if the result is success or not
     */
    public boolean isSuccess() {
        return resultCode == ResultCode.SUCCESS;
    }
}
