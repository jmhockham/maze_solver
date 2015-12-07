package mazeTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
 * This is a simplified version of the A* pathing. 
 * a good walk-through can be found here:
 * http://www.policyalmanac.org/games/aStarTutorial.htm 
 */

public class CreatePathCommand {

	private ArrayList<ArrayList<Cell>> maze;
	private int[] size;
	private int[] startPos;
	private int[] endPos;
	private ArrayList<Waypoint> mazeRoute;
	private ArrayList<Waypoint> openList;
	private ArrayList<Waypoint> closedList;
	private WaypointComparator waypointComparator;

	public CreatePathCommand(ArrayList<String> inputFileLines) {
		String[] sizeStrArr = inputFileLines.get(0).split(" ");
		size = new int[] { Integer.parseInt(sizeStrArr[0]), Integer.parseInt(sizeStrArr[1]) };

		String[] startPosStrArr = inputFileLines.get(1).split(" ");
		startPos = new int[] { Integer.parseInt(startPosStrArr[0]), Integer.parseInt(startPosStrArr[1]) };

		String[] endPosStrArr = inputFileLines.get(2).split(" ");
		endPos = new int[] { Integer.parseInt(endPosStrArr[0]), Integer.parseInt(endPosStrArr[1]) };

		maze = new ArrayList<ArrayList<Cell>>();

		for (int i = 3; i < inputFileLines.size(); i++) {
			String[] mazeRowData = inputFileLines.get(i).split(" ");
			ArrayList<Cell> mazeRow = new ArrayList<Cell>();
			for (int j = 0; j < mazeRowData.length; j++) {
				int cellType = Integer.parseInt(mazeRowData[j]);
				Cell c = new Cell(cellType);
				mazeRow.add(c);
			}
			maze.add(mazeRow);
		}
		// caching this so that we don't always create a new one
		waypointComparator = new WaypointComparator();
	}

	public void createPath() {
		closedList = new ArrayList<Waypoint>();
		openList = new ArrayList<Waypoint>();
		mazeRoute = new ArrayList<Waypoint>();
		Cell startCell = maze.get(startPos[0]).get(startPos[1]);
		startCell.setCellType(Cell.CELL_TYPE_START);
		Cell endCell = maze.get(endPos[0]).get(endPos[1]);
		endCell.setCellType(Cell.CELL_TYPE_END);

		// populate the list of cells we can't pick for our route
		for (int y = 0; y < maze.size(); y++) {
			ArrayList<Cell> mazeRows = maze.get(y);
			for (int x = 0; x < mazeRows.size(); x++) {
				Cell cell = mazeRows.get(x);
				if (cell.getCellType() == Cell.CELL_TYPE_WALL) {
					int[] pos = new int[] { y, x };
					Waypoint w = new Waypoint(-1, -1, null, pos);
					closedList.add(w);
				}
			}
		}

		int hValue = ((endPos[0] - startPos[0]) * 10) + ((endPos[1] - startPos[1]) * 10);
		Waypoint startWaypoint = new Waypoint(0, hValue, null, startPos);
		closedList.add(startWaypoint);

		Waypoint next = startWaypoint;
		while (next != null) {
			next = getNextWaypoint(next);
		}

	}

	private Waypoint getNextWaypoint(Waypoint parentWaypoint) {
		int[] parentPos = parentWaypoint.getPos();
		int yParent = parentPos[0];
		int xParent = parentPos[1];

		Waypoint next = null;
		boolean finished = false;

		Waypoint rightWaypoint = createWaypoint(parentWaypoint, yParent, xParent + 1);
		if (testIfFinished(rightWaypoint)) {
			next = rightWaypoint;
			finished = true;
		}
		Waypoint leftWaypoint = createWaypoint(parentWaypoint, yParent, xParent - 1);
		if (testIfFinished(leftWaypoint)) {
			next = leftWaypoint;
			finished = true;
		}
		Waypoint topWaypoint = createWaypoint(parentWaypoint, yParent - 1, xParent);
		if (testIfFinished(topWaypoint)) {
			next = topWaypoint;
			finished = true;
		}
		Waypoint bottomWaypoint = createWaypoint(parentWaypoint, yParent + 1, xParent);
		if (testIfFinished(bottomWaypoint)) {
			next = bottomWaypoint;
			finished = true;
		}

		if (finished) {
			boolean creatingRoute = true;
			while (creatingRoute) {
				mazeRoute.add(next);
				Cell cell = getCellForPos(next.getPos());

				if (cell.getCellType() == Cell.CELL_TYPE_START) {
					creatingRoute = false;
					Collections.reverse(mazeRoute);
				} else {
					next = next.getParent();
				}
			}
			return null;
		} else {
			Collections.sort(openList, waypointComparator);
			java.util.Iterator<Waypoint> iter = openList.iterator();
			next = iter.next();
			iter.remove();
			closedList.add(next);
			// getNextWaypoint(next);
			return next;
		}

	}

	private boolean testIfFinished(Waypoint waypoint) {
		if (waypoint != null) {
			Cell cell = getCellForPos(waypoint.getPos());
			if (cell.getCellType() == Cell.CELL_TYPE_END) {
				return true;
			}
		}
		return false;
	}

	private class WaypointComparator implements Comparator<Waypoint> {

		@Override
		public int compare(Waypoint waypoint1, Waypoint waypoint2) {
			return waypoint1.getFValue() < waypoint2.getFValue() ? -1
					: waypoint1.getFValue() == waypoint2.getFValue() ? 0 : 1;
		}

	}

	private Cell getCellForPos(int[] pos) {
		int y = pos[0];
		int x = pos[1];
		Cell cell = maze.get(y).get(x);
		return cell;
	}

	private Waypoint createWaypoint(Waypoint parentWaypoint, int y, int x) {
		Waypoint w = null;

		// check the closed list
		boolean waypointOnClosedList = false;
		for (int i = 0; i < closedList.size(); i++) {
			Waypoint waypoint = closedList.get(i);
			int[] pos = waypoint.getPos();
			if (y == pos[0] && x == pos[1]) {
				waypointOnClosedList = true;
				break;
			}
		}

		// now check the open list
		if (!waypointOnClosedList) {
			boolean alreadyOnOpenList = false;
			for (int i = 0; i < openList.size(); i++) {
				Waypoint waypoint = openList.get(i);
				int[] pos = waypoint.getPos();
				if (y == pos[0] && x == pos[1]) {
					alreadyOnOpenList = true;
					w = waypoint;
					break;
				}
			}
			if (!alreadyOnOpenList) {
				int gValue = parentWaypoint.getGValue() + 10;
				int hValue = ((endPos[0] - y) * 10) + ((endPos[1] - x) * 10);
				int[] cellPos = new int[] { y, x };
				w = new Waypoint(gValue, hValue, parentWaypoint, cellPos);
				openList.add(w);
			}
		}
		// System.gc();
		return w;
	}

	public void printPath() {
		if (mazeRoute != null && mazeRoute.size() > 0) {
			for (Waypoint waypoint : mazeRoute) {
				Cell cellForPos = getCellForPos(waypoint.getPos());
				cellForPos.setOnRoute(true);
			}
		}
		for (ArrayList<Cell> mazeRow : maze) {
			String row = "";
			for (Cell cell : mazeRow) {
				row += cell.getDisplayValue();
			}
			System.out.println(row);
		}
	}

}
