package com.ivianuu.rxshortcuts.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ivianuu.rxshortcuts.RxShortcuts;
import com.ivianuu.rxshortcuts.Shortcut;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private RxShortcuts rxShortcuts;

    private Button requestShortcut;
    private TextView shortcutTitle;
    private ImageView shortcutIcon;
    private Button executeShortcut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rxShortcuts = RxShortcuts.create(this);

        requestShortcut = findViewById(R.id.request_shortcut);
        shortcutTitle = findViewById(R.id.shortcut_title);
        shortcutIcon = findViewById(R.id.shortcut_icon);
        executeShortcut = findViewById(R.id.execute_shortcut);

        requestShortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxShortcuts.requestShortcut(111)
                        .subscribe(new Consumer<Shortcut>() {
                            @Override
                            public void accept(Shortcut shortcut) throws Exception {
                                handleShortcutResult(shortcut);
                            }
                        });
            }
        });
    }

    private void handleShortcutResult(final Shortcut shortcut) {
        shortcutTitle.setText(shortcut.getName());
        shortcutIcon.setImageBitmap(shortcut.getIcon());
        executeShortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(shortcut.getIntent());
            }
        });
    }
}
