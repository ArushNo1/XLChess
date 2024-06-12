import java.util.ArrayList;

public class ChessGeneral extends ChessPiece {

	public ChessGeneral(int x, int y, boolean white) {
		super(x, y, white);
		name = "General";
		value = 4;
	}

	@Override
	public boolean moveIsValid(int xDes, int yDes, int[][] boardState) {
		int xDiff = xDes - x;
		int yDiff = yDes - y;
		return (xDiff < 2 && yDiff < 2 || moveIsLShaped(xDes, yDes, boardState));
	}

	@Override
	public ArrayList<int[]> showLegalMoves(int[][] boardState) {
		ArrayList<int[]> output = new ArrayList<int[]>();
		int[] xPos = { 2, 2, 1, -1, -2, -2, -1, 1 };
		int[] yPos = { -1, 1, 2, 2, 1, -1, -2, -2 };
		for (int i = 0; i < 8; i++) {
			int tempX = x + xPos[i];
			int tempY = y + yPos[i];
			if (tempX > chessGame.getSquareCount() || tempY > chessGame.getSquareCount() || tempX < 1 || tempY < 1) {
				continue;
			}
			if (boardState[tempX][tempY] != sameNum) {
				int[] xy = { tempX, tempY };
				output.add(xy);
			}
		}
		genLegalMoves(output, boardState, 1, true, 0, 7, 1);
		return output;
	}

}
