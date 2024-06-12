import java.util.ArrayList;

public class ChessSoldier extends ChessPiece {

	public ChessSoldier(int x, int y, boolean white) {
		super(x, y, white);
		name = "Soldier";
		value = 1;
	}

	@Override
	public boolean moveIsValid(int xDes, int yDes, int[][] boardState) {

		int forward = isWhite ? 1 : -1;
		if (xDes - x == 0) {
			if (boardState[x][y + forward] == sameNum) {
				return false;
			} else if (yDes == y + forward) {
				return true;
			} else if (yDes == y + 2 * forward && firstMove) {
				if ((boardState[x][y + forward * 2] != sameNum)) {
					return true;
				}
			}
		} else if (xDes - x == 1) {
			return (boardState[x + 1][y] == 0);
		} else if (xDes - x == -1) {
			return (boardState[x - 1][y] == 0);
		}
		return false;
	}

	@Override
	public ArrayList<int[]> showLegalMoves(int[][] boardState) {
		ArrayList<int[]> output = new ArrayList<int[]>();
		int forward = isWhite ? 1 : -1;
		if (boardState[x][y + forward] != sameNum) {
			int[] xy = { x, y + forward };
			output.add(xy);
			if (boardState[x][y + 2 * forward] != sameNum && firstMove) {
				int[] xy1 = { x, y + 2 * forward };
				output.add(xy1);
			}
		}

		if (boardState[x + 1][y] == 0) {
			int[] xy = { x + 1, y };
			output.add(xy);
		} 
		if (boardState[x - 1][y] == 0) {
			int[] xy = { x - 1, y };
			output.add(xy);
		}

		return output;
	}

}
