package com.dani_juarez.undirlaflota;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private final int gridSize = 10;  // Tamaño de la cuadrícula (10x10)
    private final int cellMargin = 4; // Margen entre celdas
    private final int textSize = 18;  // Tamaño de texto

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableLayout = findViewById(R.id.tableLayout);
        initializeGrid(gridSize, cellMargin, textSize);
    }

    private void initializeGrid(int gridSize, int cellMargin, int textSize) {

        TableRow headerRow = new TableRow(this);
        headerRow.addView(new TextView(this));

        // Fila amb les lletres del tauler
        for (int j = 0; j < gridSize; j++) {
            TextView textView = new TextView(this);
            textView.setText(String.valueOf((char) ('A' + j)));
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(textSize);
            TableRow.LayoutParams params = new TableRow.LayoutParams();
            params.setMargins(cellMargin, cellMargin, cellMargin, cellMargin);
            textView.setLayoutParams(params);
            headerRow.addView(textView);
        }
        tableLayout.addView(headerRow);

        // Cuadricula (Botons y columna de numeros)
        for (int i = 0; i < gridSize; i++) {
            TableRow row = new TableRow(this);

            // Columna dels numeros
            TextView textView = new TextView(this);
            textView.setText(String.valueOf(i + 1));
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(textSize);
            TableRow.LayoutParams params = new TableRow.LayoutParams();
            params.setMargins(cellMargin, cellMargin, cellMargin, cellMargin);
            textView.setLayoutParams(params);
            row.addView(textView);

            // Botons de la cuadricula
            for (int j = 0; j < gridSize; j++) {
                Button button = new Button(this);
                button.setText("");
                TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                buttonParams.setMargins(cellMargin, cellMargin, cellMargin, cellMargin);
                button.setLayoutParams(buttonParams);

                int finalI = i;
                int finalJ = j;
                button.setOnClickListener(v -> onCellClicked(finalI, finalJ));

                row.addView(button);
            }

            tableLayout.addView(row);
        }
    }

    private void onCellClicked(int row, int col) {
        char columnLetter = (char) ('A' + col);
        Toast.makeText(this, "Celda: (" + (row + 1) + ", " + columnLetter + ")", Toast.LENGTH_SHORT).show();
    }
}
