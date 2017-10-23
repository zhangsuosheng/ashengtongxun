/**
 * 
 */
package ashengtongxun;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

/**
 * ��������ʵ�����봰��Ŀ�w�͸�h�����봰���Dimension������Dimension.width��Ϊ�ؼ���Dimension.height��Ϊ�ؼ��ߣ�
 * ����һ���ܽ�����������Ļ���������
 * ������Ҫ���ø���Ĵ���Ĺ��캯����д��������䣺
 * setLocation(WindowXY.getXY(this.getSize()));
 * �Ӷ�ʵ���д����ʱ������������Ļ����
 * 
 * @author ������
 *
 */
public class WindowXY {
	//���봰�ڵĿ�͸ߣ����������Ӧ����ʾ��λ��
	public static Point getXY(int w,int h){
		Toolkit toolkit=Toolkit.getDefaultToolkit();
		int height=toolkit.getScreenSize().height;
		int width=toolkit.getScreenSize().width;
		
		int x=(width-w)/2;
		int y=(height-h)/2;
		
		Point p=new Point(x,y);
		
		return p;
	}
	
	//�����غ�����������һ�����غ���������д���������Լ򻯴��룡
	public static Point getXY(Dimension d){
		return getXY(d.width,d.height);
	}
	
}
