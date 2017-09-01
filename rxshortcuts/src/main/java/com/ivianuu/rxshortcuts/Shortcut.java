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

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

/**
 * Represents a shortcut
 */
public final class Shortcut {

    private Bitmap icon;
    private Intent intent;
    private String name;

    /**
     * Instantiates a new shortcut
     */
    Shortcut(@NonNull Bitmap icon, @NonNull Intent intent, @NonNull String name) {
        this.icon = icon;
        this.intent = intent;
        this.name = name;
    }

    /**
     * Returns the icon of this shortcut
     */
    @NonNull
    public Bitmap getIcon() {
        return icon;
    }

    /**
     * Returns the intent of this shortcut
     */
    @NonNull
    public Intent getIntent() {
        return intent;
    }

    /**
     * Returns the name of this shortcut
     */
    @NonNull
    public String getName() {
        return name;
    }

}
