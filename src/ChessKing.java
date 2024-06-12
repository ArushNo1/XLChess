import java.util.ArrayList;

//Arush Bodla block 5

public class ChessKing extends ChessPiece {

	public ChessKing(int x, int y, boolean white) {
		super(x,y,white);
		name = "King";
		value = 0;
	}

	@Override
	public boolean moveIsValid(int xDes, int yDes, int[][] boardState) {
		int xDiff = Math.abs(x - xDes);
		int yDiff = Math.abs(y - yDes);
		int castleX = (xDes - x > 0) ? chessGame.getSquareCount() : 1;
		int xInc = (castleX == 1) ? -1 : 1;

		if (yDiff == 0 && xDiff == 2) {
			int[] desXy = { castleX, y };
			ChessPiece rookToCastle = chessGame.getPieceAt(desXy);
			for (int i = 0; i < 3; i++) {
				int xTemp = x + i * xInc;
				int[] xy = { xTemp, y };
				if (inCheck(xy, isWhite)) {
					return false;
				}
			}
			int xTemp = x + xInc;
			while (xTemp > 1 && xTemp < chessGame.getSquareCount()) {
				if (boardState[xTemp][y] != 0) {
					return false;
				}
				xTemp += xInc;
			}
			rookToCastle.x = (castleX == 1) ? xDes + 1 : xDes - 1;
			return true;
		}

		if (xDiff > 1 || yDiff > 1) {
			return false;
		}
		int[] pos = { xDes, yDes };
		return !inCheck(pos, isWhite);

	}

	@Override
	public ArrayList<int[]> showLegalMoves(int[][] boardState) {
		ArrayList<int[]> output = new ArrayList<int[]>();
		if (firstMove) {
			int[] desXy = { chessGame.getSquareCount(), y };
			ChessPiece rookToCastle = chessGame.getPieceAt(desXy);
			int[] sq1 = { x + 1, y };
			int[] sq2 = { x + 2, y };
			int[] sq3 = { x - 1, y };
			int[] sq4 = { x - 2, y };
			if (rookToCastle != null && rookToCastle.firstMove) {
				if (!inCheck(sq1, isWhite) && !inCheck(sq2, isWhite)) {
					int xTemp = x + 1;
					boolean valid = true;
					while (xTemp < chessGame.getSquareCount()) {
						if (boardState[xTemp][y] != 0) {
							valid = false;
						}
						xTemp++;
					}
					if (valid) {
						output.add(sq2);
					}
				}
			}
			int[] newDesXy = { 1, y };
			rookToCastle = chessGame.getPieceAt(newDesXy);
			if (rookToCastle != null && rookToCastle.firstMove) {
				if (!inCheck(sq3, isWhite) && !inCheck(sq4, isWhite)) {
					int xTemp = x - 1;
					boolean valid = true;
					while (xTemp > 1) {
						if (boardState[xTemp][y] != 0) {
							valid = false;
						}
						xTemp--;
					}
					if (valid) {
						output.add(sq4);
					}
				}
			}
		}
		int[] xIncs = { 1, -1, 0, 0, 1, 1, -1, -1 };
		int[] yIncs = { 0, 0, 1, -1, 1, -1, 1, -1 };
		for (int i = 0; i < 8; i++) {
			int tempX = x + xIncs[i];
			int tempY = y + yIncs[i];
			int currentSquare = boardState[tempX][tempY];

			if (currentSquare == sameNum) {
				continue;
			}
			int[] coords = { tempX, tempY };
			if (coords[0] > chessGame.getSquareCount() || coords[0] < 1 || coords[1] > chessGame.getSquareCount() || coords[1] < 1) {
				continue;
			}

			if (!inCheck(coords, isWhite)) {
				output.add(coords);
			}
		}
		return output;
	}

}
