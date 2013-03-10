package B;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Hello world!
 */
public class ServerMain {

    public static void main(String[] args) throws IOException {
        final AsynchronousServerSocketChannel listener =
                AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(5000));

        listener.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
            public void completed(AsynchronousSocketChannel ch, Void att) {
                // accept the next connection
                listener.accept(null, this);

                // handle this connection
                ch.read(ByteBuffer.allocate(1), null, new);
            }

            public void failed(Throwable exc, Void att) {
            }
        });
        try {
            for (; ; )
                Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }

    static class HandshakeRequestFromClient implements CompletionHandler<Integer, Void {
        @Override
        public void completed(Integer integer, Void o) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void failed(Throwable throwable, Void o) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    public static void main(String[] args) throws IOException {
        final AsynchronousServerSocketChannel listener =
                AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(5000));

        listener.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
            public void completed(AsynchronousSocketChannel ch, Void att) {
                // accept the next connection
                listener.accept(null, this);

                // handle this connection
                ch.read(ByteBuffer.allocate(1), null, new HandshakeRequestFromClient());
            }

            public void failed(Throwable exc, Void att) {
            }
        });
        try {
            for (; ; )
                Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }
}
