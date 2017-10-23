package model;

import java.util.HashMap;
import java.util.LinkedList;

import net.sf.json.JSONObject;
import view.ChatWindow;
import view.FaceJPanel;
import view.util.Config;

public class MessagePool {
	
	private MessagePool(){}
	
	private static MessagePool messagePool=new MessagePool();
//	public static HashMap<String,LinkedList<Message>> hashMap=new HashMap();
	
	public static MessagePool getMessagePool(){
		return messagePool;
	}
	
	public void addMessage(String jsonstr){
		JSONObject json=JSONObject.fromObject(jsonstr);
		String type=json.getString("type");
		String code=json.getString("code");
		String msg=json.getString("msg");
		String myUID=json.getString("myUID");
		String toUID=json.getString("toUID");
		
		Message message=new Message();
		message.setType(type);
		message.setCode(code);
		message.setMsg(msg);
		message.setMyUID(myUID);
		message.setToUID(toUID);
		
		ChatWindow chatWindow=Config.chatWindowTable.get(myUID);
		
		if(chatWindow==null||chatWindow.isVisible()==false){
			FaceJPanel faceJPanel=Config.hashtable.get(myUID);
			faceJPanel.addMessage(message);
			System.out.println("add into hashtable");
		}else{
			chatWindow.addMessage(message);
			System.out.println("add directly");
		}
		
//		LinkedList list=hashMap.get(myUID);
//		if(list==null){
//			list=new LinkedList();
//			list.add(message);
//			hashMap.put(myUID, list);
//		}else{
//			list.add(message);
//			hashMap.put(myUID, list);
//		}
		
	}
}
