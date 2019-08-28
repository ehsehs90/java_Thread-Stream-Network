package javaNetwork;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class User {

	ArrayList<EchoRunnable> sharedObject = new ArrayList<EchoRunnable>();
	String msg;
public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	//	public synchronized void AddClient(String name, Socket socket) {
//		try {
//			//sharedObject.add(new DataOutputStream(socket.getOutputStream()));
//			sendMsg(name + "입장 했습니다 ","Server");
//			sharedObject.add(new DataOutputStream(socket.getOutputStream()),name );
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	public  void add(EchoRunnable e) {
		sharedObject.add(e);
	}
	public synchronized void Remove(String name) {
		try {
			sharedObject.remove(name);
			System.out.println("리무브소켓");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void sendMsg() throws Exception {
		for(EchoRunnable t :sharedObject) {
			t.sendMsg(msg);
		}

	}
}
