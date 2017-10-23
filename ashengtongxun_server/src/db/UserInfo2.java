package db;
/**
 * 好友列表中所需的好友信息
 * uid 用户id
 * netname 用户昵称
 * sign 个性签名
 * img 用户头像
 * @author acer
 *
 */
public class UserInfo2 {
	
	private String uid;
	private String netname;
	private String sign;
	private String img;
	
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getNetname() {
		return netname;
	}
	public void setNetname(String netname) {
		this.netname = netname;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	
}
