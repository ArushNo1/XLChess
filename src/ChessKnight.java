import java.util.ArrayList;

//Arush Bodla block 5

public class ChessKnight extends ChessPiece {

	public ChessKnight(int x, int y, boolean white) {
		super(x,y,white);
		name = "Night";
		value = 3;
	}

	@Override
	public boolean moveIsValid(int xDes, int yDes, int[][] boardState) {
		return moveIsLShaped(xDes, yDes, boardState);

	}

	@Override
	public ArrayList<int[]> showLegalMoves(int[][] boardState) {
		ArrayList<int[]> output = new ArrayList<int[]>();
		int[] xPos = { 2, 2, 1, -1, -2, -2, -1, 1 };
		int[] yPos = { -1, 1, 2, 2, 1, -1, -2, -2 };
		for (int i = 0; i < 8; i++) {
			int tempX = x + xPos[i];
			int tempY = y + yPos[i];
			if(tempX > chessGame.getSquareCount() || tempY > chessGame.getSquareCount() || tempX < 1 || tempY < 1) {
				continue;
			}
			int currentSquare = boardState[tempX][tempY];
			if(currentSquare != sameNum) {
				int[] xy = {tempX, tempY};
				output.add(xy);
			}
			
		}
		return output;
	}

}
