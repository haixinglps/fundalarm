package cn.exrick.manager.service.util;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RtmpAmf0Parser {

	// AMF0数据类型标识
	private static final byte AMF0_NUMBER = 0x00;
	private static final byte AMF0_BOOLEAN = 0x01;
	private static final byte AMF0_STRING = 0x02;
	private static final byte AMF0_OBJECT = 0x03;
	private static final byte AMF0_NULL = 0x05;
	private static final byte AMF0_ECMA_ARRAY = 0x08;
	private static final byte AMF0_OBJECT_END = 0x09;
	private static final byte AMF0_STRICT_ARRAY = 0x0A;

	public static void parsePlayCommand(ByteBuffer buffer, Map<String, Object> result) {

		// 4. 解析流名称
		String streamName = readAmf0String(buffer);
//		result.put("streamName", streamName);

		// 5. 解析可选参数对象
		if (buffer.hasRemaining()) {
			if (buffer.get() == AMF0_OBJECT) {
				result.put("parameters", parseAmf0Object(buffer));
			}
		}

		result.put("streamName", streamName);
	}

	private static Map<String, Object> parseAmf0Object(ByteBuffer buffer) {
		Map<String, Object> obj = new HashMap<>();
		while (buffer.hasRemaining()) {
			String key = readAmf0String(buffer);
			if (key.isEmpty() && buffer.get() == 0x09) { // OBJECT_END
				break;
			}
			byte type = buffer.get();
			switch (type) {
			case AMF0_NUMBER:
				obj.put(key, readAmf0Number(buffer));
				break;
			case AMF0_STRING:
				obj.put(key, readAmf0String(buffer));
				break;
			default:
				throw new IllegalArgumentException("Unsupported AMF0 type: " + type);
			}
		}
		return obj;
	}

	private static int getMessageHeaderLength(int chunkType) {
		switch (chunkType) {
		case 0:
			return 11; // 完整消息头
		case 1:
			return 7; // 省略流ID
		case 2:
			return 3; // 仅时间戳增量
		default:
			return 0; // 类型3无消息头
		}

	}

	public static Map<String, Object> parseAmf0Command(byte[] data) {
		ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN);

		// 解析基本头
		byte basicHeader = buffer.get();
		// System.out.println("Hex: " + Integer.toHexString(basicHeader & 0xFF)); //
		// 输出十六进制77
		int chunkType = (basicHeader & 0xC0) >> 6; // 获取块类型(0-3)
		// System.out.println("type:" + chunkType);
		int chunkStreamId = basicHeader & 0x3F; // 获取流ID

		// 解析消息头(根据块类型确定长度)
		int messageHeaderLength = getMessageHeaderLength(chunkType);
		byte[] messageHeader = new byte[messageHeaderLength];
		// System.out.println("length:" + messageHeaderLength);
		buffer.get(messageHeader);

		// 处理扩展时间戳(类型3块无消息头)
		if (chunkType != 3 && (buffer.getInt(4) == 0xFFFFFF)) {
			// System.out.println("+4");
			buffer.position(buffer.position() + 4); // 跳过扩展时间戳
		}

//		// 剩余部分即为消息体
//		byte[] body = new byte[buffer.remaining()];
//		

		Map<String, Object> result = new HashMap<>();
//		buffer.get(body); // 将剩余数据复制到字节数组

		// 解析命令名称
		try {
			String commandName = null;
			try {
				commandName = readAmf0String(buffer);
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (commandName == null)
				return null;
			// System.out.println("commandname:" + commandName);
			result.put("command", commandName);

			// 解析事务ID
			double transactionId = readAmf0Number(buffer);
			result.put("transactionId", transactionId);
			// System.out.println("transid:" + transactionId);

			// 根据命令类型解析参数
			switch (commandName) {
			case "connect":
				parseConnectParams(buffer, result);
				break;
			case "play":
				buffer.get();
				parsePlayCommand(buffer, result);
			case "publish":
				// parseStreamParams(buffer, result);
				break;
			default:
				// parseGenericParams(buffer, result);
			}
			return result;

		} catch (Exception e) {
			return null;
			// TODO: handle exception
		}

	}

	// 读取布尔类型(0x01)
	public static boolean readBoolean(ByteBuffer buffer) {
		byte type = buffer.get();
		if (type != 0x01)
			throw new IllegalArgumentException("Invalid AMF0 boolean marker");
		return buffer.get() != 0;
	}

	private static void parseConnectParams(ByteBuffer buffer, Map<String, Object> result) {
		// 解析connect命令的Object参数
		if (buffer.get() == AMF0_OBJECT) {
			while (buffer.remaining() > 3) { // 至少需要3字节(0x000009)
				// System.out.println("读取key");
				String key = readAmf0String2(buffer);
				// System.out.println("key:" + key);

				// System.out.println("读取valuetype");

				byte valueType = buffer.get(buffer.position());

				Object value = null;
				switch (valueType) {
				case 0x00:
					// System.out.println("00");
					value = readAmf0Number(buffer);
					break;
				case 0x01:
					// System.out.println("01");
					value = readBoolean(buffer);
					break;
				case 0x02:
					// System.out.println("02");
					value = readAmf0String(buffer);
					break;
//		                case 0x03 : parseNestedObject(buffer);
//		                default -> throw new IllegalArgumentException("Unsupported AMF0 type: " + valueType);
				}
				;

				result.put(key, value);
				// System.out.println("value:" + value);

				// 检查对象结束标记(0x000009)
				if (buffer.remaining() >= 3) {
					byte[] endMarker = new byte[3];
					buffer.mark();
					buffer.get(endMarker);
					if (endMarker[0] == 0x00 && endMarker[1] == 0x00 && endMarker[2] == 0x09) {
						break;
					}
					buffer.reset();
				}
			}
		}
	}

	private static Object readAmf0Value(ByteBuffer buffer, byte type) {
		switch (type) {
		case AMF0_NUMBER:
			return readAmf0Number(buffer);
		case AMF0_BOOLEAN:
			return readAmf0Boolean(buffer);
		case AMF0_STRING:
			return readAmf0String(buffer);
		case AMF0_OBJECT:
			return readAmf0Object(buffer);
		case AMF0_NULL:
			buffer.get();
			return null;
		case AMF0_ECMA_ARRAY:
			return readAmf0EcmaArray(buffer);
		case AMF0_STRICT_ARRAY:
			return readAmf0StrictArray(buffer);
		default:
			throw new RuntimeException("Unsupported AMF0 type: " + type);
		}
	}

	private static String readAmf0String(ByteBuffer buffer) {
//		byte type = buffer.get();

		// 检查至少3字节(类型+长度)
		if (buffer.remaining() < 3) {
			throw new BufferUnderflowException();
		}

		byte type = buffer.get();
		// System.out.println("字符串类型Hex: " + Integer.toHexString(type & 0xFF)); //
		// 输出十六进制77
		if (type != 0x02) {
			throw new IllegalArgumentException("Invalid AMF0 string marker");
		}

		// 获取长度并检查剩余数据
		int length = buffer.getShort() & 0xFFFF;
		if (buffer.remaining() < length) {
			throw new BufferUnderflowException();
		}

		byte[] bytes = new byte[length];
		buffer.get(bytes);
		return new String(bytes, StandardCharsets.UTF_8);

	}

	private static String readAmf0String2(ByteBuffer buffer) {
//		byte type = buffer.get();
//
//		// 检查至少3字节(类型+长度)
//		if (buffer.remaining() < 3) {
//			throw new BufferUnderflowException();
//		}
//
//		byte type = buffer.get();
//		//System.out.println("字符串类型Hex: " + Integer.toHexString(type & 0xFF)); // 输出十六进制77
//		if (type != 0x02) {
//			throw new IllegalArgumentException("Invalid AMF0 string marker");
//		}

		// 打印缓冲区前4字节内容

//		buffer.rewind();

//		short xx = buffer.getShort();

		byte tt = buffer.get(); // 读取一个字节
		String hexStra = Integer.toHexString(tt & 0xFFFF);
		// System.out.println("hexstr:" + hexStra);
		byte next = buffer.get(); // 读取一个字节
		int length = next & 0xFF; // 转换为无符号整数
		if (length > 15) {
			next = buffer.get();
			length = next & 0xFF;
		}

		// 获取长度并检查剩余数据
//		int length = xx & 0xFFFF;
		String hexStr = Integer.toHexString(next & 0xFFFF);
		// System.out.println("hexstr:" + hexStr);

		// System.out.println("length:" + length);
		// System.out.println("remain:" + buffer.remaining());
//		if (buffer.remaining() < length) {
//			throw new BufferUnderflowException();
//		}

		byte[] bytes = new byte[length];
		buffer.get(bytes);
		return new String(bytes, StandardCharsets.UTF_8);

	}

	private static double readAmf0Number(ByteBuffer buffer) {
		byte type = buffer.get();

		return buffer.getDouble();
	}

	private static boolean readAmf0Boolean(ByteBuffer buffer) {
		byte type = buffer.get();

		return buffer.get() == 1;
	}

	private static Map<String, Object> readAmf0Object(ByteBuffer buffer) {
		Map<String, Object> obj = new HashMap<>();
		while (true) {
			String key = readAmf0String(buffer);
			if (key.isEmpty() && buffer.get() == AMF0_OBJECT_END) {
				break;
			}
			byte type = buffer.get();
			buffer.position(buffer.position() - 1);
			obj.put(key, readAmf0Value(buffer, type));
		}
		return obj;
	}

	private static List<Object> readAmf0StrictArray(ByteBuffer buffer) {
		buffer.get(); // 跳过类型标识
		int count = buffer.getInt();
		List<Object> array = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			byte type = buffer.get();
			array.add(readAmf0Value(buffer, type));
		}
		return array;
	}

	private static Map<String, Object> readAmf0EcmaArray(ByteBuffer buffer) {
		buffer.get(); // 跳过类型标识
		buffer.getInt(); // 跳过数组长度
		return readAmf0Object(buffer);
	}
}
