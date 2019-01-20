package com.janhelmich.crius;

import android.content.Context;

import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ShapeFactory;

public class Grid extends Node {

    public static final float BORDER_WIDTH = 0.01f;
    public static final float GAP_FACTOR = 0.9f;

    private GameState gameState;

    private final Node center;
    private final float lineLength;
    private final int rowCount;
    private final int columnCount;
    private final int height;
    private final Context context;

    Long time;

    private Node[][][] nodeGrid;


    public Grid(Node center, float lineLength, int rowCount, int columnCount, int height, Context context, GameState gameState) {

        this.setParent(center);
        this.center = center;
        this.setLocalPosition(new Vector3(0.0f,0.0f,0.0f));
        this.lineLength = lineLength;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.height = height;
        this.context = context;
        this.gameState = gameState;

        nodeGrid = new Node[rowCount][columnCount][height];
        time = System.currentTimeMillis();
    }


    public void addGrid() {

        addBottomPlane();
        addTopPlane();
        //fillTotalGrid();
    }

    public void fillTotalGrid() {
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                for (int q = 0; q < height; q++) {

                    final Color color = new Color(0.0f, 0.0f, 0.0f, 0.7f);

                    Node cube = makeCube(color);

                    cube.setLocalPosition(new Vector3(((columnCount / 2) - j) * lineLength, q * lineLength+0.5f*lineLength, ((rowCount / 2) - i) * lineLength));
                    cube.setParent(this);
                    nodeGrid[i][j][q] = cube;

                }
            }
        }
    }


    public void fillGameStateGrid() {

        addBottomPlane();
        addTopPlane();
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                for (int q = 0; q < height; q++) {

                    nodeGrid[i][j][q] = null;
                    if (nodeGrid[i][j][q] != null) {
                        nodeGrid[i][j][q].setParent(null);
                    }

                    if (gameState.grid[i][j][q] != null) {
                        final Color color = gameState.grid[i][j][q];

                        Node cube = makeCube(color);
                        cube.setLocalPosition(new Vector3(((columnCount / 2) - j) * lineLength, q * lineLength + 0.5f * lineLength, ((rowCount / 2) - i) * lineLength));
                        cube.setParent(this);
                        nodeGrid[i][j][q] = cube;
                    }


                }
            }
        }
    }

    private void addBottomPlane() {
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {

                Node cube = makeOutlineCube(new Vector3(lineLength*GAP_FACTOR, BORDER_WIDTH, lineLength*GAP_FACTOR));

                cube.setLocalPosition(new Vector3(((columnCount / 2) - j) * lineLength, -1*BORDER_WIDTH, ((rowCount / 2) - i) * lineLength));
                cube.setParent(this);
            }
        }
    }

    private void addTopPlane() {
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {

                Node cube = makeOutlineCube(new Vector3(lineLength*GAP_FACTOR, BORDER_WIDTH, lineLength*GAP_FACTOR));

                cube.setLocalPosition(new Vector3(((columnCount / 2) - j) * lineLength, height*lineLength, ((rowCount / 2) - i) * lineLength));
                cube.setParent(this);
            }
        }
    }


    private Node makeCube(Color color) {
        Node cube = new Node();

        final Renderable[] cubeRenderable = new Renderable[1];
        MaterialFactory.makeTransparentWithColor(context, color)
                .thenAccept(
                        material -> {
                            cubeRenderable[0] = ShapeFactory.makeCube(new Vector3(lineLength*GAP_FACTOR,lineLength*GAP_FACTOR,lineLength*GAP_FACTOR),
                                    Vector3.zero(), material);
                        });

        cube.setRenderable(cubeRenderable[0]);
        return cube;
    }


    private Node makeOutlineCube(Vector3 dimensions) {
        Node cube = new Node();

        final Color c = new Color(1.0f, 1.0f, 1.0f, 0.4f);

        final Renderable[] cubeRenderable = new Renderable[1];
        MaterialFactory.makeTransparentWithColor(context, c)
                .thenAccept(
                        material -> {
                            cubeRenderable[0] = ShapeFactory.makeCube(dimensions,
                                    Vector3.zero(), material);
                        });

        cube.setRenderable(cubeRenderable[0]);
        return cube;
    }



    @Override
    public void onUpdate(FrameTime frameTime) {
        if (time+100 < System.currentTimeMillis()) {
            gameState.updateState();
            fillGameStateGrid();
            time = System.currentTimeMillis();
        }
    }

}

