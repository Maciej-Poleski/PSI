package B;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Hello world!
 */
public class ServerMain {

    public static void main(String[] args) throws IOException {
        final AsynchronousServerSocketChannel listener =
                AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(12345));

        listener.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
            public void completed(AsynchronousSocketChannel ch, Void att) {
                // accept the next connection
                listener.accept(null, this);

                // handle this connection
                HandshakeRequestFromClient hrfc = new HandshakeRequestFromClient(ch);


                ch.read(hrfc.buffer, null, hrfc);
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

    static class WaitForAnswerWrittenAndRestart implements CompletionHandler<Integer, Void> {

        private final AsynchronousSocketChannel channel;
        ByteBuffer buffer;


        public WaitForAnswerWrittenAndRestart(AsynchronousSocketChannel channel, int bufferSize) {
            this.channel = channel;
            buffer = ByteBuffer.allocate(bufferSize);
        }

        @Override
        public void completed(Integer integer, Void aVoid) {
            HandshakeRequestFromClient hrfc = new HandshakeRequestFromClient(channel);

            channel.read(hrfc.buffer, null, hrfc);
        }

        @Override
        public void failed(Throwable throwable, Void aVoid) {
            System.err.println("Wystąpił błąd podczas transmisji:");
            throwable.printStackTrace();
            try {
                channel.close();
            } catch (IOException ignored) {
            }
        }
    }

    static class WaitSendFileAndRestart implements CompletionHandler<Integer, Void> {
        private final AsynchronousSocketChannel channel;
        private final File fileToSend;
        ByteBuffer buffer;

        public WaitSendFileAndRestart(AsynchronousSocketChannel channel, File fileToSend) {
            //To change body of created methods use File | Settings | File Templates.
            this.channel = channel;
            this.fileToSend = fileToSend;
            buffer = ByteBuffer.allocate(20);
        }

        @Override
        public void completed(Integer integer, Void aVoid) {
            try {
                FileChannel fileChannel = FileChannel.open(Paths.get(fileToSend.toURI()), StandardOpenOption.READ);
                MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
                fileChannel.close();
                channel.write(mappedByteBuffer, null, new WaitForAnswerWrittenAndRestart(channel, 0));
            } catch (IOException e) {
                System.err.println("Wystąpił nieoczekiwany wyjątek, protokół nie daje możliwości sygnalizacji.\n Połączenie przerwane.");
            }
        }

        @Override
        public void failed(Throwable throwable, Void aVoid) {
            // give up
            try {
                channel.close();
            } catch (IOException ignored) {
            }
        }
    }

    static class HandshakeRequestFromClient implements CompletionHandler<Integer, Void> {
        private final AsynchronousSocketChannel channel;
        ByteBuffer buffer = ByteBuffer.allocate(2048);

        public HandshakeRequestFromClient(AsynchronousSocketChannel ch) {

            channel = ch;
        }

        @Override
        public void completed(Integer integer, Void o) {
            buffer.flip();
            Charset charset = Charset.forName("UTF-8");
            CharsetDecoder decoder = charset.newDecoder();
            try {
                CharBuffer charBuffer = decoder.decode(buffer);
                String command = charBuffer.toString();
                if (command.startsWith("List")) {
                    dispatchListCommand();
                } else if (command.startsWith("Exit")) {
                    dispatchExitCommand();
                } else if (command.startsWith("Get;")) {
                    dispatchGetCommand(command.substring(4));
                } else {
                    System.err.println("Nie rozpoznane polecenie: " + command);
                    channel.close();
                }
            } catch (CharacterCodingException e) {
                System.err.println("Odebrana wiadomość ma złe kodowanie (nie UTF-8)");
            } catch (IOException ignored) {
            }
        }

        private void dispatchGetCommand(String param) {
            File fileToSend = new File(param.trim());
            if (!fileToSend.isFile()) {
                WaitForAnswerWrittenAndRestart reply = new WaitForAnswerWrittenAndRestart(channel, 50);
                try {
                    reply.buffer.put("NO\n".getBytes("UTF-8"));
                    reply.buffer.flip();
                    channel.write(reply.buffer, null, reply);
                } catch (UnsupportedEncodingException e) {
                    // assert(false)
                }
            } else {
                try {
                    WaitSendFileAndRestart reply = new WaitSendFileAndRestart(channel, fileToSend);
                    reply.buffer.put("OK;".getBytes("UTF-8"));
                    reply.buffer.put((Long.toString(fileToSend.length()) + "\n").getBytes("UTF-8"));
                    reply.buffer.flip();
                    channel.write(reply.buffer, null, reply);
                } catch (IOException e) {
                    System.err.println("Wystąpił nieoczekiwany wyjątek, protokół nie daje możliwości sygnalizacji.\n Połączenie przerwane.");
                }

            }
        }

        private void dispatchExitCommand() {
            try {
                channel.close();
            } catch (IOException ignored) {
            }
        }

        private void dispatchListCommand() {
            File cwd = new File(".");
            File[] files = cwd.listFiles();
            StringBuilder builder = new StringBuilder();
            for (File file : files != null ? files : new File[0]) {
                if (file.isFile()) {
                    builder.append(file.getName()).append(" ");
                }
            }
            builder.append('\n');
            try {
                byte[] rawReply = builder.toString().getBytes("UTF-8");
                WaitForAnswerWrittenAndRestart reply = new WaitForAnswerWrittenAndRestart(channel, rawReply.length + 10);
                reply.buffer.put(rawReply);
                reply.buffer.flip();
                channel.write(reply.buffer, null, reply);
            } catch (UnsupportedEncodingException e) {
                // assert(false)
            }
        }

        @Override
        public void failed(Throwable throwable, Void o) {
            // give up
            try {
                channel.close();
            } catch (IOException ignored) {
            }
        }
    }
}
