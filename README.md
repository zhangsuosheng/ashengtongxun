java即时聊天工具

ashengtongxun目录是项目的客户端部分
ashengtongxun_server目录是项目的服务器端部分

如果要部署，需要先在ashengtongxun/src/view/util/Config.java下配置服务器的IP和端口号
并在ashengtongxun_server/src/db/DBManager.java下配置数据库的url，用户名和密码

实现功能：
登陆，注册，发送/接收消息（尚未做服务器端存储，需要双方都在线方能使用），未读消息提示

待实现功能：
搜索/添加好友，离线消息接收，发送/接收文件