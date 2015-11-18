package com.codepath.simpletodo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.activities.backend.Configuration;
import com.codepath.simpletodo.activities.utility.FileUtility;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView
        .OnItemLongClickListener, AdapterView.OnItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_EDIT_ITEM = 1000;
    public static final String KEY_EDIT_ITEM = "KEY_EDIT_ITEM";
    public static final String KEY_EDIT_ITEM_POSITION = "KEY_EDIT_ITEM_POSITION";

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView listView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.edit_text_new_item);
        findViewById(R.id.button_add_item).setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listview);

        items = FileUtility.readItems(this, Configuration.TODO_ITEMS_FILENAME);
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout
                .simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_item:
                String text = editText.getText().toString();
                if (text.isEmpty()) {
                    Log.d(TAG, "Not adding empty item");
                    break;
                }
                itemsAdapter.add(text);
                editText.setText("");
                FileUtility.writeItems(this, items, Configuration.TODO_ITEMS_FILENAME);
                break;
            default:
                break;
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
        FileUtility.writeItems(this, items, Configuration.TODO_ITEMS_FILENAME);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "Removing item at position " + position);
        items.remove(position);
        listUpdated();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item = items.get(position);
        Log.d(TAG, String.format("Edit item at position %d: %s", position, item));
        Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
        intent.putExtra(KEY_EDIT_ITEM, item);
        intent.putExtra(KEY_EDIT_ITEM_POSITION, position);
        startActivityForResult(intent, REQUEST_CODE_EDIT_ITEM);
    }
}
