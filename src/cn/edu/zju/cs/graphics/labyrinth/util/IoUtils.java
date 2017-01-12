package cn.edu.zju.cs.graphics.labyrinth.util;

import org.lwjgl.BufferUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class IoUtils {

    private IoUtils() {}

    public static ByteBuffer getResourceAsByteBuffer(String resource, int bufferSizeHint)
            throws IOException {
        ByteBuffer buffer;
        URL url = Thread.currentThread().getContextClassLoader().getResource(resource);
        File file = new File(url.getFile());
        if (file.isFile()) {
            try (FileInputStream inputStream = new FileInputStream(file)) {
                try (FileChannel channel = inputStream.getChannel()) {
                    return channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
                }
            }
        } else {
            buffer = BufferUtils.createByteBuffer(bufferSizeHint);
            try (InputStream source = url.openStream()) {
                if (source == null) {
                    throw new FileNotFoundException(resource);
                }
                try (ReadableByteChannel channel = Channels.newChannel(source)) {
                    while (true) {
                        int bytes = channel.read(buffer);
                        if (bytes == -1) {
                            break;
                        }
                        if (buffer.remaining() == 0) {
                            buffer = resizeBuffer(buffer, buffer.capacity() * 2);
                        }
                    }
                    buffer.flip();
                }
            }
        }
        return buffer;
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }
}
