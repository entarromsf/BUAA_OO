package simulatedElevator;

class Floor {
	private boolean[] light = { false, false }; // 0:up 1:down

	void set_light(int i, boolean change) {
		this.light[i] = change;
	}

	boolean get_light(int t) {
		return this.light[t];
	}

}
