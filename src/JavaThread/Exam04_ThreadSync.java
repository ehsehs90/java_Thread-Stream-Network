package JavaThread;

/*
 * 2개의 Thread를 파생시켜서 공용객체를 이용하도록 만들어 보아요!
 * Thread가 공용객체를 동기화해서 사용하는경우와 그렇지 않은 경우를
 * 비교해서 이해해 보아요!
 * 
 * 공용객체를 만들기 위한 class를 정의해 보아요!  
 */
class SharedObject {
	private int number; // 공용 객체가 가지는 field

	// getter & setter (Thread에 의해서 사용되요)
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	// Thread에 의해서 사용이 되는 business method **공용객체 메서드(필드제어)**
	// synchronized keyword 로 동기화를 할 수 있어요
	// method동기화는 효율이 좋지 않아요!
	// 메서드의 동기 화- 이만~큼의 코드를 블락@쿠 :: 블락영역이 넓어요- 임계영역이 크다.
	// 일반적으로 메서드 블락이 아닌 동기화 블럭을 사용해요
	// 동기화 block을 이용해서 처리하는게 일반적.

	public synchronized void assignNumber(int number) { // 싱크로나이즈드 @@ 블럭이 끝날때 까지 롹을 갖고있다. -> wait() 이라는 메서드 호출하면 현재 갖고
		 											// 있는 모니터 객체를 탁 놓음
		synchronized (this) { 
			this.number = number;
			try {
				Thread.sleep(3000); // Thread.sleep은 예외처리가 필요해요. try-catch
				System.out.println("현재 공용객체의 number :" + this.number); //
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

//Runnable interface를 class로 구현 (전형적인 모양)
class MyRunnable implements Runnable {
	// 이 러너블 인터페이스에는 shared를 필드로 갖고 있어야해. --아예 runnable객체 만들 때 shared를 인자로 떙긴다.
	// (shared는 인젝션됨)
	SharedObject shared;      // 생성자한테 sharedobject 넘겨요.
	int input;                // 생성자한테 sharedobject 넘겨요. + 숫자도 넘겨요

	public MyRunnable(SharedObject shared, int input) {
		this.shared = shared;
		this.input = input;
	}

	@Override
	public void run() {
		shared.assignNumber(input);
	}
}

public class Exam04_ThreadSync {

	public static void main(String[] args) {

		// 공용객체를 하나 생성해요!
		SharedObject shared = new SharedObject();

		// Thread 생성 (2개) //이 두개의 thread가 위에서 만난 공용객체(shard)를 갖고 있어요! 이것이 바로 쓰레드가 객체를
		// 공유한다는 뜻임
		Thread t1 = new Thread(new MyRunnable(shared, 100));
		Thread t2 = new Thread(new MyRunnable(shared, 200));

		// Thread 실행 (runnable상태로 전환)
		t1.start();
		t2.start();

	}

}
