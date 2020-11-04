package chat4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MultiClient {

	public static void main(String[] args) {
		//클라이언트의 접속사명을 입력
		System.out.println("이름을 입력하세요 :");
		Scanner scan = new Scanner(System.in);
		String s_name = scan.nextLine();
		
		PrintWriter out = null;
		
		//서버의 메세지를 읽어오는 기능을 Receiver클래스로 옮김
		//BufferedReader in = null;
		
		try {
			/*
			C: \bin>java chat3.MultiClient  접속할 IP주소
				=> 위와 같이 접속하면 로컬이 아닌 내가 원하는 서버로 
			접속할 수 있다.
			 */
			String ServerIP = "localhost";
			
			if(args.length > 0) {
				ServerIP = args[0];
			}

			Socket socket = new Socket(ServerIP, 9999);
			System.out.println("서버와 연결되었습니다...");
			
			//서버에서 보내는 메세지를 읽어올 Receiver쓰레드 객체생성 및 시작
			Thread receiver = new Receiver(socket);
			//독립쓰레드 : main()함수 쓰레드가 죽어도 계속 쓰레드가 실행됨
			//setDaemon(true) 선언이 없으므로 독립쓰레드로 생성된다.
			receiver.start();
			
			//char2와 다르게 in은 정의하지 않음
			out =  new PrintWriter(socket.getOutputStream(), true);
			
			
			out.println(s_name);//최초로 접속자의 이름을 전송

			/*
			 소켓이 close되기 전이라면 클라이언트는 지속적으로 서버로 
			 메세지를 보낼 수 있다. 
			 */
			//out은 처음에 이름을 보내기 때문에 while문 안으로 들어온다.
			
			//이름 전송이후에는 메세지를 지속적으로 전송함.
			while(out != null) {
				try {
					
					//클라이언트가 서버에 out해주면 서버가 받고 그 메세지를 다시 out해주면 
					//쓰레드도 받고 작성하는건 계속 돌아가고 있고 클라이언트는 계속 작성할 수 있다.
					
					//클라이언트는 내용을 입력한 후 서버로 전송한다.
					String s2 = scan.nextLine();
					//만약 입력값이 q(Q)라면 while루푸를 탈출한다.
					if(s2.equals("q") || s2.equals("Q")) {
						break;
					}
					else {
						//q가 아니라면 서버로 입력한 내용을 전송한다.
						out.println(s2);
					}
				}
				catch(Exception e) {
					System.out.println("예외 : " + e);
				}
			}
			
			//클라이언트가 q를 입력하면 소켓과 스트림을 모두 지원해제한다.
			//스트림과 소켓을 종료한다.
			out.close();
			socket.close();
			
			
		}
		catch (Exception e) {
			System.out.println("예외발생[MultiClient]" + e);
		}
		
	}

}
