package com.android.example.wordlistsql;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    private final LayoutInflater mInflater;
    private WordListOpenHelper mDB;
    private Context mContext;

    public static final String EXTRA_ID = "ID";
    public static final String EXTRA_WORD = "WORD";

    public WordListAdapter(Context context, WordListOpenHelper db) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mDB = db;
    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        public final TextView wordItemView;
        Button delete_button, edit_button;

        public WordViewHolder(View itemView) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.word);
            delete_button = itemView.findViewById(R.id.delete_button);
            edit_button = itemView.findViewById(R.id.edit_button);
        }
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.wordlist_item, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        WordItem current = mDB.query(position);
        if (current != null) {
            holder.wordItemView.setText(current.getWord());

            // Listener para eliminar palabra
            holder.delete_button.setOnClickListener(view -> {
                new android.app.AlertDialog.Builder(mContext)
                        .setTitle("Confirmar eliminación")
                        .setMessage("¿Quieres eliminar \"" + current.getWord() + "\"?")
                        .setPositiveButton("Eliminar", (dialog, which) -> {
                            mDB.delete(current.getId());
                            ((MainActivity) mContext).updateUI();
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            });

            // Listener para editar palabra
            holder.edit_button.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, EditWordActivity.class);
                intent.putExtra(EXTRA_ID, current.getId());
                intent.putExtra(EXTRA_WORD, current.getWord());
                ((MainActivity) mContext).startActivityForResult(intent, MainActivity.WORD_EDIT);
            });

        } else {
            holder.wordItemView.setText("Sin datos");
        }
    }

    @Override
    public int getItemCount() {
        return (int) mDB.count();
    }
}
