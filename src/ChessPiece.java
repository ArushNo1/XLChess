
//Arush Bodla block 5
import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

public abstract class ChessPiece {
	public boolean isWhite;
	public boolean firstMove = true;

	protected String name;
	protected int sameNum;

	public int x;
	public int y;
	public int value;

	public static int enPassX;
	public static int enPassY;
	public static int movesSincePass = 0;

	public static char[][] pieceState;
	public static Chess chessGame;
	public static String promotable = "PS";

	public ChessPiece(int x, int y, boolean white) {
		this.x = x;
		this.y = y;
		isWhite = white;
		sameNum = white ? 1 : 2;
	}

	public void setPieceAsMoved() {
		firstMove = false;
	}

	public String toString() {
		String whiteBlack = isWhite ? "White" : "Black";
		return whiteBlack + name + " At " + x + ", " + y + " Moved? " + !firstMove;
	}

	public static void setPieceState(char[][] pieceState2) {
		pieceState = pieceState2;
	}

	public static void setGame(Chess program) {
		chessGame = program;
	}

	public void checkForPromotion() {
		if (promotable.contains(name.charAt(0) + "")) {
			if (isWhite) {
				if (y == chessGame.getSquareCount()) {
					chessGame.setPromotionInfo(x, isWhite);
				}
			} else {
				if (y == 1) {
					chessGame.setPromotionInfo(x, isWhite);
				}
			}
		}
	}

	public static boolean inCheck(int[] xy, boolean whiteBlack) {
		return inCheck(xy, whiteBlack, pieceState);
	}

	public static boolean inCheck(int[] xy, boolean whiteBlack, char[][] pieceState) {
		String enemyKnightMovers = (whiteBlack) ? "NEZ" : "nez";
		String enemyCamelMovers = (whiteBlack) ? "C" : "c";
		char sameKing = (whiteBlack) ? 'k' : 'K';
		int[] xKnights = { 2, 2, 1, -1, -2, -2, -1, 1 };
		int[] yKnights = { -1, 1, 2, 2, 1, -1, -2, -2 };
		int[] xCamels = { 2, 2, 1, -1, -2, -2, -1, 1 };
		int[] yCamels = { -1, 1, 2, 2, 1, -1, -2, -2 };
		// order of Directions
		// > ⬈ ^ ↖ < ↙ v ↘
		String[][] whiteBadPieces = { { "EQRZ", "KSA", "A" }, { "QBAZ", "PK" }, { "EQRZ", "KA", "A" }, { "QBAZ", "PK" },
				{ "EQRZ", "KSA", "A" }, { "QBAZ", "K" }, { "EQRZ", "KA", "A" }, { "QBAZ", "K" } };
		String[][] blackBadPieces = { { "eqrz", "ksa", "a" }, { "qbaz", "pk" }, { "eqrz", "ka", "a" }, { "qbaz", "pk" },
				{ "eqrz", "ksa", "a" }, { "qbaz", "k" }, { "eqrz", "ka", "a" }, { "qbaz", "k" } };
		String[][] badPieces = (whiteBlack) ? whiteBadPieces : blackBadPieces;
		// manually checks for pawns
		if (xy[0] > chessGame.getSquareCount()) {
			return false;
		}

		for (int i = 0; i < 8; i++) {
			int xTemp = xy[0] + xKnights[i];
			int yTemp = xy[1] + yKnights[i];
			if (xTemp >= 0 && yTemp >= 0 && xTemp < pieceState.length && yTemp < pieceState.length) {
				if (enemyKnightMovers.contains(pieceState[xTemp][yTemp] + "")) {
					return true;
				}
			}
			xTemp = xy[0] + xCamels[i];
			yTemp = xy[1] + yCamels[i];
			if (xTemp >= 0 && yTemp >= 0 && xTemp < pieceState.length && yTemp < pieceState.length) {
				if (enemyCamelMovers.contains(pieceState[xTemp][yTemp] + "")) {
					return true;
				}
			}
			int xInc = getXInc(i);
			int yInc = getYInc(i);
			xTemp = xy[0] + xInc;
			yTemp = xy[1] + yInc;
			char currentPiece = '§';
			if (xTemp >= 0 && xTemp < pieceState.length && yTemp >= 0 && yTemp < pieceState.length) {
				currentPiece = pieceState[xTemp][yTemp];
			}

			int counter = 1;
			while (currentPiece != '§') {
				int tempCounter = counter < badPieces[i].length ? counter : 0;
				if (badPieces[i][0].contains(currentPiece + "")
						|| badPieces[i][tempCounter].contains(currentPiece + "")) {
					return true;
				} else if (currentPiece != '.' && currentPiece != sameKing) {
					break;
				}
				counter++;
				xTemp += xInc;
				yTemp += yInc;
				currentPiece = pieceState[xTemp][yTemp];
			}
		}
		return false;
	}

	public boolean move(int xDes, int yDes, int[][] boardState) {
		// Validates if the square is empty, and is a legal move
		if (boardState[xDes][yDes] == sameNum) {
			return false;
		}
		if (moveIsValid(xDes, yDes, boardState)) {
			x = xDes;
			y = yDes;
			updatePass();
			return true;
		}
		return false;
	}

	public static void updatePass() {
		movesSincePass++;
		if (movesSincePass > 1) {
			movesSincePass = 0;
			enPassX = 0;
			enPassY = 0;
		}
	}

	public void draw(Graphics g, int boardX, int boardY, int squareSize) {
		String sideName = isWhite ? "white" : "black";
		//ImageIcon image = new ImageIcon("chess\\" + sideName + name + ".png");
		ImageIcon image = new ImageIcon(getClass().getClassLoader().getResource(sideName + name + ".png"));
		g.drawImage(image.getImage(), (x - 1) * squareSize + boardX,
				(chessGame.getSquareCount() - y) * squareSize + boardY, squareSize, squareSize, null);
	}

	public void drawM(Graphics g, int mouseX, int mouseY, int squareSize) {
		String sideName = isWhite ? "white" : "black";
		//ImageIcon image = new ImageIcon("chess\\" + sideName + name + ".png");
		ImageIcon image = new ImageIcon(getClass().getClassLoader().getResource(sideName + name + ".png"));
		g.drawImage(image.getImage(), mouseX - squareSize / 2, mouseY - squareSize / 2, squareSize, squareSize, null);
	}

	public boolean moveIsDiagonal(int xDes, int yDes, int[][] boardState) {
		int xDiff = xDes - x;
		int yDiff = yDes - y;
		if (Math.abs(yDiff) != Math.abs(xDiff)) {
			return false;
		}
		int xInc = (xDes > x) ? 1 : -1;
		int yInc = (yDes > y) ? 1 : -1;
		int yAdd = yInc;
		for (int xAdd = xInc; Math.abs(xAdd) < Math.abs(xDiff); xAdd += xInc) {
			if (boardState[x + xAdd][y + yAdd] != 0) {
				return false;
			}
			yAdd += yInc;
		}
		return true;
	}

	public boolean moveIsStraight(int xDes, int yDes, int[][] boardState) {
		if (xDes - x == 0) {
			int increment = (yDes > y) ? 1 : -1;
			for (int i = increment; Math.abs(i) < yDes - y; i += increment) {
				if (boardState[x][y + i] != 0) {
					return false;
				}
			}
			return true;
		}
		if (yDes - y == 0) {
			int increment = (xDes > x) ? 1 : -1;
			for (int i = increment; Math.abs(i) < xDes - x; i += increment) {
				if (boardState[x + i][y] != 0) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public boolean moveIsLShaped(int xDes, int yDes, int[][] boardState) {
		int xDiff = Math.abs(xDes - x);
		int yDiff = Math.abs(yDes - y);
		return (xDiff == 1 && yDiff == 2) || (xDiff == 2 && yDiff == 1);
	}

	public boolean moveIsBigLShaped(int xDes, int yDes, int[][] boardState) {
		int xDiff = Math.abs(xDes - x);
		int yDiff = Math.abs(yDes - y);
		return (xDiff == 1 && yDiff == 3) || (xDiff == 3 && yDiff == 1);
	}

	public boolean limit(int xDes, int yDes, int limit) {
		int xDiff = Math.abs(x - xDes);
		int yDiff = Math.abs(y - yDes);
		return (xDiff < limit && yDiff < limit);
	}

	public static int getXInc(int dir) {
		return (int) (Math.PI * .5 * Math.cos(Math.PI * .25 * dir));
	}

	public static int getYInc(int dir) {
		return (int) (Math.PI * .5 * Math.sin(Math.PI * .25 * dir));
	}

	public void genLegalMoves(ArrayList<int[]> movesList, int[][] boardState, int limit, boolean canCapture,
			int directionMin, int directionMax, int increment) {
		int enemyNum = isWhite ? 2 : 1;
		for (int i = directionMin; i <= directionMax; i += increment) {
			int xInc = getXInc(i);
			int yInc = getYInc(i);
			int tempX = x + xInc;
			int tempY = y + yInc;
			for (int j = 0; j < limit || limit == 0; j++) {
				if (boardState[tempX][tempY] == sameNum) {
					break;
				}
				if (canCapture && boardState[tempX][tempY] == enemyNum) {
					int[] xy = { tempX, tempY };
					movesList.add(xy);
					break;
				}
				if (boardState[tempX][tempY] == 0) {
					int[] xy = { tempX, tempY };
					movesList.add(xy);
				} else {
					break;
				}
				tempX += xInc;
				tempY += yInc;
			}
		}
	}

	public void genLegalMoves(ArrayList<int[]> movesList, int[][] boardState, int limit, boolean diagonal) {
		if (diagonal) {
			genLegalMoves(movesList, boardState, limit, true, 1, 7, 2);
		} else {
			genLegalMoves(movesList, boardState, limit, true, 0, 6, 2);
		}
	}

	public abstract boolean moveIsValid(int xDes, int yDes, int[][] boardState);

	public abstract ArrayList<int[]> showLegalMoves(int[][] boardState);

}
