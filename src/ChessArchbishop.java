import java.util.ArrayList;

//Arush Bodla block 5

public class ChessArchbishop extends ChessPiece {

	public ChessArchbishop(int x, int y, boolean white) {
		super(x,y,white);
		name = "Archbishop";
		value = 4;
	}

	@Override
	public boolean moveIsValid(int xDes, int yDes, int[][] boardState) {
		return moveIsDiagonal(xDes, yDes, boardState) || (limit(xDes, yDes, 2) && moveIsStraight(xDes, yDes, boardState));
	}

	public ArrayList<int[]> showLegalMoves(int[][] boardState){
		ArrayList<int[]> output = new ArrayList<int[]>();
		genLegalMoves(output, boardState, 0, true);
		genLegalMoves(output, boardState, 2, false);
		return output;
	}

}