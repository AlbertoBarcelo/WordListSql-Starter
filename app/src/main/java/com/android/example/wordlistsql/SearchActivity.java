package com.android.example.wordlistsql;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {

    private EditText mEditWordView;
    private TextView mTextView;
    private WordListOpenHelper mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mEditWordView = findViewById(R.id.search_word);
        mTextView = findViewById(R.id.search_result);
        Button searchButton = findViewById(R.id.button_search);

        mDB = new WordListOpenHelper(this);

        searchButton.setOnClickListener(this::showResult);
    }

    public void showResult(View view) {
        String word = mEditWordView.getText().toString().trim();

        if (word.isEmpty()) {
            mTextView.setText("Please enter a word to search.");
            return;
        }

        mTextView.setText("Results for: " + word + "\n\n");

        Cursor cursor = mDB.search(word);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                int index = cursor.getColumnIndex(WordListOpenHelper.KEY_WORD);
                String result = cursor.getString(index);
                mTextView.append(result + "\n");
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            mTextView.append("No results found.");
        }
    }
}
