package com.android.example.wordlistsql;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Intent;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    public static final int WORD_EDIT = 1;
    public static final int WORD_ADD = -1;

    private RecyclerView mRecyclerView;
    private WordListAdapter mAdapter;
    private WordListOpenHelper mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDB = new WordListOpenHelper(this);
        mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new WordListAdapter(this, mDB);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> startActivityForResult(new Intent(this, EditWordActivity.class), WORD_EDIT));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == WORD_EDIT && resultCode == RESULT_OK) {
            String word = data.getStringExtra(EditWordActivity.EXTRA_REPLY);
            int id = data.getIntExtra(WordListAdapter.EXTRA_ID, WORD_ADD);

            if (word != null && !word.isEmpty()) {
                if (id == WORD_ADD) {
                    long newId = mDB.insert(word);
                    if (newId != -1) {
                        Log.d("MainActivity", "Nueva palabra añadida: " + word);
                    } else {
                        Log.e("MainActivity", "Error al insertar la palabra.");
                    }
                } else {
                    int updatedRows = mDB.update(id, word);
                    if (updatedRows > 0) {
                        Log.d("MainActivity", "Palabra actualizada: " + word + " (ID: " + id + ")");
                    } else {
                        Log.e("MainActivity", "Error al actualizar la palabra.");
                    }
                }
                updateUI();
            } else {
                Log.d("MainActivity", "Palabra vacía, no se guardó.");
            }
        }
    }

    public void updateUI() {
        mAdapter.notifyDataSetChanged(); // Notifica cambios al RecyclerView
        Log.d("MainActivity", "Lista actualizada después de eliminar");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
