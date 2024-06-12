import java.util.ArrayList;

public class ChessCamel extends ChessPiece {

	public ChessCamel(int x, int y, boolean white) {
		super(x, y, white);
		name = "Camel";
		value = 3;
	}

	@Override
	public boolean moveIsValid(int xDes, int yDes, int[][] boardState) {
		int xDiff = Math.abs(xDes - x);
		int yDiff = Math.abs(yDes - y);
		return moveIsBigLShaped(xDes, yDes, boardState) 
				|| (xDiff == 0 && yDiff == 1 && boardState[xDes][yDes] == 0)
				|| (yDiff == 0 && xDiff == 1 && boardState[xDes][yDes] == 0);
	}

	@Override
	public ArrayList<int[]> showLegalMoves(int[][] boardState) {
		ArrayList<int[]> output = new ArrayList<int[]>();
		int[] xPos = { 3, 3, 1, -1, -3, -3, -1, 1 };
		int[] yPos = { -1, 1, 3, 3, 1, -1, -3, -3 };
		for (int i = 0; i < 8; i++) {
			int tempX = x + xPos[i];
			int tempY = y + yPos[i];
			if (tempX > chessGame.getSquareCount() || tempY > chessGame.getSquareCount() || tempX < 1 || tempY < 1) {
				continue;
			}
			int currentSquare = boardState[tempX][tempY];
			if (currentSquare != sameNum) {
				int[] xy = { tempX, tempY };
				output.add(xy);
			}	

		}
		genLegalMoves(output, boardState, 1, false, 0, 6, 2);

		return output;
	}

}
