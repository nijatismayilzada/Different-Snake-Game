package com.thepot.differentsnakegame.service;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.thepot.differentsnakegame.R;
import com.thepot.differentsnakegame.clicklistener.gamebuttons.DownButtonOCL;
import com.thepot.differentsnakegame.clicklistener.gamebuttons.LeftButtonOCL;
import com.thepot.differentsnakegame.clicklistener.gamebuttons.RightButtonOCL;
import com.thepot.differentsnakegame.clicklistener.gamebuttons.UpButtonOCL;
import com.thepot.differentsnakegame.model.Cell;
import com.thepot.differentsnakegame.model.CellType;

import static com.thepot.differentsnakegame.model.CellType.EMPTY;
import static com.thepot.differentsnakegame.model.CellType.OBSTACLE;
import static com.thepot.differentsnakegame.service.CageService.CELL_MAX_ID;
import static com.thepot.differentsnakegame.service.CageService.CELL_MIN_ID;

public class ButtonService {
    private ImageButton upButton;
    private ImageButton downButton;
    private ImageButton leftButton;
    private ImageButton rightButton;
    private TextView gameOver;

    private CageService cageService;
    private SnakeService snakeService;
    private LevelService levelService;

    public ButtonService(AppCompatActivity activity, CageService cageService, SnakeService snakeService, LevelService levelService) {
        this.cageService = cageService;
        this.snakeService = snakeService;
        this.levelService = levelService;

        upButton = activity.findViewById(R.id.upButton);
        upButton.setOnClickListener(new UpButtonOCL(snakeService));
        downButton = activity.findViewById(R.id.downButton);
        downButton.setOnClickListener(new DownButtonOCL(snakeService));
        leftButton = activity.findViewById(R.id.leftButton);
        leftButton.setOnClickListener(new LeftButtonOCL(snakeService));
        rightButton = activity.findViewById(R.id.rightButton);
        rightButton.setOnClickListener(new RightButtonOCL(snakeService));

        gameOver = activity.findViewById(R.id.gameOver);
    }

    public void updateButtons() {

        upButton.setClickable(true);
        downButton.setClickable(true);
        leftButton.setClickable(true);
        rightButton.setClickable(true);

        Cell snakeHead = snakeService.getSnakeHead();

        int yUp = checkEdge(snakeHead.getY() - 1 < CELL_MIN_ID, snakeHead.getY() - 1, CELL_MAX_ID);
        int yUp2 = checkEdge(yUp - 1 < CELL_MIN_ID, yUp - 1, CELL_MAX_ID);

        if (levelService.getCurrentLevel().getMovesLeft() == 0
                || cantGoUp(yUp)
                || (isObstacle(yUp, snakeHead.getX()) && cantMoveUp(yUp2))) {
            upButton.setClickable(false);
        }


        int yBottom = checkEdge(snakeHead.getY() + 1 > CELL_MAX_ID, snakeHead.getY() + 1, CELL_MIN_ID);
        int yBottom2 = checkEdge(yBottom + 1 > CELL_MAX_ID, yBottom + 1, CELL_MIN_ID);

        if (levelService.getCurrentLevel().getMovesLeft() == 0
                || cantGoDown(yBottom)
                || (isObstacle(yBottom, snakeHead.getX()) && cantMoveDown(yBottom2))) {
            downButton.setClickable(false);
        }


        int xLeft = checkEdge(snakeHead.getX() - 1 < CELL_MIN_ID, snakeHead.getX() - 1, CELL_MAX_ID);
        int xLeft2 = checkEdge(xLeft - 1 < CELL_MIN_ID, xLeft - 1, CELL_MAX_ID);

        if (levelService.getCurrentLevel().getMovesLeft() == 0
                || cantGoLeft(xLeft)
                || (isObstacle(snakeHead.getY(), xLeft) && cantMoveLeft(xLeft2))) {
            leftButton.setClickable(false);
        }


        int xRight = checkEdge(snakeHead.getX() + 1 > CELL_MAX_ID, snakeHead.getX() + 1, CELL_MIN_ID);
        int xRight2 = checkEdge(xRight + 1 > CELL_MAX_ID, xRight + 1, CELL_MIN_ID);

        if (levelService.getCurrentLevel().getMovesLeft() == 0
                || cantGoRight(xRight)
                || (isObstacle(snakeHead.getY(), xRight) && cantMoveRight(xRight2))) {
            rightButton.setClickable(false);
        }

        if (upButton.isClickable() || downButton.isClickable() || leftButton.isClickable() || rightButton.isClickable()) {
            gameOver.setVisibility(View.INVISIBLE);
        } else {
            gameOver.setVisibility(View.VISIBLE);
        }
    }

    private int checkEdge(boolean endOfEdge, int coordinate, int replacementCoordinate) {
        if (endOfEdge && levelService.getCurrentLevel().isTransparentWall()) {
            return replacementCoordinate;
        }
        return coordinate;
    }

    private boolean cantGoUp(int yUp) {
        return yUp < CELL_MIN_ID || getCellType(yUp, snakeService.getSnakeHead().getX()).isNotActionable();
    }

    private boolean cantMoveUp(int yUp) {
        return yUp < CELL_MIN_ID || getCellType(yUp, snakeService.getSnakeHead().getX()) != EMPTY;
    }

    private boolean cantGoDown(int yBottom) {
        return yBottom > CELL_MAX_ID || getCellType(yBottom, snakeService.getSnakeHead().getX()).isNotActionable();
    }

    private boolean cantMoveDown(int yBottom) {
        return yBottom > CELL_MAX_ID || getCellType(yBottom, snakeService.getSnakeHead().getX()) != EMPTY;
    }

    private boolean cantGoLeft(int xLeft) {
        return xLeft < CELL_MIN_ID || getCellType(snakeService.getSnakeHead().getY(), xLeft).isNotActionable();
    }

    private boolean cantMoveLeft(int xLeft) {
        return xLeft < CELL_MIN_ID || getCellType(snakeService.getSnakeHead().getY(), xLeft) != EMPTY;
    }

    private boolean cantGoRight(int xRight) {
        return xRight > CELL_MAX_ID || getCellType(snakeService.getSnakeHead().getY(), xRight).isNotActionable();
    }

    private boolean cantMoveRight(int xRight) {
        return xRight > CELL_MAX_ID || getCellType(snakeService.getSnakeHead().getY(), xRight) != EMPTY;
    }

    private boolean isObstacle(int y, int x) {
        return getCellType(y, x) == OBSTACLE;
    }

    private CellType getCellType(int y, int x) {
        return cageService.getCage().cells[y][x].getCellType();
    }

}
