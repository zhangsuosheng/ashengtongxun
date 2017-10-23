/**
 * 
 */
package ashengtongxun;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

/**
 * 该类用于实现输入窗体的宽w和高h或输入窗体的Dimension参数（Dimension.width即为控件宽，Dimension.height即为控件高）
 * 返回一个能将窗体置于屏幕中央的坐标
 * 再在需要调用该类的窗体的构造函数中写入如下语句：
 * setLocation(WindowXY.getXY(this.getSize()));
 * 从而实现中窗体打开时将窗体置于屏幕中央
 * 
 * @author 张所晟
 *
 */
public class WindowXY {
	//输入窗口的宽和高，计算出窗体应当显示的位置
	public static Point getXY(int w,int h){
		Toolkit toolkit=Toolkit.getDefaultToolkit();
		int height=toolkit.getScreenSize().height;
		int width=toolkit.getScreenSize().width;
		
		int x=(width-w)/2;
		int y=(height-h)/2;
		
		Point p=new Point(x,y);
		
		return p;
	}
	
	//该重载函数调用了另一个重载函数，这种写法往往可以简化代码！
	public static Point getXY(Dimension d){
		return getXY(d.width,d.height);
	}
	
}
