package Game;

import GUI.Renderer;
import Game.Logic.GameLogic;
import Game.Structures.Table;

public class BilliardGame {
    private Renderer renderer;
    private Table table;
    private GameLogic gameLogic;

    public BilliardGame() {
        this.table = new Table();
        this.renderer = new Renderer(table);
        this.gameLogic = new GameLogic(table);
    }

    public void startGame() {
        this.renderer.render();
        this.gameLogic.startGame();
    }
}
