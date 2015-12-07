package mazeTest;

public class Waypoint {
	private int gValue = 0;
	private int hValue = 0;
	private int fValue = 0;
	private Waypoint parent = null;
	private int[] pos;

	public Waypoint(int gValue, int hValue, Waypoint parent, int[] pos) {
		this.gValue = gValue;
		this.hValue = hValue;
		this.fValue = gValue + hValue;
		this.parent = parent;
		this.pos = pos;
	}

	public int getGValue() {
		return gValue;
	}

	public void setGValue(int gValue) {
		this.gValue = gValue;
	}

	public int getHValue() {
		return hValue;
	}

	public void setHValue(int hValue) {
		this.hValue = hValue;
	}

	public int getFValue() {
		return fValue;
	}

	public void setFValue(int fValue) {
		this.fValue = fValue;
	}

	public Waypoint getParent() {
		return parent;
	}

	public void setParent(Waypoint parent) {
		this.parent = parent;
	}

	public int[] getPos() {
		return pos;
	}

	public void setPos(int[] pos) {
		this.pos = pos;
	}
	
}
