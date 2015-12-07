package mazeTest;

public class Cell {
	public static final int CELL_TYPE_CORRIDOR = 0;
	public static final int CELL_TYPE_WALL = 1;
	public static final int CELL_TYPE_START = 2;
	public static final int CELL_TYPE_END = 3;

	private int cellType = 1;
	private String displayValue = "1";
	private boolean onRoute = false;

	public Cell(int cellType) {
		this.cellType = cellType;
	}

	public int getCellType() {
		return cellType;
	}

	public void setCellType(int cellType) {
		this.cellType = cellType;
	}

	public String getDisplayValue() {
		if (cellType == CELL_TYPE_WALL) {
			displayValue = "#";
		} else if (cellType == CELL_TYPE_CORRIDOR) {
			if (!onRoute) {
				displayValue = " ";
			} else {
				displayValue = "X";
			}
		} else if (cellType == CELL_TYPE_START) {
			displayValue = "S";
		} else if (cellType == CELL_TYPE_END) {
			displayValue = "E";
		}
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	public boolean isOnRoute() {
		return onRoute;
	}

	public void setOnRoute(boolean onRoute) {
		this.onRoute = onRoute;
	}

}
