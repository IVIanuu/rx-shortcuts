package com.ivianuu.rxshortcuts.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ivianuu.rxshortcuts.RxShortcuts;
import com.ivianuu.rxshortcuts.Shortcut;
import com.ivianuu.rxshortcuts.ShortcutResult;

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

        requestShortcut.setOnClickListener(view ->
                rxShortcuts.requestShortcut(111)
                        .filter(shortcutResult -> {
                            if (!shortcutResult.isSuccess()) {
                                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                            }
                            return shortcutResult.isSuccess();
                        })
                        .map(ShortcutResult::getShortcut)
                        .subscribe(this::handleShortcutResult));
    }

    private void handleShortcutResult(final Shortcut shortcut) {
        shortcutTitle.setText(shortcut.getName());
        executeShortcut.setOnClickListener(view -> startActivity(shortcut.getIntent()));
    }
}
