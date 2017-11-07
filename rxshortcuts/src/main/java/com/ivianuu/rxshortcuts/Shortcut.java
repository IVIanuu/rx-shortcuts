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
import android.support.annotation.NonNull;

/**
 * Represents a shortcut
 */
public final class Shortcut {

    private Intent.ShortcutIconResource iconResource;
    private Intent intent;
    private String name;

    /**
     * Instantiates a new shortcut
     */
    Shortcut(@NonNull Intent.ShortcutIconResource iconResource, @NonNull Intent intent, @NonNull String name) {
        this.iconResource = iconResource;
        this.intent = intent;
        this.name = name;
    }

    /**
     * Returns the icon res of this shortcut
     */
    @NonNull public Intent.ShortcutIconResource getIconResource() {
        return iconResource;
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
