package com.android.example.wordlistsql;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;

public class MyButtonOnClickListener implements View.OnClickListener {
    private int id;
    private String word;
    private Context context;
    private WordListOpenHelper mDB;

    public MyButtonOnClickListener(int id, String word, Context context, WordListOpenHelper db) {
        this.id = id;
        this.word = word;
        this.context = context;
        this.mDB = db;
    }

    @Override
    public void onClick(View v) {
        Log.d("DeleteButton", "Botón de eliminar presionado para: " + word);

        new AlertDialog.Builder(context)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Seguro que quieres eliminar \"" + word + "\"?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    int result = mDB.delete(id);
                    Log.d("DeleteButton", "Palabra eliminada: " + word + " (id: " + id + ")");
                    if (result > 0) {
                        ((MainActivity) context).updateUI(); // Actualiza la UI después de eliminar
                    }
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
