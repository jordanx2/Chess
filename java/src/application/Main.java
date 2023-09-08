package application;

public class Main{
	public static void chess()
	{
		String[] a = {"MAIN"};
        processing.core.PApplet.runSketch( a, new Chess());
    }

    public static void main(String[] args) {
        chess();
    }
}