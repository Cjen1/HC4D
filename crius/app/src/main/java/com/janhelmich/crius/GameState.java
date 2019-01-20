package com.janhelmich.crius;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.ar.sceneform.rendering.Color;
import com.janhelmich.ar.smarthome.RequestSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class GameState {

    public static final String BASE_URL = "http://192.168.43.177:5000/tetris/";


    private final int rows;
    private final int columns;
    private final int height;

    private int score;

    private final Context context;

    public Color[][][] grid;

    public GameState(int rows, int columns, int height, Context context) {
        this.rows = rows;
        this.columns = columns;
        this.height = height;
        this.context = context;

        score = 0;

        grid = new Color[rows][columns][height];

        grid[0][0][0] = new Color(0.0f, 0.0f, 1.0f, 0.5f);
        grid[1][1][1] = new Color(0.0f, 1.0f, 0.0f, 0.5f);
    }

    public void startGame() {
        // TODO: MAKE API CALL TO START GAME
        JsonObjectRequest jsonObjectRequest;
        jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, BASE_URL + "init/" + rows + "/" + columns + "/" + height, null, response -> {
                    // DO NOTHING HERE
                }, error -> {
                    //Log.i("Request Error", error.getMessage());
                });

        // Access the RequestQueue through your singleton class.
        RequestSingleton.getInstance(this.context).addToRequestQueue(jsonObjectRequest);
    }

    public void updateState() {
        JsonObjectRequest jsonObjectRequest;
        jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, BASE_URL + "state", null, response -> {
                    try {
                        JSONArray dimensions = (JSONArray) response.get("dim");
                        int score = (int) response.get("score");
                        this.score = score;
                        JSONArray board = (JSONArray) response.get("board");
                        String[][][] stringBoard = new String[columns][height][rows];
                        for (int i = 0; i < board.length(); i++) {
                            for (int j = 0; j < ((JSONArray) board.get(i)).length(); j++) {
                                for (int k = 0; k < ((JSONArray) ((JSONArray) board.get(i)).get(j)).length(); j++) {
                                    stringBoard[i][j][k] = (String) ((JSONArray) ((JSONArray) board.get(i)).get(j)).get(k);
                                }
                            }
                        }
                        Log.i("BOARD", stringBoard.toString());
                        updateBoard(stringBoard);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    //Log.i("Request Error", error.getMessage());
                });
        // Access the RequestQueue through your singleton class.
        // {"dim": {"lz": 3, "lx": 1, "ly": 2}, "score": 0, "board": [[[0, 0, 0], [0, 0, 0]]]}

        // Access the RequestQueue through your singleton class.
        RequestSingleton.getInstance(this.context).addToRequestQueue(jsonObjectRequest);
    }

    public void updateBoard(String[][][] board) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                for (int altitude = 0; altitude < height; altitude++) {
                    // TODO: ADJUST MAPPING ACCORDING TO JSON FORMAT
                    if (!board[col][altitude][row].equals("")) {
                        grid[row][col][altitude] = hex2Rgb(board[col][altitude][row]);
                    } else {
                        grid[row][col][altitude] = null;
                    }
                }
            }
        }

    }

    public static Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
    }
}
