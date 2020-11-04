package chat5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer {

	// 맴버변수
	static ServerSocket serverSocket = null;
	static Socket socket = null;

	// 생성자
	public MultiServer() {
		// 실행부 없음
	}

	// 서버의 초기화를 담당할 메소드
	public void init() {

		try {

			// 클라이언트의 접속을 대기
			serverSocket = new ServerSocket(9999);
			System.out.println("서버가 시작되었습니다.");

			/*
			 1명의 클라이언트가 접속할 때마다 허용(accept())해주고
			 동시에 MultiServerT 쓰레드를 생성한다.
			 해당 쓰레드는 1명의 클라이언트가 전송하는 메세지를 읽어서 Echo
			 해주는 역할을 한다.
			 */
			while (true) {
				// 접속요청 허가
				socket = serverSocket.accept();

				//내부클래스의 객체 생성 및 쓰레드 시작
				//객체를 생성해야 새로운 클라이언트가 하나의 소켓을 할당받을 수 있다.
				Thread mst = new MultiServerT(socket);
				
				/*
				 mst 쓰레드의 역할 : 객체를 생성하고 그 객체를 실행하는 동안에 다른 역할을 할 수 
				 없다. 그러므로 쓰레드를 생성해야한다.
				 */
				//mst.run();
				mst.start();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 chat4까지 init()이 static이었으나, chat5부터는 일반적인 
	 메소드로 변경되었다. 따라서 객체를 생성 후 호출하는 방식으로 변경된다.
	 */
	// main()은 프로그램의 출발점 역할만 담당한다.
	public static void main(String[] args) {
		MultiServer ms = new MultiServer();
		ms.init();
	}

	//내부클래스
	/*
	 init()에 기술되었던 스트림을 생성 후 메세지를 읽기/쓰기 하던 부분이
	 해당 내부클래스로 이동되었다.
	 */
	class MultiServerT extends Thread {
		Socket socket;
		PrintWriter out = null;
		BufferedReader in = null;

		/*
		생성자 : 1명의 클라이언트가 접속할 때 생성했던 Socket객체를 
				매개변수로 받아 이를 기반으로 입출력 스트림을 생성하고 있다.
		 */
		public MultiServerT(Socket socket) {
			this.socket = socket;
			try {
				out = new PrintWriter(this.socket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			} catch (Exception e) {
				System.out.println("예외 : " + e);
			}
		}

		/*
		 쓰레드로 동작할 run()에서는 클라이언트의 
		 접속자명과 메세지를 지속적으로 읽어 Echo 해주는 역할을 하고 있다.
		 */
		@Override
		public void run() {
			String name = "";
			String s = "";

			try {
				//접속자명을 Echo
				if (in != null) {
					name = in.readLine();
					// 접속자명을 서버의 콘솔로 출력하고...
					System.out.println(name + " 접속");
					// 클라이언트로 Echo해준다.
					out.println("> " + name + "님이 접속했습니다.");
				}

				while (in != null) {
					s = in.readLine();
					if (s == null) {
						break;
					}
					// 읽어온 메세지를 서버의 콘솔에 출력하고...
					System.out.println(name + " ==> " + s);
					// 클라이언트에게 Echo해준다.
					sendAllMsg(name, s);

				}
			} catch (Exception e) {
				System.out.println("예외 : " + e);
			}
		}
		
		// 서버가 클라이언트에게 메세지를 Echo해주는 메소드
		public void sendAllMsg(String name, String msg) {
			try {
				out.println("> " + name + " ==> " + msg);
			} catch (Exception e) {
				System.out.println("예외 : " + e);
			}
		}

	}
}