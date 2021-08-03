package framework.test.interceptor;

/**
 * Echo Service
 */
public class EchoService {
	@Logging
	public String echo(String message) {
		return "[ECHO] : " + message;
	}
}
