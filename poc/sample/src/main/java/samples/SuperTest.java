package samples;

class Parent {
	
	Parent() {
		System.out.println("Parent Constructor!");
	}

	Parent(String msg) {
		System.out.println("Parent Constructor with Message : " + msg);
	}
}

public class SuperTest extends Parent {

	SuperTest() {
		super("Hello");
		System.out.println("SuperTest Constructor!");
	}
	
	public static void main(String[] args) {
		SuperTest st = new SuperTest();	
	}

}
