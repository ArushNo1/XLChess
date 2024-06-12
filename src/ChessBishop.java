import java.util.ArrayList;

//Arush Bodla block 5

public class ChessBishop extends ChessPiece {

	public ChessBishop(int x, int y, boolean white) {
		super(x,y,white);
		name = "Bishop";
		value = 3;
		
	}

	@Override
	public boolean moveIsValid(int xDes, int yDes, int[][] boardState) {
		return moveIsDiagonal(xDes, yDes, boardState);
	}

	public ArrayList<int[]> showLegalMoves(int[][] boardState){
		ArrayList<int[]> output = new ArrayList<int[]>();
		genLegalMoves(output, boardState, 0, true);
		return output;
	}

}
