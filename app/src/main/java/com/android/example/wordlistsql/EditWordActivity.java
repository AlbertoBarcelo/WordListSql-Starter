package com.android.example.wordlistsql;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditWordActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.android.example.wordlistsql.REPLY";
    private EditText mEditWordView;
    private int mId = MainActivity.WORD_ADD; // Si es -1, significa que estamos agregando

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word);

        mEditWordView = findViewById(R.id.edit_word);

        // Verifica si estamos editando o creando una palabra nueva
        if (getIntent().hasExtra(WordListAdapter.EXTRA_ID)) {
            mId = getIntent().getIntExtra(WordListAdapter.EXTRA_ID, MainActivity.WORD_ADD);
            String word = getIntent().getStringExtra(WordListAdapter.EXTRA_WORD);
            mEditWordView.setText(word); // Llena el campo si es edición
        }

        // Configura el listener del botón SAVE
        findViewById(R.id.button_save).setOnClickListener(this::returnReply);
    }

    public void returnReply(View view) {
        String word = mEditWordView.getText().toString().trim(); // Elimina espacios en blanco

        if (word.isEmpty()) {
            Toast.makeText(this, "La palabra no puede estar vacía", Toast.LENGTH_SHORT).show();
            return; // Evita que se guarde una palabra vacía
        }

        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_REPLY, word);
        replyIntent.putExtra(WordListAdapter.EXTRA_ID, mId); // Si es -1, es una nueva palabra
        setResult(RESULT_OK, replyIntent);
        finish(); // Cierra la actividad
    }
}
