package chat3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer {

	//맴버변수
	static ServerSocket serverSocket = null;
	static Socket socket = null;
	static PrintWriter out = null;
	static BufferedReader in = null;
	static String s = ""; //클라이언트의 메세지를 저장
	
	//생성자
	public MultiServer() {
		//실행부 없음
	}
	
	//서버의 초기화를 담당할 메소드
	public static void init() {
		
		//클라이언트의 이름을 저장할 변수
		String name = "";
		
		try {
			
			//클라이언트의 접속을 대기
			serverSocket = new ServerSocket(9999);
			System.out.println("서버가 시작되었습니다.");
			
			//접속요청 허가
			socket = serverSocket.accept();
			
			System.out.println(socket.getInetAddress() + ":" +  socket.getPort());
			
			//클라이언트로 메세지를 보낼 준비(output스트림)
			out = new PrintWriter(socket.getOutputStream(), true);
			//클라이언트가 보내준 메세지를 읽을 준비(input 스트림)
			in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			
			//클라이언트가 최초로 전송하는 메세지는 이름이므로....
			if(in != null) {
				name = in.readLine();
				//접속자명을 서버의 콘솔로 출력하고...
				System.out.println(name + " 접속"); 
				//클라이언트로 Echo해준다.
				out.println("> " + name + "님이 접속했습니다."); 
			}
			
			//클라이언트가 전송하는 메세지를 계속해서 읽어온다.
			while(in != null) {
				s = in.readLine();
				if(s == null) {
					break;
				}
				//읽어온 메세지를 서버의 콘솔에 출력하고...
				System.out.println(name + " ==> " + s);
				
				//out.println("> " + name + " ==> " + s); => sendAllMsg() 메소드로 옮겨놈
				
				//클라이언트에게 Echo해준다.
				sendAllMsg(name, s);
				
			}
			
			System.out.println("Bye....!!!");
		}
		catch(Exception e) {
			System.out.println("예외 1 : " + e);
			//e.printStackTrace();
		}
		finally {
			try {
				//입출력스트림 종료
				in.close();
				out.close();
				//소켓종료(자원해제)
				socket.close();
				serverSocket.close();
			}
			catch(Exception e) {
				System.out.println("예외 2 : " + e);
				e.printStackTrace();
			}
		}
	}
	
	//서버가 클라이언트에게 메세지를 Echo해주는 메소드
	public static void sendAllMsg(String name, String msg) {
		try {
			out.println("> " + name + " ==> " + s);
		}
		catch(Exception e) {
			System.out.println("예외 : " + e);
		}
	}
	
	//main()은 프로그램의 출발점 역할만 담당한다.
	public static void main(String[] args) {
		init();	
	}

}
