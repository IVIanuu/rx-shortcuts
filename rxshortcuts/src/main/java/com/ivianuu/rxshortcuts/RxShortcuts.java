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

import android.app.Activity;
import android.app.FragmentManager;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

import static com.ivianuu.rxshortcuts.Preconditions.checkNotNull;

/**
 * Rxshortcuts
 */
public final class RxShortcuts {

    private static final String TAG = "RxShortcuts";

    private RxShortcutsFragment rxShortcutsFragment;

    private RxShortcuts(Activity activity) {
        rxShortcutsFragment = getRxShortcutFragment(activity);
    }

    /**
     * Returns a new RxShortcuts instance
     */
    @NonNull
    public static RxShortcuts create(@NonNull Activity activity) {
        checkNotNull(activity, "activity == null");
        return new RxShortcuts(activity);
    }

    /**
     * Emits on shortcut selection
     */
    @CheckResult @NonNull
    public Single<ShortcutResult> requestShortcut(int requestCode) {
        return requestShortcut(requestCode, "");
    }

    /**
     * Emits on shortcut selection
     */
    @CheckResult @NonNull
    public Single<ShortcutResult> requestShortcut(final int requestCode, @NonNull final String pickerTitle) {
        checkNotNull(pickerTitle, "pickerTitle == null");
        PublishSubject<ShortcutResult> subject = rxShortcutsFragment.getSubjectByRequestCode(requestCode);
        if (subject == null) {
            // create a new subject
            subject = PublishSubject.create();
            rxShortcutsFragment.setSubjectForRequestCode(requestCode, subject);
        }

        // request the shortcut
        final PublishSubject<ShortcutResult> finalSubject = subject;
        return Single.create(e -> {
            finalSubject.subscribe(shortcutResult -> {
                if (!e.isDisposed()) {
                    e.onSuccess(shortcutResult);
                }
            }, throwable -> {
                if (!e.isDisposed()) {
                    e.onError(throwable);
                }
            });

            rxShortcutsFragment.requestShortcut(requestCode, pickerTitle);
        });
    }

    private RxShortcutsFragment getRxShortcutFragment(Activity activity) {
        RxShortcutsFragment rxShortcutsFragment = findRxShortcutsFragment(activity);
        boolean newInstance = rxShortcutsFragment == null;
        if (newInstance) {
            rxShortcutsFragment = new RxShortcutsFragment();
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(rxShortcutsFragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return rxShortcutsFragment;
    }

    private RxShortcutsFragment findRxShortcutsFragment(Activity activity) {
        return (RxShortcutsFragment) activity.getFragmentManager().findFragmentByTag(TAG);
    }
}
