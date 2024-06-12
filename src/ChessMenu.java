import java.awt.*;
import java.awt.event.*;
import javax.swing.ImageIcon;

public class ChessMenu {
	private MouseEvent e;
	private static Chess chessGame;
	Slider sizeSlider;

	int width;
	int height;
	private int screen;

	private boolean cursorFlash;
	int ticks;
	private int cursorBox = 0;

	private String minutes = "";
	private String seconds = "";
	private String increment = "";
	boolean done;

	private int pieceScreen = 0;

	public ChessMenu(int w, int h) {
		width = w;
		height = h;
		sizeSlider = new Slider(450, 400, 400, 10, new Color(250, 225, 190), new Color(207, 230, 232), 4, 14,
				"Board Size: } x }");
	}

	public static void setGame(Chess thisgame) {
		chessGame = thisgame;
	}

	public void updateClick(MouseEvent e) {
		this.e = e;
	}

	public boolean draw(Graphics g, int drawScreen) {
		screen = drawScreen;
		sortClick();
		ticks++;
		if (ticks > 60) {
			ticks = 0;
			if (cursorFlash) {
				cursorFlash = false;
			} else {
				cursorFlash = true;
			}
		}
		if (screen == 0) {
			drawMainMenu(g);
			return true;
		} else if (screen == 1) {
			drawGameSettings(g);
			return true;
		} else if (screen == 2) {
			drawHelp(g);
			return true;
		} else if (screen == 3) {
			return false;
		}
		return false;
	}

	public void drawMainMenu(Graphics g) {
		g.setColor(new Color(108, 69, 3));
		g.fillRect(0, 0, width, height);

		Font bigFont = new Font("Bauhaus 93", Font.PLAIN, 150);
		FontMetrics metrics = g.getFontMetrics(bigFont);
		int strX = 0 + (width - metrics.stringWidth("XL Chess")) / 2;
		g.setColor(new Color(232, 232, 170));
		g.setFont(bigFont);
		g.drawString("XL Chess", strX, height / 3);

		Font buttonFont = new Font("Bauhaus 93", Font.PLAIN, 50);
		metrics = g.getFontMetrics(buttonFont);
		strX = 0 + (width - metrics.stringWidth("Play")) / 2;
		g.setFont(buttonFont);
		g.drawString("Play", strX, height * 6 / 12);

		strX = 0 + (width - metrics.stringWidth("Help")) / 2;
		g.drawString("Help", strX, height * 8 / 12);
	}

	public void drawGameSettings(Graphics g) {
		g.setColor(new Color(108, 69, 3));
		g.fillRect(0, 0, width, height);
		
		drawBackArrow(g);

		int sizeValue = (e != null && sizeSlider.inBounds(e)) ? sizeSlider.xToValue(e.getX()) : sizeSlider.currentVal;
		sizeSlider.draw(g, sizeValue);

		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(5));
		g2.setColor(Color.white);
		int boxX = 300;
		int boxWidth = 110;

		g2.fillRoundRect(boxX, 470, 80, 60, 10, 10);
		g2.fillRoundRect(boxX + boxWidth, 470, 80, 60, 10, 10);
		g2.fillRoundRect(boxX + boxWidth * 2, 470, 80, 60, 10, 10);

		g2.setColor(Color.black);
		g2.drawRoundRect(boxX, 470, 80, 60, 10, 10);
		g2.drawRoundRect(boxX + boxWidth, 470, 80, 60, 10, 10);
		g2.drawRoundRect(boxX + boxWidth * 2, 470, 80, 60, 10, 10);

		g.setColor(Color.black);
		g.setFont(new Font("Berlin sans", Font.PLAIN, 80));
		g.drawString(":", boxX + 85, 520);
		g.drawString("|", boxX + 195, 520);

		g.setFont(new Font("Berlin sans", Font.PLAIN, 50));
		if (cursorFlash) {
			if (cursorBox == 1) {
				g.drawString("|", 365, 515);
			} else if (cursorBox == 2) {
				g.drawString("|", 475, 515);
			} else if (cursorBox == 3) {
				g.drawString("|", 585, 515);
			}
		}
		g.drawString(minutes, 310, 520);
		g.drawString(seconds, 420, 520);
		g.drawString(increment, 530, 520);

		g.setColor(new Color(250, 225, 190));
		Font buttonFont = new Font("Bauhaus 93", Font.PLAIN, 50);
		FontMetrics metrics = g.getFontMetrics(buttonFont);
		int strX = 0 + (width - metrics.stringWidth("Play")) / 2;
		g.setFont(buttonFont);
		g.drawString("Play", strX, height * 21 / 24);
		//ImageIcon boardImage = new ImageIcon("chess\\boards\\board" + sizeSlider.currentVal + ".png");
		ImageIcon boardImage = new ImageIcon(getClass().getClassLoader().getResource("boards/board" + sizeSlider.currentVal + ".png"));
		g.drawImage(boardImage.getImage(), 300, 40, 300, 300, null);

	}

	public void drawBackArrow(Graphics g) {
		g.setColor(new Color(232, 232, 170));
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(5));
		g2.drawLine(15, 25, 30, 25);
		g2.drawLine(15, 25, 20, 20);
		g2.drawLine(15, 25, 20, 30);
	}
	
	public void drawHelp(Graphics g) {
		g.setColor(new Color(108, 69, 3));
		g.fillRect(0, 0, width, height);
		
		drawBackArrow(g);
		
		ChessPiece[] pieces = { new ChessPawn(1, 1, true), new ChessKnight(1, 1, true), new ChessBishop(1, 1, true),
				new ChessRook(1, 1, true), new ChessQueen(1, 1, true), new ChessKing(1, 1, true),
				new ChessChancellor(1, 1, true), new ChessArchbishop(1, 1, true), new ChessSoldier(1, 1, true),
				new ChessGeneral(1, 1, true), new ChessAmazon(1, 1, true), new ChessCamel(1, 1, true) };
		String[] descriptions = {
				"Pawn: Moves forward 1 or 2 squares on its first move,"
						+ "\nand 1 square forward after that. It captures diagonally, "
						+ "\nand promotes to another piece when it reaches the back row.",
				"Knight: Moves two squares in one direction, then 1 "
						+ "\nsquare in another direction, like an L shape.",
				"Bishop: Moves diagonally in all four directions",
				"Rook: Moves horizontally and vertically any number of squares.",
				"Queen: Moves like a bishop and rook combined",
				"King: Can move one square in any direction. " + "\nThe goal of the game is to put the king in"
						+ "\ncheckmate, or a position in which any square " + "\nthe king moves to can be captured",
				"Chancellor: Moves like a rook and knight combined",
				"Archbishop: Moves like a bishop, but can go two " + "squares horizontall or vertically.",
				"Soldier: Like a pawn, but captures forward and " + "\ncan move sideways 1 square",
				"General: Moves like a knight and a king combined", 
				"Amazon: Moves like a knight and a queen combined",
				"Camel: Moves like a knight, but 3 squares in one " + "\ndirection insted of 2. It can also move 1 square "
						+ "\nhorizontally or vertically, but cannot caputure that way." };
		//ImageIcon image = new ImageIcon("chess\\boards\\piece" + pieceScreen + ".png");
		ImageIcon image = new ImageIcon(getClass().getClassLoader().getResource("boards/piece" + pieceScreen + ".png"));
		g.drawImage(image.getImage(), 500, 100, 300, 300, null);

		//image = new ImageIcon("chess\\white" + pieces[pieceScreen].name + ".png");
		image = new ImageIcon(getClass().getClassLoader().getResource("white" + pieces[pieceScreen].name + ".png")); 
		g.drawImage(image.getImage(), 125, 125, 250, 250, null);

		g.setColor(new Color(232, 232, 170));

		g.setFont(new Font("Berlin Sans FB", Font.PLAIN, 20));
		drawString(g, descriptions[pieceScreen], 100, 450);

		g.drawString("Back", 200, 600);
		g.drawString("Next", 650, 600);
	}

	public void next() {
		pieceScreen++;
		if (pieceScreen >= 12) {
			pieceScreen = 0;
		}
	}

	public void previous() {
		pieceScreen--;
		if (pieceScreen < 0) {
			pieceScreen = 11;
		}
	}

	// borrowed From
	// https://stackoverflow.com/questions/4413132/problems-with-newline-in-graphics2d-drawstring
	void drawString(Graphics g, String text, int x, int y) {
		for (String line : text.split("\n"))
			g.drawString(line, x, y += g.getFontMetrics().getHeight());
	}

	public void drawPromotion(Graphics g, ChessBoard board) {
		// g.fillRect(r * squareSize + x, c * squareSize + y, squareSize, squareSize);
		int drawX = chessGame.getPromotionSpot();
		if (drawX == 0)
			return;
		boolean isWhite = chessGame.isPromotionWhite();
		int sqSize = board.squareSize;
		int drawY = (isWhite) ? 0 : chessGame.getSquareCount() - 4;
		drawY *= sqSize;
		drawY += board.y;
		drawX--;
		drawX *= sqSize;
		drawX += board.x;
		g.setColor(new Color(230, 230, 230, 240));
		g.fillRect(drawX, drawY, sqSize, sqSize * 4);
		String whiteBlack = isWhite ? "white" : "black";
		//ImageIcon image = new ImageIcon("chess\\" + whiteBlack + "Queen.png");
		ImageIcon image = new ImageIcon(getClass().getClassLoader().getResource(whiteBlack + "Queen.png"));
		g.drawImage(image.getImage(), drawX, drawY, sqSize, sqSize, null);

		drawY += sqSize;
		//image = new ImageIcon("chess\\" + whiteBlack + "Rook.png");
		image = new ImageIcon(getClass().getClassLoader().getResource(whiteBlack + "Rook.png"));
		g.drawImage(image.getImage(), drawX, drawY, sqSize, sqSize, null);

		drawY += sqSize;
		//image = new ImageIcon("chess\\" + whiteBlack + "Bishop.png");
		image = new ImageIcon(getClass().getClassLoader().getResource(whiteBlack + "Bishop.png"));
		g.drawImage(image.getImage(), drawX, drawY, sqSize, sqSize, null);

		drawY += sqSize;
		//image = new ImageIcon("chess\\" + whiteBlack + "Night.png");
		image = new ImageIcon(getClass().getClassLoader().getResource(whiteBlack + "Night.png"));
		g.drawImage(image.getImage(), drawX, drawY, sqSize, sqSize, null);

	}

	public void promotionClick(MouseEvent click, ChessBoard board) {
		if (board == null || chessGame.getPromotionSpot() == 0) {
			return;
		}
		int minX = chessGame.getPromotionSpot();
		boolean isWhite = chessGame.isPromotionWhite();
		int sqSize = board.squareSize;
		int minY = (isWhite) ? 0 : chessGame.getSquareCount() - 4;
		minY *= sqSize;
		minY += board.y;
		minX--;
		minX *= sqSize;
		minX += board.x;
		int maxX = minX + sqSize;

		int x = click.getX();
		int y = click.getY();

		if (x > minX && x < maxX) {
			if (y > minY && y < minY + sqSize * 4) {
				int creationY = isWhite ? chessGame.getSquareCount() : 1;
				int[] xy = { chessGame.getPromotionSpot(), creationY };
				ChessPiece newPiece;

				if (y < minY + sqSize) {
					newPiece = new ChessQueen(xy[0], xy[1], isWhite);
				} else if (y < minY + sqSize * 2) {
					newPiece = new ChessRook(xy[0], xy[1], isWhite);
				} else if (y < minY + sqSize * 3) {
					newPiece = new ChessBishop(xy[0], xy[1], isWhite);
				} else {
					newPiece = new ChessKnight(xy[0], xy[1], isWhite);
				}

				newPiece.setPieceAsMoved();
				chessGame.removePiece(xy);
				chessGame.addPiece(newPiece);
				chessGame.setPromotionInfo(0, false);
				chessGame.upDateBoard(false);
				chessGame.checkForMate();
			}
		}
	}

	public void sortClick() {
		if (e != null && e.getID() == MouseEvent.MOUSE_RELEASED && !done) {
			int x = e.getX();
			int y = e.getY();
			if (screen == 1) {
				if (y > 480 && y < 530) {
					if (x > 300 && x < 380) {
						cursorBox = 1;
					} else if (x > 410 && x < 490) {
						cursorBox = 2;
					} else if (x > 520 && x < 600) {
						cursorBox = 3;
					} else {
						cursorBox = 0;
					}
				} else {
					cursorBox = 0;
					if (x > 400 && y > 580 && x < 500 && y < 625) {
						play();
						done = true;
					}
				}
			}

		}
	}

	public void processKey(KeyEvent k) {
		int key = k.getKeyCode();
		String currentString = "";

		if (cursorBox == 1) {
			currentString = minutes;
		} else if (cursorBox == 2) {
			currentString = seconds;
		} else if (cursorBox == 3) {
			currentString = increment;
		}
		if (key > 47 && key < 58) {
			currentString += key - 48;
		} else if (key > 95 && key < 106) {
			currentString += key - 96;
		} else if (key == 8) {
			if (currentString.length() > 1) {
				currentString = currentString.substring(0, currentString.length() - 1);
			} else {
				currentString = "";
			}
		}

		if (currentString.length() > 2) {
			currentString = currentString.substring(currentString.length() - 2, currentString.length());
		}
		if (cursorBox == 1) {
			minutes = currentString;
		} else if (cursorBox == 2) {
			seconds = currentString;
		} else if (cursorBox == 3) {
			increment = currentString;
		}
	}

	public void play() {
		if (minutes.equals("")) {
			minutes = "0";
		}
		if (seconds.equals("")) {
			seconds = "0";
		}
		if (increment.equals("")) {
			increment = "0";
		}
		int minutesNum = Integer.parseInt(minutes);
		int secondsNum = Integer.parseInt(seconds);
		int increNum = Integer.parseInt(increment);

		if (secondsNum > 60) {
			secondsNum -= 60;
			minutesNum++;
		}

		double timeDouble = minutesNum + secondsNum / 100.0;
		chessGame.startGame(sizeSlider.currentVal, timeDouble, increNum);
	}

}
