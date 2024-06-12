import java.awt.*;
import java.awt.event.*;

public class Slider {
	public Color knobColor;
	public Color backgroundColor;
	public int x;
	public int y;
	public int width;
	public int height;
	public int min;
	public int max;
	public int currentVal;
	public String name;

	public Slider(int x, int y, int width, int height, Color kColor, Color bColor, int min, int max, String name) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		knobColor = kColor;
		backgroundColor = bColor;
		this.min = min;
		this.max = max;
		currentVal = min;
		this.name = name;
	}

	public void draw(Graphics g, double value) {
		g.setColor(backgroundColor);
		g.fillRoundRect(x - width / 2, y - height / 2, width, height, height / 2, height / 2);
		g.setColor(knobColor);

		int drawX = (int) (width * ((value - min) / (max - min)));
		g.fillOval(drawX + x - width / 2 - height * 6 / 8, y - height * 6 / 8, height * 6 / 4, height * 6 / 4);

		String stringToDraw = formatDrawString(name);
		Font drawingFont = new Font("Berlin Sans", Font.PLAIN, height * 3 / 2);
		FontMetrics metrics = g.getFontMetrics(drawingFont);
		g.setFont(drawingFont);
		int strX = (x - width / 2) + (width - metrics.stringWidth(stringToDraw)) / 2;
		int strY = (y + height * 9 / 4);
		g.drawString(stringToDraw, strX, strY);
	}

	public int xToValue(double clickX) {
		clickX -= x;
		clickX += width / 2;
		if (clickX < 0 || clickX > width) {
			return currentVal;
		}
		clickX /= width;
		clickX *= max - min;
		clickX += min;
		currentVal = (int) Math.round(clickX);
		return currentVal;
	}

	public boolean inBounds(MouseEvent e) {
		int clickX = e.getX();
		int clickY = e.getY();
		clickX -= x;
		clickX += width / 2;
		if (clickX < 0 || clickX > width) {
			return false;
		}
		if (Math.abs(clickY - y) > height) {
			return false;
		}
		return true;
	}

	private String formatDrawString(String raw) {
		String output = "";
		for (int i = 0; i < raw.length(); i++) {
			if (raw.charAt(i) == '}') {
				output += currentVal;
			} else {
				output += raw.charAt(i);
			}
		}
		return output;
	}

}
