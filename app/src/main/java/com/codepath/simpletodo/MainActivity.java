package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_EDIT_ITEM = 1000;
    public static final String KEY_EDIT_ITEM = "KEY_EDIT_ITEM";
    public static final String KEY_EDIT_ITEM_POSITION = "KEY_EDIT_ITEM_POSITION";

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called with: " + "savedInstanceState = [" + savedInstanceState +
                "]");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.etNewItem);
        findViewById(R.id.btnAddItem).setOnClickListener(this);
        lvItems = (ListView) findViewById(R.id.lvItems);

        readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout
                .simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddItem:
                String text = editText.getText().toString();
                if (text.isEmpty()) {
                    Log.d(TAG, "Not adding blank item");
                    break;
                }
                itemsAdapter.add(text);
                editText.setText("");
                writeItems();
                break;
            default:
                break;
        }
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long
                    id) {
                Log.d(TAG, "Removing item at position " + position);
                items.remove(position);
                listUpdated();
                return false;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = items.get(position);
                Log.d(TAG, String.format("Edit item at position %d: %s", position, item));
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                intent.putExtra(KEY_EDIT_ITEM, item);
                intent.putExtra(KEY_EDIT_ITEM_POSITION, position);
                startActivityForResult(intent, REQUEST_CODE_EDIT_ITEM);
            }
        });
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            Log.e(TAG, "Error reading file, creating new list", e);
            items = new ArrayList<>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            Log.e(TAG, "", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                Log.d(TAG, "onActivityResult: OK");
                String newText = data.getStringExtra(KEY_EDIT_ITEM);
                int position = data.getIntExtra(KEY_EDIT_ITEM_POSITION, -1);
                items.set(position, newText);
                listUpdated();
                break;
            case RESULT_CANCELED:
                Log.d(TAG, "onActivityResult: CANCELED");
                break;
            default:
                Log.d(TAG, "onActivityResult: Unknown: " + resultCode);
        }
    }

    private void listUpdated() {
        itemsAdapter.notifyDataSetChanged();
        writeItems();
    }
}
