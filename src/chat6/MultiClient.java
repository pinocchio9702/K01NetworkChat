package chat6;

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
		
		//PrintWriter out = null;
		
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
			
			Thread receiver = new Receiver(socket);
			/*
			쓰레드를 실행하지 않으면 두번째 클라이언트부터는 글을 쓸 수가 없다.
			자바 프로세스는 하나로 돌아가는데 비해 두개의 클라이언트가 쓰는것을 원하기 때문에
			 */
			receiver.start();
			
			Thread sender = new Sender(socket, s_name);
			/*
			sender쓰레드를 실행하지 않으면 두번쨰 클라이언트부터는 서버에 글이 전송되지 않는다.
			 */
			sender.start();

			/*
			 소켓이 close되기 전이라면 클라이언트는 지속적으로 서버로 
			 메세지를 보낼 수 있다. 
			 */
			//out은 처음에 이름을 보내기 때문에 while문 안으로 들어온다.
			
		}
		catch (Exception e) {
			System.out.println("예외발생[MultiClient]" + e);
		}
		
	}

}
