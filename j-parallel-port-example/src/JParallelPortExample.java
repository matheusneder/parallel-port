import java.io.IOException;
import matheus.jparallelport.JParallelPort;

public class JParallelPortExample {
	public static void main(String[] args) {
		JParallelPort parport = new JParallelPort();
		try {
			parport.open(); // open port

			// write 0 value to data pins (usually 0x378) put all pins to low
			parport.writeData((byte) 0);

			// write 0 value to control pins (usually 0x37a) put C0, C1 and C3
			// to high
			// and C2 to low
			parport.writeControl((byte) 0);

			// read from status and print it as an integer
			System.out.println("Status: " + (int) parport.readStatus());

			parport.close(); // close port
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}
