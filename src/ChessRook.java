import java.util.ArrayList;

//Arush Bodla block 5

public class ChessRook extends ChessPiece {

	public ChessRook(int x, int y, boolean white) {
		super(x,y,white);
		name = "Rook";
		value = 5;
	}

	@Override
	public boolean moveIsValid(int xDes, int yDes, int[][] boardState) {
		return moveIsStraight(xDes, yDes, boardState);
	}

	@Override
	public ArrayList<int[]> showLegalMoves(int[][] boardState) {
		ArrayList<int[]> output = new ArrayList<int[]>();
		genLegalMoves(output, boardState, 0, false);
		return output;
	}

}
