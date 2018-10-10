
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HelloWorldServer {
    private  int port;

    public HelloWorldServer(int port){
        super();
        this.port = port;
    }

    public void run() throws Exception{
        /***
         * NioEventLoopGroup 是用来处理I/O操作的多线程事件循环器，
         * Netty提供了许多不同的EventLoopGroup的实现用来处理不同传输协议。 在这个例子中我们实现了一个服务端的应用，
         * 因此会有2个NioEventLoopGroup会被使用。 第一个经常被叫做‘boss’，用来接收进来的连接。
         * 第二个经常被叫做‘worker’，用来处理已经被接收的连接， 一旦‘boss’接收到连接，就会把连接信息注册到‘worker’上。
         * 如何知道多少个线程已经被使用，如何映射到已经创建的Channels上都需要依赖于EventLoopGroup的实现，
         * 并且可以通过构造函数来配置他们的关系。
         */

        EventLoopGroup bossGroup = new NioEventLoopGroup();//创建父事件循环组
        EventLoopGroup workerGroup = new NioEventLoopGroup();//创建子类的事件循环组
        System.out.println("准备运行端口：" + port);

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            /**
             * group方法接收两个参数， 第一个为父时间循环组，第二个参数为子事件循环组
             */

            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)  //bossGroup的通道，只是负责连接
                    .childHandler(new HelloWorldChannelnitializer()); //workerGroup的处理器，

            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        }
        finally {
            /***
             * 关闭
             */
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();

        }


    }

    public static  void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new HelloWorldServer(port).run();
        System.out.println("server:run()");
    }

}

