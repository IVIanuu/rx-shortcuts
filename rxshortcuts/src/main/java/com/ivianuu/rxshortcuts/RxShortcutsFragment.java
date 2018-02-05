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

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import java.util.HashMap;

import io.reactivex.subjects.PublishSubject;

/**
 * Handles the activity result flow
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public final class RxShortcutsFragment extends Fragment {

    private final HashMap<Integer, PublishSubject<ShortcutResult>> subjects = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    void requestShortcut(int requestCode, @NonNull String pickShortcutString) {
        // request a shortcut
        Intent pickIntent = new Intent(Intent.ACTION_PICK_ACTIVITY);
        pickIntent.putExtra(Intent.EXTRA_INTENT, new Intent(Intent.ACTION_CREATE_SHORTCUT));
        pickIntent.putExtra(Intent.EXTRA_TITLE, pickShortcutString);
        startActivityForResult(pickIntent, requestCode);
    }

    @CheckResult @Nullable
    PublishSubject<ShortcutResult> getSubjectByRequestCode(int requestCode) {
        return subjects.get(requestCode);
    }

    void setSubjectForRequestCode(int requestCode, @NonNull PublishSubject<ShortcutResult> subject) {
        subjects.put(requestCode, subject);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (!subjects.containsKey(requestCode)) {
            // were only interested in our own requests
            return;
        }

        if (data == null) {
            // remove pending request code
            PublishSubject<ShortcutResult> subject = subjects.remove(requestCode);
            subject.onNext(new ShortcutResult(requestCode, ShortcutResult.ResultCode.Companion.getCANCELLED(), null));
            return;
        }

        Intent shortcutIntent = data.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT);
        if (shortcutIntent == null) {
            // we have do request the shortcut intent
            try {
                startActivityForResult(data, requestCode);
                return;
            } catch (ActivityNotFoundException e) {
                // remove pending request code
                PublishSubject<ShortcutResult> subject = subjects.remove(requestCode);
                subject.onNext(new ShortcutResult(requestCode, ShortcutResult.ResultCode.Companion.getFAILED(), null));
                return;
            }
        }

        // remove pending request code
        PublishSubject<ShortcutResult> subject = subjects.remove(requestCode);

        // handle intent
        Bitmap icon
                = data.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON);
        Intent.ShortcutIconResource iconResource
                = data.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE);
        String name = data.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);

        // notify subject
        Shortcut shortcut = new Shortcut(icon, iconResource, shortcutIntent, name);
        subject.onNext(new ShortcutResult(requestCode, ShortcutResult.ResultCode.Companion.getSUCCESS(), shortcut));
    }

}
