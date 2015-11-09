package com.yly.facedetect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;

public class SingletonFaceDetect{
	//static  fields 
	private  static final String xmlHaarcascadeName = "haarcascade_frontalface_alt.xml";
	private static  final String xmlYLYHaarName = "ylyHaar.xml";
	private static CascadeClassifier faceDetector = null;
	private static boolean isloadHaarcascade_frontalface_alt = false;
	
	public static final int SUCCESS = 0;
	public static final int ERROR_LOADLIBRARY_INVALID = 1;
	public static final int ERROR_HAARCASCADEXML_INVALID = 2;
	public static final int ERROR_SOURCEIMAGE_INVALID = 3;
	public static final int ERROR_NOFACE_DETECTED =4;
	public static final int ERROR_MORE_THAN_ONE_FACE_DETECTED = 5;
	public static final int BUFFER_SIZE = 10000;
	
	private  SingletonFaceDetect() {
		//System.out.println(xmlFile);
		
		File file  = new File(xmlYLYHaarName);
		if(false==file.exists()){
			InputStream is =  SingletonFaceDetect.class.getResourceAsStream(xmlHaarcascadeName);
			byte[] buf = new byte[BUFFER_SIZE];
			OutputStream out = null;
			try {
				out = new FileOutputStream(xmlYLYHaarName);
				int count_read = 0;
				while( ( count_read = is.read(buf) )> 0){
					out.write(buf, 0, count_read);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					is.close();
					out.flush();
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}		
		faceDetector = new CascadeClassifier(xmlYLYHaarName);
		
		if(false==faceDetector.empty()){
			System.out.println("xml loaded");
			isloadHaarcascade_frontalface_alt = true;
		}else{
			System.out.println("xml unloaded");
			isloadHaarcascade_frontalface_alt = false;
		}
	}
	
	// staic inner class for singleton
	private static class SingletonFaceDetectorHolder{
		private static final SingletonFaceDetect  INSTANCE = new SingletonFaceDetect();
	}
	
	//Singleton method for the class
	public  static final  SingletonFaceDetect getINSTANCE(){
//		xmlFile=xmlfile;
		return SingletonFaceDetectorHolder.INSTANCE;
	}
	
	/*
	 * 输入   文件URI  , 生成图像保存的文件夹 URI	
	 * 返回  处理信息
	 */
	public int  detectFace(String sourceFolder,  String imageName,  String saveFolder, String saveName){
		
		if(Core.VERSION.isEmpty()){
			return ERROR_LOADLIBRARY_INVALID;
		}

		
		if(false==isloadHaarcascade_frontalface_alt ){
			return ERROR_HAARCASCADEXML_INVALID;
		}
		
		Mat image  = Highgui.imread(sourceFolder+"/"+imageName);
		if(true == image.empty()){
//			System.out.println("***********");
			return ERROR_SOURCEIMAGE_INVALID;
		}
		
		MatOfRect faceDetections = new  MatOfRect();

		faceDetector.detectMultiScale(image, faceDetections);
		
		if(faceDetections.empty()){
			return ERROR_NOFACE_DETECTED;
		}
		
		if(1 != faceDetections.total()){
			return ERROR_MORE_THAN_ONE_FACE_DETECTED;
		}
		Highgui.imwrite( sourceFolder+"/"+saveName , new Mat(image,faceDetections.toList().get(0)));
		return SUCCESS;
	}
}



