package org.example.shch.kafka.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * 编码
 *
 * @author shichao
 * @since 1.0.0
 * 2021/4/11 14:18
 */
public class CodeUtils {
    private static final Logger logger = LoggerFactory.getLogger(CodeUtils.class);

    private CodeUtils() {
    }

    public static void main(String[] args) {
        ByteBuffer buffer300 = ByteBuffer.allocate(4);
        varIntsEncode(300, buffer300);
        logger.info("300 varint encode:{}", getByteBuffer(buffer300));
        ByteBuffer buffer65 = ByteBuffer.allocate(4);
        zigZagEncode(65, buffer65);
        logger.info("65 zigzag encode:{}.", getByteBuffer(buffer65));
        int value = zigZagDecode(buffer65);
        logger.info("After decode 65:{}.", value);
    }

    /**
     * varints 编码
     *
     * @param value  待编码的值(int32位)
     * @param buffer 字节buffer
     */
    public static void varIntsEncode(int value, ByteBuffer buffer) {
        while ((value & 0xFFFFFF80) != 0L) {
            byte b = (byte) ((value & 0x7F) | 0x80);
            buffer.put(b);
            value >>>= 7;
        }
        buffer.put((byte) value);
    }

    public static void zigZagEncode(int value, ByteBuffer buffer) {
        int v = (value << 1) ^ (value >> 31);
        while ((v & 0xFFFFFF80) != 0L) {
            byte b = (byte) ((v & 0x7F) | 0x80);
            buffer.put(b);
            v >>>= 7;
        }
        buffer.put((byte) v);
    }

    public static int zigZagDecode(ByteBuffer byteBuffer) {
        int value = 0;
        int i = 0;
        int b;
        int count = 0;
        while (((b = byteBuffer.get(count)) & 0x80) != 0) {
            count++;
            value |= (b & 0x7F) << i;
            i += 7;
            if (i > 28) {
                throw new IllegalArgumentException("---:" + value);
            }
            if (count == byteBuffer.position()) {
                break;
            }
        }
        value |= b << i;
        return (value >>> 1) ^ -(value & 1);
    }

    /**
     * 返回int类型的数的 varints 编码的位数
     *
     * @param value 值
     * @return 返回位数
     */
    public static int intValueSizeOfVarints(int value) {
        int bytes = 1;
        while ((value & 0xFFFFFF80) != 0L) {
            bytes++;
            value >>>= 7;
        }
        return bytes;
    }

    public static int intValueSizeOfZigZag(int value) {
        int v = (value << 1) ^ (value >> 31);
        int bytes = 1;
        while ((v & 0xFFFFFF80) != 0L) {
            bytes++;
            v >>>= 7;
        }
        return bytes;
    }

    /**
     * 将byte buffer对象转换为01码
     *
     * @param buffer byteBuffer 对象
     * @return 返回 0/1码
     */
    public static String getByteBuffer(ByteBuffer buffer) {
        StringBuilder strBuffer = new StringBuilder();
        for (int i = 0; i < buffer.position(); i++) {
            byte curByte = buffer.get(i);
            // curByte & 0xFF 是为了做 补码到源码 的转换
            // +0x100 是为了补全小于256的数前面的0
            String curStr = Integer.toBinaryString((curByte & 0xFF) + 0x100);
            strBuffer.append(curStr.substring(1));
        }
        return strBuffer.toString();
    }
}
