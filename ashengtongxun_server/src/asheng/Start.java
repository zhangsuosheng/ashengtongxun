package asheng;

import java.io.IOException;

public class Start {
	public static void main(String[] args){
		new Thread(){
			public void run(){
				try {
					LoginServer.openServer();
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}
		}.start();
		
		new Thread(){
			public void run(){
				try {
					RegServer.openServer();
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}
		}.start();
		
		new Thread(){
			public void run(){
				try {
					UDPMessageServer.openServer();
				} catch (IOException e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
			}
		}.start();
	}
}
