import java.util.ArrayList;

//Arush Bodla block 5

public class ChessPawn extends ChessPiece {

	public ChessPawn(int x, int y, boolean white) {
		super(x,y,white);
		name = "Pawn";
		value = 1;
	}

	@Override
	public boolean moveIsValid(int xDes, int yDes, int[][] boardState) {
		int forward = isWhite ? 1 : -1;
		int enemyNumber = isWhite ? 2 : 1;
		if (xDes == x) {
			if (boardState[x][y + forward] != 0) {
				return false;
			} else if (yDes == y + forward) {
				return true;
			} else if (yDes == y + 2 * forward && firstMove) {
				
				if((boardState[x][y + forward * 2] == 0)) {
					enPassX = xDes;
					enPassY = yDes + -1 * forward;
					return true;
				}
				return false;
			} else {
				return false;
			}
		}
		if (xDes - x == 1) {
			if(xDes == enPassX && yDes == enPassY) {
				int[] passDes = {enPassX, enPassY + -1*forward};
				chessGame.removePiece(passDes);
				enPassX = 0; 
				enPassY = 0;
				return true;
			}
			return (boardState[x + 1][y + forward] == enemyNumber);
		}
		if (xDes - x == -1) {
			if(xDes == enPassX && yDes == enPassY) {
				int[] passDes = {enPassX, enPassY + -1*forward};
				chessGame.removePiece(passDes);
				enPassX = 0; 
				enPassY = 0;
				return true;
			}
			return (boardState[x - 1][y + forward] == enemyNumber);
		}
		return false;
	}

	@Override
	public ArrayList<int[]> showLegalMoves(int[][] boardState) {
		ArrayList<int[]> output = new ArrayList<int[]>();
		int forward = isWhite ? 1 : -1;
		int enemyNum = isWhite ? 2 : 1;
		if (boardState[x][y + forward] == 0) {
			int[] xy = { x, y + forward };
			output.add(xy);
			if (boardState[x][y + 2 * forward] == 0 && firstMove) {
				int[] xy1 = { x, y + 2 * forward };
				output.add(xy1);
			}
		}
		if (boardState[x + 1][y + forward] == enemyNum || (enPassX == x+1 && enPassY == y + forward)) {
			int[] xy = { x + 1, y + forward };
			output.add(xy);
		}
		if (boardState[x - 1][y + forward] == enemyNum || (enPassX == x-1 && enPassY == y + forward)) {
			int[] xy = { x - 1, y + forward };
			output.add(xy);
		}
		
		return output;
	}

}
