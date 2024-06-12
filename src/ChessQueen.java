import java.util.ArrayList;

//Arush Bodla block 5

public class ChessQueen extends ChessPiece {

	public ChessQueen(int x, int y, boolean white) {
		super(x, y, white);
		name = "Queen";
		value = 9;
	}

	@Override
	public boolean moveIsValid(int xDes, int yDes, int[][] boardState) {
		return (moveIsDiagonal(xDes, yDes, boardState) || moveIsStraight(xDes, yDes, boardState));
	}

	@Override
	public ArrayList<int[]> showLegalMoves(int[][] boardState) {
		ArrayList<int[]> output = new ArrayList<int[]>();
		genLegalMoves(output, boardState, 0, true, 0, 7, 1);
		return output;
	}

}
