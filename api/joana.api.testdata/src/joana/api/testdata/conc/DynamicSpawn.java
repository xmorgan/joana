/**
 * This file is part of the Joana IFC project. It is developed at the
 * Programming Paradigms Group of the Karlsruhe Institute of Technology.
 *
 * For further details on licensing please read the information at
 * http://joana.ipd.kit.edu or contact the authors.
 */
package joana.api.testdata.conc;

/**
 * @author Simon Bischof <simon.bischof@kit.edu>
 */
public class DynamicSpawn {
	static class Thread1 extends Thread {
		public void run() {
			System.out.println("1");
		}
	}
	
	static class Thread2 extends Thread {
		public void run() {
			System.out.println("2");
		}
	}
	
	static class Thread3 extends Thread {
		public void run() {
			new Thread4().start();
			System.out.println("3");
		}
	}
	
	static class Thread4 extends Thread {
		public void run() {
			System.out.println("4");
		}
	}
	
	static int x;
	
	public static void main(String[] args) throws InterruptedException {
		Thread1 t1 = new Thread1();
		Thread2 t2;
		Thread3 t3;

		while (x > 1) {
			t1 = new Thread1();
			t1.start();
			x--;
		}
		t1.join();
		
		if (x * x > 10) {
			while (x > 1) {
				t2 = new Thread2();
				t2.start();
				x--;
			}
		} else {
			while (x > 1) {
				t3 = new Thread3();
				t3.start();
				x--;
			}
		}
	}
}
