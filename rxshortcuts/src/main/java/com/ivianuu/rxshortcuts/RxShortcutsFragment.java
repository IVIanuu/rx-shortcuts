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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Handles the activity result flow
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public final class RxShortcutsFragment extends Fragment {

    private Map<Integer, PublishSubject<Shortcut>> subjects = new HashMap<>();

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

    PublishSubject<Shortcut> getSubjectByRequestCode(int requestCode) {
        return subjects.get(requestCode);
    }

    void setSubjectForRequestCode(int requestCode, @NonNull PublishSubject<Shortcut> subject) {
        subjects.put(requestCode, subject);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (!subjects.containsKey(requestCode)) {
            // were only interested in our own requests
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
                PublishSubject<Shortcut> subject = subjects.remove(requestCode);
                subject.onError(e);
                return;
            }
        }

        // remove pending request code
        PublishSubject<Shortcut> subject = subjects.remove(requestCode);

        // handle intent
        Bitmap icon = Util.createShortcutIconString(getActivity(), data);
        String name = data.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);

        // notify subject
        Shortcut shortcut = new Shortcut(icon, shortcutIntent, name);
        subject.onNext(shortcut);
    }

}
