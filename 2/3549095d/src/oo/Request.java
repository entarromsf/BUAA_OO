package oo;
import java.math.*;
public class Request {
	private String who_request;
	private int target;
	private String direction;
	private BigDecimal request_time;
	private boolean isError;
	public Request(String request) {
		request = request.replace(" ", "");
		String regex1 = "\\(FR,\\+?\\d+,((?:DOWN)|(?:UP)),\\+?\\d+\\)";
		String regex2 = "\\(ER,\\+?\\d+,\\+?\\d+\\)";
		if (!(request.matches(regex1)||request.matches(regex2))) {
			isError = true;
		}
		else if (request.matches(regex1)) {
				request = request.replaceAll("[()]", "");
				String[] request_split = request.split(",");
				who_request = request_split[0];
				direction = request_split[2];
				try {
					target = Integer.parseInt(request_split[1]);
					request_time = new BigDecimal(request_split[3]);
					BigDecimal max_unsigned_int = new BigDecimal("4294967295");
					if ((target==1&&direction.equals("DOWN"))||(target==10&&direction.equals("UP"))||target>10||target<1||request_time.compareTo(max_unsigned_int)==1)
						isError = true;
					else isError = false;
				}
				catch(Exception e) {
					isError = true;
				}
			}
			else if (request.matches(regex2)) {
				request = request.replaceAll("[()]", "");
				String[] request_split = request.split(",");
				who_request = request_split[0];
				
				try {
					target = Integer.parseInt(request_split[1]);
					request_time = new BigDecimal(request_split[2]);
					BigDecimal max_unsigned_int = new BigDecimal("4294967295");
					if (target>10||target<1||request_time.compareTo(max_unsigned_int)==1)
						isError = true;
					else isError = false;
				}
				catch(Exception e) {
					isError = true;
				}
				
			}
	}
	public boolean get_isError() {
		return isError;
	}
	public String get_who_request() {
		return who_request;
	}
	public int get_target() {
		return target;
	}
	public String get_direction() {
		return direction;
	}
	public BigDecimal get_request_time() {
		return request_time;
	}
}
