
//Arush Bodla Block 5
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Chess extends JPanel {


	/**
	 * 
	 */
	private static final long serialVersionUID = -7442217838662125069L;
	/**
	 * 
	 */
	private static final int WIDTH = 900;
	private static final int HEIGHT = 700;

	// required global variables

	private BufferedImage image;
	private Graphics g;
	private Timer timer;
	private ChessBoard board;
	private ChessMenu menu;

	private int currentMouseX;
	private int currentMouseY;
	private int startXCoord;
	private int startYCoord;
	private boolean clickedRecent = false;
	private int timeSinceClick;
	private boolean dragging = false;
	private boolean pieceSelectedByClick;

	private int gameState = 0;

	private int player1score = 0;
	private int player2score = 0;

	private boolean player1isWhite = true;
	private boolean whitesTurn = true;

	private ArrayList<ChessPiece> pieces = new ArrayList<ChessPiece>();

	private ChessPiece selectedPiece;

	private ArrayList<int[]> legalMoves = new ArrayList<int[]>();
	private int[] emptyArr = { 1234, 1234 };
	public ArrayList<int[]> emptyArrList = new ArrayList<int[]>(Arrays.asList(emptyArr));

	private int promotionX = 0;
	private boolean promotedPieceIsWhite;

	private int[][] boardState;
	private char[][] pieceState;
	private int whiteKingX;
	private int whiteKingY;
	private int blackKingX;
	private int blackKingY;

	private int squareCount;

	private int screen = 0;
	
	// getters and setters for all the fields other classes need
	public int getGameState() {
		return gameState;
	}

	public void setGameState(int gameState) {
		this.gameState = gameState;
	}

	public void addScore(boolean isPlayer1) {
		if (isPlayer1) {
			player1score++;
		} else {
			player2score++;
		}
	}

	public boolean getWhitesTurn() {
		return whitesTurn;
	}

	public boolean isPlayerOnesTurn() {
		return player1isWhite == whitesTurn;
	}

	public boolean isPlayerOneWhite() {
		return player1isWhite;
	}

	public int pieceIndexOf(int[] target) {
		for (int i = 0; i < pieces.size(); i++) {
			if (pieces.get(i).x == target[0]) {
				if (pieces.get(i).y == target[1]) {
					return i;
				}
			}
		}
		return -1;
	}

	public void addPiece(ChessPiece piece) {
		pieces.add(piece);
	}

	public void removePiece(int[] position) {
		pieces.remove(pieceIndexOf(position));
	}

	public void setPromotionInfo(int x, boolean isWhite) {
		promotionX = x;
		promotedPieceIsWhite = isWhite;
	}

	public int getPromotionSpot() {
		return promotionX;
	}

	public boolean isPromotionWhite() {
		return promotedPieceIsWhite;
	}

	public int getSquareCount() {
		return squareCount;
	}

	public Chess() {
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = image.getGraphics();
		menu = new ChessMenu(WIDTH, HEIGHT);

		timer = new Timer(10, new TimerListener());
		timer.start();
		addMouseListener(new Mouse());
		addKeyListener(new Keyboard());
		setFocusable(true);
		addMouseMotionListener(new MouseMotion());
	}

	private class MouseMotion implements MouseMotionListener {

		public void mouseDragged(MouseEvent e) {
			// this code is what is responsible for picking up and dragging pieces around
			dragging = true;
			currentMouseX = e.getX();
			currentMouseY = e.getY();
			if (screen == 1) {
				menu.updateClick(e);
			} else if (screen == 3 && promotionX == 0 && gameState == 0) {
				int[] cords = board.getSquare(currentMouseX, currentMouseY);
				if (cords[0] != startXCoord || cords[1] != startYCoord) {
					if (pieceSelectedByClick) {
						selectedPiece = null;
					}
					pieceSelectedByClick = false;
				}

				if (cords[0] == 0) {
					selectedPiece = null;
					pieceSelectedByClick = false;
					return;
				}
				if (selectedPiece != null)
					return;
				ChessPiece tempPiece = getPieceAt(cords);
				selectedPiece = tempPiece;
			}
		}

		public void mouseMoved(MouseEvent e) {

		}
	}

	private class Keyboard implements KeyListener {
		public void keyPressed(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
			menu.processKey(e);
		}

		public void keyTyped(KeyEvent e) {
		}
	}

	private class Mouse implements MouseListener {
		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
			timer.start();
		}

		public void mouseExited(MouseEvent e) {
			timer.stop();
		}

		public void mousePressed(MouseEvent e) {
			timeSinceClick = 0;
			clickedRecent = true;

			if (screen == 3) {
				int[] nums = board.getSquare(e.getX(), e.getY());
				startXCoord = nums[0];
				startYCoord = nums[1];
			}
		}

		public void mouseReleased(MouseEvent e) {
			// This part is the stuff for clicking through the menus
			int x = e.getX();
			int y = e.getY();
			menu.updateClick(e);

			if (screen == 0) {
				if (x > 400 && x < 515 && y > 310 && y < 375) {
					screen = 1;
				} else if (x > 400 && x < 500 && y > 435 && y < 480) {
					screen = 2;
				}

			} else if (screen == 1) {
				if (x < 50 && y < 50) {
					screen = 0;
				}
			} else if (screen == 2) {
				if (y > 590 && y < 625) {
					if (x > 640 && x < 710) {
						menu.next();
					} else if (x > 190 && x < 260) {
						menu.previous();
					}
				} else if (y < 50 && x < 50) {
					screen = 0;
				}

			} else if (screen == 3) {
				dragging = false;
				// resign buttons
				if (x > 25 && x < 125) {
					if (y > 125 && y < 200) {
						gameState = 1400;
						addScore(true);
					} else if (y > 500 && y < 575) {
						gameState = 2400;
						addScore(false);
					}
				}
				if (promotionX != 0) {
					menu.promotionClick(e, board);
				} else if (gameState != 0) {
					if (x > 460 && x < 640 && y > 430 && y < 500) {
						reset();
					}
				} else {
					// What happens when a piece is clicked on
					int[] nums = board.getSquare(x, y);
					if (nums[0] == 0) {
						return;
					}

					if (clickedRecent && nums[0] == startXCoord && nums[1] == startYCoord) {
						// That means this was a click
						if (contains(legalMoves, nums) && selectedPiece != null) {
							// until line 294 is special moves
							if (selectedPiece.name.equals("King") && Math.abs(nums[0] - selectedPiece.x) == 2) {
								if (nums[0] > selectedPiece.x) {
									int[] rookXy = { squareCount, selectedPiece.y };
									ChessPiece rookToCastle = getPieceAt(rookXy);
									rookToCastle.x = nums[0] - 1;
								} else {
									int[] rookXy = { 1, selectedPiece.y };
									ChessPiece rookToCastle = getPieceAt(rookXy);
									rookToCastle.x = nums[0] + 1;
								}
							}
							if (selectedPiece.name.equals("Pawn") && Math.abs(nums[1] - selectedPiece.y) == 2) {
								ChessPiece.enPassX = nums[0];
								int backward = selectedPiece.isWhite ? -1 : 1;
								ChessPiece.enPassY = nums[1] + backward;
								ChessPiece.movesSincePass = 0;
							}
							selectedPiece.x = nums[0];
							selectedPiece.y = nums[1];
							selectedPiece.checkForPromotion();
							if (selectedPiece.name.equals("Pawn") && nums[0] == ChessPiece.enPassX
									&& nums[1] == ChessPiece.enPassY) {
								int backward = selectedPiece.isWhite ? -1 : 1;
								int[] passDes = { ChessPiece.enPassX, ChessPiece.enPassY + backward };
								pieces.remove(pieceIndexOf(passDes));
								ChessPiece.enPassX = 0;
								ChessPiece.enPassY = 0;

							}
							// capturing
							for (int i = 0; i < pieces.size(); i++) {
								if (pieces.get(i) == selectedPiece) {
									continue;
								}
								if (pieces.get(i).x == selectedPiece.x && pieces.get(i).y == selectedPiece.y) {
									pieces.remove(i);
								}

							}
							ChessPiece.updatePass();
							board.endTurn();
							toggleTurn();
							selectedPiece.firstMove = false;
							checkForMate();
						}
						// This is the logic if a piece is not moving, but being selected
						upDateBoard(false);
						ChessPiece tempPiece = selectedPiece;
						selectedPiece = getPieceAt(nums);
						if (selectedPiece == null) {
							pieceSelectedByClick = false;
							return;
						}
						legalMoves = selectedPiece.showLegalMoves(boardState);
						if (selectedPiece.isWhite != whitesTurn) {
							legalMoves = emptyArrList;
						}
						removeIllegalMoves(selectedPiece, legalMoves);
						if (pieceSelectedByClick && selectedPiece == tempPiece) {
							pieceSelectedByClick = false;
							selectedPiece = null;
						} else {
							pieceSelectedByClick = true;
						}
					} else {
						// This means it was a drag
						if (selectedPiece != null && selectedPiece.isWhite == whitesTurn) {
							int oldX = selectedPiece.x;
							int oldY = selectedPiece.y;
							int kingX = whitesTurn ? whiteKingX : blackKingX;
							int kingY = whitesTurn ? whiteKingY : blackKingY;
							int[] kingPos = { kingX, kingY };
							if (selectedPiece.move(nums[0], nums[1], boardState)) {
								toggleTurn();
								if (ChessPiece.inCheck(kingPos, !whitesTurn) && !selectedPiece.name.equals("King")) {
									toggleTurn();
									selectedPiece.x = oldX;
									selectedPiece.y = oldY;
								} else {
									// Capturing
									for (int i = 0; i < pieces.size(); i++) {
										if (pieces.get(i) == selectedPiece) {
											continue;
										}
										if (pieces.get(i).x == selectedPiece.x && pieces.get(i).y == selectedPiece.y) {
											pieces.remove(i);
										}

									}
									upDateBoard(false);
									selectedPiece.firstMove = false;
									selectedPiece.checkForPromotion();
									checkForMate();
									board.endTurn();

								}
							} else {
								upDateBoard(false);
							}

						}
						selectedPiece = null;
					}
				}
			}
		}

		public void toggleTurn() {
			if (whitesTurn)
				whitesTurn = false;
			else {
				whitesTurn = true;
			}
		}
	}

// TimerListener class that is called repeatedly by the timer
	private class TimerListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			g.setColor(Color.black);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			// Really just draws all of the UI's
			if (!menu.draw(g, screen)) {
				board.draw(g);
				for (int i = 0; i < pieces.size(); i++) {
					if (pieces.get(i) == selectedPiece && dragging && !pieceSelectedByClick
							&& selectedPiece.isWhite == whitesTurn) {
						pieces.get(i).drawM(g, currentMouseX, currentMouseY, board.squareSize);
					} else {
						pieces.get(i).draw(g, board.x, board.y, board.squareSize);
					}
				}
				if (pieceSelectedByClick) {
					board.drawLegalMoves(g, legalMoves);
				}
				board.drawWinScreen(g, gameState, player1score, player2score);
				menu.drawPromotion(g, board);

			}

			timeSinceClick++;
			if (timeSinceClick > 15) {
				timeSinceClick = 0;
				clickedRecent = false;
			}
			repaint(); // leave this alone, it MUST be the last thing in this method
		}

	}

	// do not modify this
	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}

	public boolean contains(ArrayList<int[]> body, int[] target) {
		// custom contains method since Arraylists one wasnt working
		for (int i = 0; i < body.size(); i++) {
			if (body.get(i)[0] == target[0]) {
				if (body.get(i)[1] == target[1]) {
					return true;
				}
			}
		}
		return false;
	}

	public ChessPiece getPieceAt(int[] xy) {
		for (int i = 0; i < pieces.size(); i++) {
			if (pieces.get(i).x == xy[0]) {
				if (pieces.get(i).y == xy[1]) {
					return pieces.get(i);
				}
			}

		}
		return null;
	}

	public void removeIllegalMoves(ChessPiece selPiece, ArrayList<int[]> listOfMoves) {
		upDateBoard(false);
		// Runs each move in the arraylist and sees if the king is in check after

		if (selPiece.name.equals("King")) {
			int maxLim = listOfMoves.size();
			for (int i = 0; i < maxLim; i++) {
				if (ChessPiece.inCheck(listOfMoves.get(i), selPiece.isWhite)) {
					listOfMoves.remove(i);
					i--;
					maxLim = listOfMoves.size();
				}

			}
			return;
		}
		char[][] tempPieceState = copyOf(pieceState);
		int[] oldXY = { selPiece.x, selPiece.y };
		char oldPiece;
		char pieceNameLetter = selPiece.isWhite ? Character.toLowerCase(selPiece.name.charAt(0))
				: selPiece.name.charAt(0);
		int maxLim = listOfMoves.size();
		for (int i = 0; i < maxLim; i++) {
			tempPieceState[oldXY[0]][oldXY[1]] = '.';
			int[] newXY = { listOfMoves.get(i)[0], listOfMoves.get(i)[1] };
			if (newXY[0] == 1234) {
				return;
			}

			int[] whiteKingPos = { whiteKingX, whiteKingY };
			int[] blackKingPos = { blackKingX, blackKingY };
			int[] kingPos = selPiece.isWhite ? whiteKingPos : blackKingPos;
			oldPiece = tempPieceState[newXY[0]][newXY[1]];
			tempPieceState[newXY[0]][newXY[1]] = pieceNameLetter;
			if (ChessPiece.inCheck(kingPos, whitesTurn, tempPieceState)) {

				listOfMoves.remove(i);
				i--;
			}
			tempPieceState[newXY[0]][newXY[1]] = oldPiece;
			tempPieceState[oldXY[0]][oldXY[1]] = pieceNameLetter;
			maxLim = listOfMoves.size();
		}
	}

	public void checkForMate() {
		// Checks for checkmate and stalemate
		upDateBoard(false);
		gameState = 0;
		if (!sufficientMaterial()) {
			gameState = 3800;
			return;
		}

		ArrayList<int[]> legalMoves = new ArrayList<int[]>();
		for (int i = 0; i < pieces.size(); i++) {
			if (pieces.get(i).isWhite != whitesTurn) {
				continue;
			}

			ArrayList<int[]> tempLegal = pieces.get(i).showLegalMoves(boardState);
			removeIllegalMoves(pieces.get(i), tempLegal);
			legalMoves.addAll(tempLegal);
			if (legalMoves.size() > 0) {
				return;
			}
		}

		int[] whiteKingPos = { whiteKingX, whiteKingY };
		int[] blackKingPos = { blackKingX, blackKingY };
		int[] kingPos = whitesTurn ? whiteKingPos : blackKingPos;
		if (ChessPiece.inCheck(kingPos, whitesTurn)) {
			gameState = whitesTurn ? 2100 : 1100;
			if (whitesTurn) {
				if (player1isWhite) {
					player2score++;
				} else {
					player1score++;
				}
			} else {
				if (player1isWhite) {
					player1score++;
				} else {
					player2score++;
				}
			}
		} else {
			gameState = 3500;
		}

	}

	public boolean sufficientMaterial() {
		int whiteMinorPieces = 0;
		int blackMinorPieces = 0;
		String oneIsEnough = "ZQERAPS";
		String minorPieces = "BNG";
		for (int i = 0; i < pieces.size(); i++) {
			if (oneIsEnough.contains(pieces.get(i).name.charAt(0) + "")) {
				return true;
			}
			if (minorPieces.contains(pieces.get(i).name.charAt(0) + "")) {
				if (pieces.get(i).isWhite) {
					whiteMinorPieces++;
				} else {
					blackMinorPieces++;
				}
			}
		}
		return (whiteMinorPieces > 1 || blackMinorPieces > 1);
	}

	public char[][] copyOf(char[][] matrix) {
		char[][] output = new char[matrix.length][matrix[0].length];
		for (int r = 0; r < matrix.length; r++) {
			for (int c = 0; c < matrix[0].length; c++) {
				output[r][c] = matrix[r][c];
			}
		}
		return output;
	}

	public char[][] upDateBoard(boolean isTemporary) {
		// updates the boardState and PieceState 2d arrays
		char[][] outputState = isTemporary ? new char[pieceState.length][pieceState[0].length] : pieceState;
		for (int i = 1; i < squareCount + 1; i++) {
			for (int j = 1; j < squareCount + 1; j++) {
				boardState[i][j] = 0;
				outputState[i][j] = '.';
			}
		}
		for (int i = 0; i < pieces.size(); i++) {
			int boardX = pieces.get(i).x;
			int boardY = pieces.get(i).y;
			char letterName = pieces.get(i).name.charAt(0);
			if (pieces.get(i).name.equals("King")) {
				if (pieces.get(i).isWhite) {
					whiteKingX = boardX;
					whiteKingY = boardY;
				} else {
					blackKingX = boardX;
					blackKingY = boardY;
				}
			}
			if (pieces.get(i).isWhite) {
				boardState[boardX][boardY] = 1;
				outputState[boardX][boardY] = Character.toLowerCase(letterName);
			} else if (!pieces.get(i).isWhite) {
				boardState[boardX][boardY] = 2;
				outputState[boardX][boardY] = letterName;
			}
		}
		if (!isTemporary) {
			for (int i = 0; i < pieces.size(); i++) {
				ChessPiece.setPieceState(pieceState);
			}
		}
		return outputState;
	}

	public void fillBorder(int size, char[][] outputState) {
		if (outputState == null) {
			outputState = pieceState;
		}
		for (int i = 0; i < size + 2; i++) {
			boardState[0][i] = 3;
			boardState[size + 1][i] = 3;
			boardState[i][0] = 3;
			boardState[i][size + 1] = 3;
			outputState[0][i] = '§';
			outputState[size + 1][i] = '§';
			outputState[i][0] = '§';
			outputState[i][size + 1] = '§';

		}
	}

	public void reset() {
		pieces.clear();
		pieceState = null;
		boardState = null;
		board = null;
		gameState = 0;
		legalMoves = emptyArrList;
		selectedPiece = null;
		whitesTurn = true;
		squareCount = 0;
		screen = 1;
		menu.done = false;
	}

	public void startGame(int squareCount, double time, int increment) {
		screen = 3;
		String[] fens = { "0", "1", "2", "3", "KR2/PN2/2np/2rk", "RNKQB/PPPPP/5/ppppp/bqknr",
				"RNQKNR/PPPPPP/6/6/pppppp/rnqknr", "RNBQKNR/PPPPPPP/7/7/7/ppppppp/rnbqknr",
				"RNBQKBNR/PPPPPPPP/8/8/8/8/pppppppp/rnbqkbnr", "RNBQKEBNR/PPPPPPPPP/9/9/9/9/9/ppppppppp/rnbqkebnr",
				"RCNBQKBNCR/PPPPSSPPPP/10/10/10/10/10/10/ppppsspppp/rcnbqkbncr",
				"RCNBQKBENCR/SPPPPSPPPPS/11/11/11/11/11/11/11/sppppspppps/rncbqkbencr",
				"RCNBAQKEBNCR/SPPPP2PPPPS/5SS5/12/12/12/12/12/12/5ss5/spppp2pppps/rcnbaqkebncr",
				"RCNBAQKZEBNCR/SSPPP3PPPSS/5P1P5/6P6/13/13/13/13/13/6p6/5p1p5/ssppp3pppss/rcnbaqkzebncr",
				"RCNBE1QK1EBNCR/SSPPPA2ZPPPSS/5P2P5/6PP6/14/14/14/14/14/14/6pp6/5p2p5/sspppa2zpppss/rcnbe1qk1ebncr" };
		this.squareCount = squareCount;
		pieceState = new char[squareCount + 2][squareCount + 2];
		boardState = new int[squareCount + 2][squareCount + 2];
		fillBorder(squareCount, null);
		board = new ChessBoard(HEIGHT, WIDTH - HEIGHT, 0, squareCount);
		board.setTime(time, increment);
		createPosition(fens[squareCount]);
		upDateBoard(false);
		player1isWhite = (Math.random() > .5);
	}

	public void createPosition(String pieceString) {
		int boardX = 1;
		int boardY = squareCount;
		for (int i = 0; i < pieceString.length(); i++) {
			char currentChar = pieceString.charAt(i);
			if (currentChar == '/') {
				boardX = 1;
				boardY--;

			} else if (currentChar > 48 && currentChar < 58) {

				String numStr = "";
				while (currentChar > 47 && currentChar < 58) {
					numStr += currentChar - 48;
					i++;
					if (i < pieceString.length()) {
						currentChar = pieceString.charAt(i);
					} else {
						break;
					}
				}
				i--;
				int numToSkip = Integer.parseInt(numStr);
				for (int k = 0; k < numToSkip; k++) {
					boardX++;
				}

			} else {
				boolean isLower = (currentChar > 96 && currentChar < 123);
				currentChar = Character.toLowerCase(currentChar);
				if (currentChar == 'p') {
					pieces.add(new ChessPawn(boardX, boardY, isLower));
				} else if (currentChar == 'n') {
					pieces.add(new ChessKnight(boardX, boardY, isLower));
				} else if (currentChar == 'b') {
					pieces.add(new ChessBishop(boardX, boardY, isLower));
				} else if (currentChar == 'r') {
					pieces.add(new ChessRook(boardX, boardY, isLower));
				} else if (currentChar == 'q') {
					pieces.add(new ChessQueen(boardX, boardY, isLower));
				} else if (currentChar == 'k') {
					pieces.add(new ChessKing(boardX, boardY, isLower));
				} else if (currentChar == 'e') {
					pieces.add(new ChessChancellor(boardX, boardY, isLower));
				} else if (currentChar == 'c') {
					pieces.add(new ChessCamel(boardX, boardY, isLower));
				} else if (currentChar == 's') {
					pieces.add(new ChessSoldier(boardX, boardY, isLower));
				} else if (currentChar == 'g') {
					pieces.add(new ChessGeneral(boardX, boardY, isLower));
				} else if (currentChar == 'z') {
					pieces.add(new ChessAmazon(boardX, boardY, isLower));
				} else if (currentChar == 'a') {
					pieces.add(new ChessArchbishop(boardX, boardY, isLower));
				}
				boardX++;
			}
		}
	}

	// main method with standard graphics code
	public static void main(String[] args) {
		FontLoader.loadFonts();
		Chess thisgame = new Chess();
		ChessPiece.setGame(thisgame);
		ChessMenu.setGame(thisgame);
		ChessBoard.setGame(thisgame);

		JFrame frame = new JFrame("Chess");
		frame.setSize(WIDTH + 18, HEIGHT + 47);
		frame.setLocation(500, 0);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(thisgame);
		frame.setVisible(true);

	}

}
