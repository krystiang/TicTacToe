package tictactoe;

import java.util.ArrayList;
import java.util.List;


public class GamestateNode {

    public Gamestate gamestate;
    public int val;
    public int move;
    public GamestateNode root;
    public List<GamestateNode> childs;

    public GamestateNode(Gamestate gamestate){
        this.gamestate = gamestate;
        childs = new ArrayList<GamestateNode>();
    }


    public GamestateNode addChild(Gamestate gamestate){
        GamestateNode child_node = new GamestateNode(gamestate);
        child_node.root = this;
        this.childs.add(child_node);
        return child_node;
    }

    public void setVal(int aVal){
        val = aVal;
    }
    public void setMove(int aMove){
    	move = aMove;
    }


}
