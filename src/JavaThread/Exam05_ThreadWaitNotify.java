package JavaThread;

//공용객체를 생성하기 위한 class 정의
class MyShared{
	
	//method호출할 때 Thread가 번갈아 가면서 호출하도록 만들고 싶어요!
	public synchronized void printNum() {
		for(int i=0;i<10;i++) {
			try {
				Thread.sleep(1000);                                               //잤다 깨서 러너블 상태가 되요. 스케쥴러에 의해 누가 선택이 될지 사실 몰라 -> 이걸 약간의
									                                              //처리코드를 이용 해 서로 번갈아가며 호출할 수 있도록 순서를 제어해 볼게요. --> notify()
				
				System.out.println(Thread.currentThread().getName()+":"+i);
				notify();                                                         //현재 wait()상태에 있는 Thread를 깨워서 runnable상태로 전환
									                                              //notify() : 현재 wait()상태를 찾아. 없으면 지나가
				 
				wait();				                                              //자기가 가지고 있는 monitor 객체를 놓고
									                                              //스스로 wait block에 들어가요!
							
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}		
	}
}


/*
 * 즉 모니터 객체를 획득하는 keyword - syncronized가 필요!
 * wait() : 자기가 갖고 있는 monitor 객체를 놓고 스스로  wait block 에 들어가요
 * 
 */

//공용객체를 가지고 있는 쓰레드 만들어요

class Exam05_Runnable implements Runnable{
	MyShared obj;
	
	public Exam05_Runnable(MyShared obj) {
		this.obj = obj;
	}

	@Override
	public void run() {	
		obj.printNum();		
	}
	
}

public class Exam05_ThreadWaitNotify {

	public static void main(String[] args) {
		
		//공용객체 만들고
		MyShared shared = new MyShared();
		
		//Thread를 생성하면서 공용객체를 넣어줘요!
		Thread t1 = new Thread(new Exam05_Runnable(shared));
		Thread t2 = new Thread(new Exam05_Runnable(shared));
		t1.setPriority(10);
		t2.setPriority(1);
		t1.start();
		t2.start();
	}
}
