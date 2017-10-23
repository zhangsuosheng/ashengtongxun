package asheng;

import java.io.IOException;

public class Start {
	public static void main(String[] args){
		new Thread(){
			public void run(){
				try {
					LoginServer.openServer();
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}.start();
		
		new Thread(){
			public void run(){
				try {
					RegServer.openServer();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}.start();
		
		new Thread(){
			public void run(){
				try {
					UDPMessageServer.openServer();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}.start();
	}
}
