package model;
/**
 * ��װ�õ�һ��UDP��Ϣ
 * @author ������
 *
 */
public class Message {
	String type;//��Ϣ���regע����Ϣ  msg������Ϣ  qrȷ����Ϣ
	String myUID;
	String toUID;
	String msg;
	String code;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMyUID() {
		return myUID;
	}
	public void setMyUID(String myUID) {
		this.myUID = myUID;
	}
	public String getToUID() {
		return toUID;
	}
	public void setToUID(String toUID) {
		this.toUID = toUID;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
