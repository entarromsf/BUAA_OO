package simulatedElevator;

class Request {
	private int req_floor;
	private double req_time;
	private short req_type; // er:0 up:1 down:2
	private int eleva_num;
	short flag; // 0: 未执行 1: 已执行
	int num;

	Request(int req_floor, double req_time, String a) {
		this.req_floor = req_floor;
		this.req_time = req_time;
		if (a.equals("UP")) {
			this.req_type = 1;
		} else {
			this.req_type = 2;
		}
		this.eleva_num = 0;
		this.flag = 0;
		this.num = Main.effectiveReqCount;
	}

	Request(int req_floor, double req_time, int eleva_num) {
		this.req_floor = req_floor;
		this.req_time = req_time;
		this.req_type = 0;
		this.eleva_num = eleva_num;
	}

	public String toString() {
		// return String.format("(%d,STILL,%.1f)", req_i.req_floor, time );
		if (this.req_type == 0)
			return String.format("ER,#%d,%d,%.1f", this.eleva_num, this.req_floor, this.req_time);
		else
			return String.format("FR,%d,%s,%.1f", this.req_floor, ((this.req_type == 1) ? "UP" : "DOWN"),
					this.req_time);
	}

	int get_req_floor() {
		return this.req_floor;
	}

	double get_req_time() {
		return this.req_time;
	}

	short get_req_type() {
		return this.req_type;
	}

	int get_eleva_num() {
		return this.eleva_num;
	}
}