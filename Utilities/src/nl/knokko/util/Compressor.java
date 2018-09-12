package nl.knokko.util;

import java.util.Arrays;

import nl.knokko.util.bits.BitInput;
import nl.knokko.util.bits.BitOutput;

public final class Compressor {
	
	private static final byte ID_DONE = -128;
	private static final byte ID_FILL_BYTE_B = -127;
	private static final byte ID_FILL_BYTE_C = -126;
	private static final byte ID_FILL_CHAR_B = -125;
	private static final byte ID_FILL_CHAR_C = -124;
	
	public static byte[] compress(byte[] original){
		byte[] newBytes = new byte[original.length / 2];
		byte[] preBytes = new byte[0];
		int writeIndex = 0;
		int readIndex = 0;
		while(readIndex < original.length){
			byte b = original[readIndex];
			newBytes = add(newBytes, writeIndex, b);
			writeIndex++;
			int length = 1;
			while(length + readIndex < original.length){
				if(original[length + readIndex] != b)
					break;
				length++;
			}
			if(length > 1){
				if(readIndex < 256 && length > 4 && length <= 260){
					preBytes = add(preBytes, ID_FILL_BYTE_B, (byte)(readIndex - 128), (byte)(length - 133));
					readIndex += length;
				}
				else if(readIndex <= Character.MAX_VALUE && length > 5 && length <= 261){
					char l = (char) readIndex;
					preBytes = add(preBytes, ID_FILL_BYTE_C, BitOutput.char0(l), BitOutput.char1(l), (byte)(length - 134));
					readIndex += length;
				}
				else if(readIndex < 256 && length <= Character.MAX_VALUE + 6 && length > 5){
					char l = (char) (length - 6);
					preBytes = add(preBytes, ID_FILL_CHAR_B, (byte)(readIndex - 128), BitOutput.char0(l), BitOutput.char1(l));
					readIndex += length;
				}
				else if(readIndex <= Character.MAX_VALUE && length > 6 && length <= Character.MAX_VALUE + 7){
					char i = (char) readIndex;
					char c = (char) (length - 7);
					preBytes = add(preBytes, ID_FILL_CHAR_C, BitOutput.char0(i), BitOutput.char1(i), BitOutput.char0(c), BitOutput.char1(c));
					readIndex += length;
				}
				else
					readIndex++;
			}
			else
				readIndex++;
		}
		preBytes = add(preBytes, ID_DONE);
		byte[] returnBytes = new byte[writeIndex + preBytes.length];
		System.arraycopy(preBytes, 0, returnBytes, 0, preBytes.length);
		System.arraycopy(newBytes, 0, returnBytes, preBytes.length, writeIndex);
		return returnBytes;
	}
	
	public static byte[] decompress(byte[] compressed){
		int preIndex = 0;
		int startIndex = 0;
		while(preIndex < compressed.length){
			if(compressed[preIndex] == ID_FILL_BYTE_B)
				preIndex += 3;
			else if(compressed[preIndex] == ID_FILL_BYTE_C)
				preIndex += 4;
			else if(compressed[preIndex] == ID_FILL_CHAR_B)
				preIndex += 4;
			else if(compressed[preIndex] == ID_FILL_CHAR_C)
				preIndex += 5;
			else if(compressed[preIndex] == ID_DONE){
				startIndex = preIndex + 1;
				break;
			}
			else
				throw new IllegalArgumentException("Unknown function type: " + compressed[preIndex]);
		}
		if(startIndex == 0)
			throw new IllegalArgumentException("Can't find start index!");
		byte[] original = Arrays.copyOfRange(compressed, startIndex, compressed.length);
		preIndex = 0;
		while(preIndex < startIndex - 1){
			if(compressed[preIndex] == ID_FILL_BYTE_B){
				int index = compressed[preIndex + 1] + 128;
				int length = compressed[preIndex + 2] + 133;
				byte[] newOriginal = new byte[original.length + length - 1];
				System.arraycopy(original, 0, newOriginal, 0, index);
				for(int i = 0; i < length; i++)
					newOriginal[i + index] = original[index];
				System.arraycopy(original, index + 1, newOriginal, index + length, original.length - index - 1);
				original = newOriginal;
				preIndex += 3;
			}
			else if(compressed[preIndex] == ID_FILL_BYTE_C){
				char index = BitInput.makeChar(compressed[preIndex + 1], compressed[preIndex + 2]);
				int length = compressed[preIndex + 3] + 134;
				byte[] newOriginal = new byte[original.length + length - 1];
				System.arraycopy(original, 0, newOriginal, 0, index);
				for(int i = 0; i < length; i++)
					newOriginal[i + index] = original[index];
				System.arraycopy(original, index + 1, newOriginal, index + length, original.length - index - 1);
				original = newOriginal;
				preIndex += 4;
			}
			else if(compressed[preIndex] == ID_FILL_CHAR_B){
				int index = compressed[preIndex + 1] + 128;
				int length = BitInput.makeChar(compressed[preIndex + 2], compressed[preIndex + 3]) + 6;
				byte[] newOriginal = new byte[original.length + length - 1];
				System.arraycopy(original, 0, newOriginal, 0, index);
				for(int i = 0; i < length; i++)
					newOriginal[i + index] = original[index];
				System.arraycopy(original, index + 1, newOriginal, index + length, original.length - index - 1);
				original = newOriginal;
				preIndex += 4;
			}
			else if(compressed[preIndex] == ID_FILL_CHAR_C){
				char index = BitInput.makeChar(compressed[preIndex + 1], compressed[preIndex + 2]);
				int length = BitInput.makeChar(compressed[preIndex + 3], compressed[preIndex + 4]) + 7;
				byte[] newOriginal = new byte[original.length + length - 1];
				System.arraycopy(original, 0, newOriginal, 0, index);
				for(int i = 0; i < length; i++)
					newOriginal[i + index] = original[index];
				System.arraycopy(original, index + 1, newOriginal, index + length, original.length - index - 1);
				original = newOriginal;
				preIndex += 5;
			}
			else
				throw new IllegalArgumentException("Unknown function type: " + compressed[preIndex]);
		}
		return original;
	}
	
	private static byte[] add(byte[] bytes, byte... values){
		bytes = Arrays.copyOf(bytes, bytes.length + values.length);
		System.arraycopy(values, 0, bytes, bytes.length - values.length, values.length);
		return bytes;
	}
	
	private static byte[] add(byte[] bytes, int index, byte value){
		if(index >= bytes.length)
			bytes = Arrays.copyOf(bytes, index + 1);
		bytes[index] = value;
		return bytes;
	}
}
