package com.dani_juarez.undirlaflota;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private ExecutorService executorService;

    private TableLayout tableLayout;
    private final int gridSize = 10;  // Tamaño de la cuadrícula (10x10)
    private final int cellMargin = 4; // Margen entre celdas
    private final int textSize = 18;  // Tamaño de texto

    private UtilsWS wsClient = null;
    private String location = "wss://apalaci8.ieti.site";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableLayout = findViewById(R.id.tableLayout);
        initializeGrid(gridSize, cellMargin, textSize);

        Button connectButton = findViewById(R.id.connectButton);
        connectButton.setOnClickListener(v -> {
            connectToUrl();
        });

        Button bounceButton = findViewById(R.id.bounceButton);
        bounceButton.setOnClickListener(v -> {
            try {
                bounceCall();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
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
                button.setOnClickListener(v -> {
                    try {
                        onCellClicked(finalI, finalJ);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                });

                row.addView(button);
            }

            tableLayout.addView(row);
        }
    }

    private void onCellClicked(int row, int col) throws JSONException {
        char columnLetter = (char) ('A' + col);
        int row_num = (row + 1);
        Toast.makeText(this, "Celda: (" + row_num + ", " + columnLetter + ")", Toast.LENGTH_SHORT).show();
        sendCellClick( row_num,columnLetter);
    }

    private void connectToUrl(){

        if (wsClient == null){
            wsClient = UtilsWS.getSharedInstance(location);

            wsClient.onMessage(message -> {

                runOnUiThread(() -> {
                    Toast.makeText(this, "Server Message: " + message, Toast.LENGTH_SHORT).show();
                    Log.d("WebSocket", "Server Message: " + message);
                });
            });
        }
    }

    private void bounceCall() throws JSONException {

        if (wsClient != null){
            JSONObject obj = new JSONObject();
            obj.put("type", "bounce");
            obj.put("destination", location);
            obj.put("message", "Aixo es una prova");
            wsClient.safeSend(obj.toString());
        }
    }

    private void sendCellClick(int row, int column) throws JSONException{
        if (wsClient != null) {
            JSONObject obj = new JSONObject();
            obj.put("type", "bounce");
            obj.put("destination", location);
            obj.put("message", "Celda: (" + row + ", " + column + ")");
            wsClient.safeSend(obj.toString());
        }
    }
}
