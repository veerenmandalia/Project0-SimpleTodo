package com.codepath.simpletodo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.simpletodo.R;

public class EditItemActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = EditItemActivity.class.getSimpleName();
    private EditText item;
    private String originalText;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        item = (EditText) findViewById(R.id.item);

        Intent intent = getIntent();
        if (intent != null) {
            originalText = intent.getStringExtra(MainActivity.KEY_EDIT_ITEM);
            position = intent.getIntExtra(MainActivity.KEY_EDIT_ITEM_POSITION, -1);
            item.setText(originalText);
        } else {
            Log.d(TAG, "intent is null");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                String text = item.getText().toString();
                if (!text.equals(originalText)) {
                    Intent data = new Intent();
                    data.putExtra(MainActivity.KEY_EDIT_ITEM, text);
                    data.putExtra(MainActivity.KEY_EDIT_ITEM_POSITION, position);
                    setResult(RESULT_OK, data);
                }
                finish();
                break;
            default:
                break;
        }
    }
}
