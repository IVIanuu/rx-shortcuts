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
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Timer;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

/**
 * @author Manuel Wrage (IVIanuu)
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
    public static RxShortcuts create(@NonNull Activity activity) {
        return new RxShortcuts(activity);
    }

    /**
     * Emits on shortcut selection
     */
    public Single<Shortcut> requestShortcut(int requestCode) {
        return requestShortcut(requestCode, rxShortcutsFragment.getString(R.string.default_picker_title));
    }

    /**
     * Emits on shortcut selection
     */
    public Single<Shortcut> requestShortcut(final int requestCode, @NonNull final String pickerTitle) {
        PublishSubject<Shortcut> subject = rxShortcutsFragment.getSubjectByRequestCode(requestCode);
        if (subject == null) {
            // create a new subject
            subject = PublishSubject.create();
            rxShortcutsFragment.setSubjectForRequestCode(requestCode, subject);
        }

        // request the shortcut
        final PublishSubject<Shortcut> finalSubject = subject;
        return Single.create(new SingleOnSubscribe<Shortcut>() {
            @Override
            public void subscribe(final SingleEmitter<Shortcut> e) throws Exception {
                finalSubject.subscribe(new Consumer<Shortcut>() {
                    @Override
                    public void accept(Shortcut shortcut) throws Exception {
                        if (!e.isDisposed()) {
                            e.onSuccess(shortcut);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!e.isDisposed()) {
                            e.onError(throwable);
                        }
                    }
                });

                rxShortcutsFragment.requestShortcut(requestCode, pickerTitle);
            }
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
