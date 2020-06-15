package com.codegym.games.game2048;

import com.codegym.engine.cell.*;
import com.codegym.engine.cell.Game;
import java.util.*;

public class Game2048 extends Game {

    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped = false;
    private int score = 0;

    private void createGame() {
        gameField = new int[SIDE][SIDE];
        createNewNumber();
        createNewNumber();

    }

    private void createNewNumber() {

        if (getMaxTileValue() == 2048) {
            win();
        }

        int x;
        int y;

        do {
            x = getRandomNumber(SIDE);
            y = getRandomNumber(SIDE);
        } while (gameField[x][y] != 0);

        int chance = getRandomNumber(10);

        if (chance == 9) {
            gameField[x][y] = 4;
        } else {
            gameField[x][y] = 2;

        }

    }

    private boolean compressRow(int[] row) {
        int[] tempRow = row.clone();
        boolean changesMade = false;
        for (int i = 0; i < row.length; i++) {
            for (int j = 0; j < row.length - 1; j++) {
                if (row[j] == 0) {
                    int temp = row[j];
                    row[j] = row[j + 1];
                    row[j + 1] = temp;
                }
            }
        }
        if (!Arrays.equals(row, tempRow)) {
            changesMade = true;
        }
        return changesMade;
    }

    private boolean mergeRow(int[] row) {
        boolean madeMove = false;
        for (int i = 0; i < row.length - 1; i++) {
            if (row[i] == row[i + 1] && row[i] != 0) {
                row[i] = 2 * row[i];
                row[i + 1] = 0;
                madeMove = true;
                score += row[i];
                setScore(score);
            }
        }
        return madeMove;
    }

    public void onKeyPress(Key key) {

        if (!canUserMove()) {
            gameOver();

        } else {
            if (isGameStopped && key == Key.SPACE) {
                isGameStopped = false;
                score = 0;
                setScore(score);
                createGame();
                drawScene();

            } else if (key == Key.LEFT && !isGameStopped) {
                moveLeft();
                drawScene();
            } else if (key == Key.RIGHT && !isGameStopped) {
                moveRight();
                drawScene();
            } else if (key == Key.UP && !isGameStopped) {
                moveUp();
                drawScene();
            } else if (key == Key.DOWN && !isGameStopped) {
                moveDown();
                drawScene();
            }
        }
    }

    private void moveLeft() {
        boolean changed = false;
        for (int[] row : gameField) {
            boolean compressed = compressRow(row);
            boolean merged = mergeRow(row);
            compressRow(row);
            if ((compressed || merged) && !changed) {
                createNewNumber();
                changed = true;
            }
        }
    }

    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }

    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();

    }

    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();

    }

    private int getMaxTileValue() {

        int max = 0;

        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField.length; j++) {
                if (max < gameField[i][j]) {
                    max = gameField[i][j];
                }
            }
        }
        return max;

    }

    private void win() {

        isGameStopped = true;

        showMessageDialog(Color.SILVER, "YOU WIN", Color.BLACK, 22);

    }

    private boolean canUserMove() {
        for (int i = 0; i < gameField.length; ++i) { // loop through row
            for (int j = 0; j < gameField.length; ++j) { // loop through column
                if (gameField[i][j] == 0) {
                    return true;
                }
                if ((i + 1) < SIDE && (gameField[i][j] == gameField[i + 1][j])) {
                    return true;
                }
                if ((j + 1) < SIDE && (gameField[i][j] == gameField[i][j + 1])) {
                    return true;
                }
            }
        }
        return false;
    }

    private void gameOver() {

        isGameStopped = true;

        showMessageDialog(Color.SILVER, "YOU WIN", Color.BLACK, 22);
    }

    private void rotateClockwise() {
        int[][] temp = new int[SIDE][SIDE];

        for (int i = 0; i < gameField.length; i++) {
            for (int j = 0; j < gameField.length; j++) {
                temp[i][j] = gameField[gameField.length - j - 1][i];
            }
        }
        gameField = temp;
    }

    private Color getColorByValue(int value) {

        Color color = null;

        switch (value) {
            case 0:
                color = Color.WHITE;
                break;
            case 2:
                color = Color.MAGENTA;
                break;
            case 4:
                color = Color.GREEN;
                break;
            case 8:
                color = Color.AZURE;
                break;
            case 16:
                color = Color.YELLOW;
                break;
            case 32:
                color = Color.BISQUE;
                break;
            case 64:
                color = Color.AQUA;
                break;
            case 128:
                color = Color.PURPLE;
                break;
            case 256:
                color = Color.SKYBLUE;
                break;
            case 512:
                color = Color.GOLD;
                break;
            case 1024:
                color = Color.ORANGE;
                break;
            case 2048:
                color = Color.SILVER;
                break;

            default:
                color = Color.WHITE;
                break;
        }

        return color;
    }

    private void setCellColoredNumber(int x, int y, int value) {
        Color color = getColorByValue(value);
        if (value > 0) {
            setCellValueEx(x, y, color, Integer.toString(value));
        } else {
            setCellValueEx(x, y, color, "");
        }
    }

    private void drawScene() {

        for (int x = 0; x < SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                setCellColoredNumber(x, y, gameField[y][x]);
            }
        }
    }

    @Override
    public void initialize() {
        setScreenSize(4, 4);
        createGame();
        drawScene();
    }
}