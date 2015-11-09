import com.yly.facedetect.SingletonFaceDetect;

public class TestMain {

	static {
		System.load(TestMain.class.getResource("libopencv_java2411.so").getFile());
	}
	public static void main(String[] args) {
		int res = SingletonFaceDetect.getINSTANCE().detectFace("/home/yang/workspace", "1.jpg", "/home/yang/workspace", "234.jpg");
		System.out.println("test"+res);
		

	}

}
