
//Arush Bodla Block 5
import java.awt.*;
import java.util.ArrayList;

public class ChessBoard {
	private Color lightColor;
	private Color darkColor;
	private Color circleColor;

	private int size;
	private int squareCount;
	public int squareSize;

	public int x;
	public int y;

	private double whiteTime;
	private double blackTime;
	public boolean gameEnded = false;

	public static Chess chessGame;
	private int increment;

	public ChessBoard() {
		lightColor = new Color(230, 230, 230);
		darkColor = new Color(1, 120, 120);
		circleColor = new Color(116, 175, 175, 128);
		size = 900;
		x = 0;
		y = 0;
		squareSize = size / 8;

	}

	public ChessBoard(int size, int x, int y, int squareCount) {
		this.size = size;
		lightColor = new Color(232, 217, 155);
		darkColor = new Color(117, 78, 39);
		circleColor = new Color(50, 45, 40, 200);

		this.x = x;
		this.y = y;
		this.squareCount = squareCount;
		squareSize = size / squareCount;
	}

	public static void setGame(Chess game) {
		chessGame = game;
	}

	public void setTime(double time, int incSeconds) {
		int minutes = (int) Math.floor(time);
		double tempSeconds = time;
		while (tempSeconds >= 1) {
			tempSeconds--;
		}
		int seconds = (int) (tempSeconds * 100);
		increment = incSeconds;
		whiteTime += minutes * 60 + seconds;
		blackTime += minutes * 60 + seconds;
	}

	public void draw(Graphics g) {
		g.setColor(darkColor);
		int counter = 0;
		for (int r = 0; r < squareCount; r++) {
			for (int c = 0; c < squareCount; c++) {
				Color tempCol = (counter % 2 == 0) ? lightColor : darkColor;
				g.setColor(tempCol);
				g.fillRect(r * squareSize + x, c * squareSize + y, squareSize, squareSize);
				counter++;
			}
			if (squareCount % 2 == 0) {
				counter++;
			}
		}
		// this isn't .01 because the calculations running cause the timer to be off
		if (chessGame.getWhitesTurn()) {
			whiteTime -= (whiteTime > 0) ? .015 : 0;
		} else {
			blackTime -= (whiteTime > 0) ? .015 : 0;
		}

		if (whiteTime < 0) {
			chessGame.setGameState(2200);
			whiteTime = -0.01;
			if (!gameEnded) {
				chessGame.addScore(!chessGame.isPlayerOneWhite());
				gameEnded = true;
			}
		}
		if (blackTime < 0) {
			chessGame.setGameState(1200);
			blackTime = -0.01;
			if (!gameEnded) {
				chessGame.addScore(chessGame.isPlayerOneWhite());
				gameEnded = true;
			}
		}
		String whiteTimeStr = timeFormat(whiteTime);
		String blackTimeStr = timeFormat(blackTime);
		g.setColor(Color.white);
		g.fillRect(25, 600, 150, 75);
		g.fillRect(25, 500, 150, 75);
		g.setColor(new Color(49, 46, 43));
		g.fillRect(25, 25, 150, 75);
		g.fillRect(25, 125, 150, 75);

		Font timeFont = new Font("Berlin Sans", Font.PLAIN, size / 24);
		g.setFont(timeFont);
		FontMetrics metrics = g.getFontMetrics(timeFont);
		int strX = (25) + (150 - metrics.stringWidth(whiteTimeStr)) / 2;
		int strY = 648;
		g.drawString(whiteTimeStr, strX, strY);
		
		g.setColor(Color.white);
		strY = 73;
		g.drawString(blackTimeStr, strX, strY);
		
		

		boolean p1sTurn = chessGame.isPlayerOnesTurn();
		drawPerson(g, -1, p1sTurn, 100, 330);
		if (p1sTurn) {
			g.setColor(new Color(242, 109, 82));
		} else {
			g.setColor(new Color(82, 194, 242));
		}

		String turnString = (p1sTurn) ? "Player 1's Turn" : "Player 2's Turn";
		Font scoreFont = new Font("Arial Rounded MT Bold", Font.BOLD, 18);
		g.setFont(scoreFont);
		metrics = g.getFontMetrics(scoreFont);
		strX = 20 + (150 - metrics.stringWidth(turnString));
		g.drawString(turnString, strX, 400);
		g.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 24));
		g.setColor(new Color(49,46, 43));
		g.drawString("Resign", 55, 545);
		g.setColor(Color.white);
		g.drawString("Resign", 55, 170);
	}

	public String timeFormat(double time) {
		if (time < 0) {
			return "0:00";
		}
		if (time == 0) {
			return "∞";
		}
		double seconds = 0;
		int minutes = 0;
		int hours = 0;
		if (time > 3600) {
			hours = (int) (time / 3600);
			time %= 3600;
		}
		if (time > 60) {
			minutes = (int) (time / 60);
			time %= 60;
		}
		seconds = time;
		if (hours > 0) {
			String o = minutes >= 10 ? "" : "0";
			return hours + ":" + o + minutes;
		}
		if (minutes > 0) {
			String o = seconds >= 10 ? "" : "0";
			return minutes + ":" + o + (int) seconds;
		}
		if (seconds >= 30) {
			return "0:" + (int) seconds;
		}

		String o = seconds >= 10 ? "" : "0";
		return "0:" + o + (Math.floor(seconds * 10) / 10);

	}

	public void endTurn() {
		if (chessGame.getWhitesTurn()) {
			whiteTime += increment;
		} else {
			blackTime += increment;
		}
	}

	public int[] getSquare(int clickX, int clickY) {
		int sqct = chessGame.getSquareCount();
		clickX -= x;
		clickY -= y;
		clickX = (int) Math.ceil(clickX * 1.0 / squareSize);
		clickY = (int) Math.ceil(clickY * 1.0 / squareSize);
		if (clickX > sqct || clickY > sqct || clickX < 1 || clickY < 1) {
			int[] s = { 0, 0 };
			return s;
		}
		int[] coords = { clickX, sqct + 1 - clickY };
		return coords;
	}

	public void drawLegalMoves(Graphics g, ArrayList<int[]> moves) {
		for (int i = 0; i < moves.size(); i++) {
			g.setColor(circleColor);
			g.fillOval((moves.get(i)[0] - 1) * squareSize + x + squareSize / 4,
					(chessGame.getSquareCount() - moves.get(i)[1]) * squareSize + y + squareSize / 4,
					squareSize * 1 / 2, squareSize * 1 / 2);
		}
	}

	public void drawWinScreen(Graphics g, int gameState, int score1, int score2) {
		if (gameState == 0)
			return;
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(size / 80));

		g.setColor(new Color(232, 243, 231));
		g.fillRoundRect(x + size * 1 / 3, y + size * 3 / 12, size * 1 / 3, size * 6 / 12, size / 24, size / 24);
		g2.setColor(Color.black);
		g2.drawRoundRect(x + size * 1 / 3, y + size * 3 / 12, size * 1 / 3, size * 6 / 12, size / 24, size / 24);

		// Borrowed from
		// https://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java
		Font headingFont = new Font("Berlin Sans FB", Font.BOLD, size / 18);
		g.setFont(headingFont);
		FontMetrics metrics = g.getFontMetrics(headingFont);
		int strX = (x + size * 1 / 3) + (size * 1 / 3 - metrics.stringWidth("Game Over")) / 2;
		g.drawString("Game Over", strX, 230);

		String winString = "";
		if (gameState / 1000 == 1) {
			winString = "White wins";
		} else if (gameState / 1000 == 2) {
			winString = "Black wins";
		} else if (gameState / 1000 == 3) {
			winString = "Draw";
		}
		gameState %= 1000;

		Font winnerFont = new Font("Berlin Sans FB", Font.PLAIN, size / 22);
		g.setFont(winnerFont);
		metrics = g.getFontMetrics(winnerFont);
		strX = (x + size * 1 / 3) + (size * 1 / 3 - metrics.stringWidth(winString)) / 2;
		g.drawString(winString, strX, 280);

		String methodString = "";
		if (gameState / 100 == 1) {
			methodString = "by checkmate";
		} else if (gameState / 100 == 2) {
			methodString = "on time";
		} else if (gameState / 100 == 3) {
			methodString = "by agreement";
		} else if (gameState / 100 == 4) {
			methodString = "by resignation";
		} else if (gameState / 100 == 5) {
			methodString = "by stalemate";
		} else if (gameState / 100 == 6) {
			methodString = "by repetition";
		} else if (gameState / 100 == 7) {
			methodString = "by 50 move rule";
		} else if (gameState / 100 == 8) {
			methodString = "by lack of pieces";
		}

		strX = (x + size * 1 / 3) + (size * 1 / 3 - metrics.stringWidth(methodString)) / 2;
		g.drawString(methodString, strX, 320);
		drawPerson(g, score1, true, x + size * 17 / 40, y + size * 25 / 48);
		drawPerson(g, score2, false, x + size * 23 / 40, y + size * 25 / 48);

		g2.setColor(Color.black);
		g2.drawRoundRect(460, 430, 180, 70, 20, 20);
		g2.setColor(new Color(127, 240, 135));
		g2.fillRoundRect(460, 430, 180, 70, 20, 20);

		g.setColor(Color.black);
		g.setFont(new Font("Berlin Sans FB", Font.PLAIN, 20));
		g.drawString("Start a new Game", 473, 470);

	}

	public void drawPerson(Graphics g, int score, boolean player1, int x, int y) {
		if (player1) {
			g.setColor(new Color(242, 109, 82));
		} else {
			g.setColor(new Color(82, 194, 242));
		}
		g.fillOval(x - size / 40, y - size / 40, size / 20, size / 20);
		g.fillArc(x - size / 24, y + size / 60, size / 12, size / 10, 0, 180);
		g.setColor(Color.white);
		int fontSize = (score > 99) ? size / 36 : size / 24;
		Font scoreFont = new Font("Arial Rounded MT Bold", Font.BOLD, fontSize);
		g.setFont(scoreFont);
		FontMetrics metrics = g.getFontMetrics(scoreFont);
		int strX = (x - size * 1 / 24) + (size * 1 / 12 - metrics.stringWidth(score + "")) / 2;

		String scoreStr = (score == -1) ? "" : score + "";
		g.drawString(scoreStr + "", strX, y + size * 7 / 120);
	}

}
