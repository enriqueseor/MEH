package dsdecmp;

import java.io.EOFException;
import java.io.IOException;

public class Compression {

	public static int[] Decompress(HexInputStream his) throws Exception {
		switch (his.readU8()) {
			case 0x10:
				return Decompress10LZ(his);
			case 0x11:
				return Decompress11LZ(his);
			case 0x24:
			case 0x28:
				return DecompressHuff(his);
			case 0x30:
				return DecompressRLE(his);
			default:
				return null;
		}
	}

	private static int getLength(HexInputStream his) throws IOException {
		int length = 0;
		for (int i = 0; i < 3; i++)
			length = length | (his.readU8() << (i * 8));
		if (length == 0) // 0 length? then length is next 4 bytes
			length = his.readlS32();
		return length;
	}

	private static int[] Decompress10LZ(HexInputStream his) throws Exception {
		int[] outData = new int[getLength(his)];

		int curr_size = 0;
		int flags;
		boolean flag;
		int disp, n, b, cdest;

		while (curr_size < outData.length) {
			try {
				flags = his.readU8();
			} catch (EOFException ex) {
				break;
			}
			for (int i = 0; i < 8; i++) {
				flag = (flags & (0x80 >> i)) > 0;
				if (flag) {
					try {
						b = his.readU8();
					} catch (EOFException ex) {
						throw new Exception("Incomplete data");
					}
					n = b >> 4;
					disp = (b & 0x0F) << 8;
					try {
						disp |= his.readU8();
					} catch (EOFException ex) {
						throw new Exception("Incomplete data");
					}
					n += 3;
					cdest = curr_size;
					if (disp > curr_size)
						throw new Exception("Cannot go back more than already written");
					for (int j = 0; j < n; j++)
						outData[curr_size++] = outData[cdest - disp - 1 + j];
                }
				else {
					try {
						b = his.readU8();
					} catch (EOFException ex) {
						break;
					}
					try {
						outData[curr_size++] = b;
					} catch (ArrayIndexOutOfBoundsException ex) {
						if (b == 0)
							break;
					}
                }
                if (curr_size > outData.length)
                    break;
            }
		}
		return outData;
	}

	private static int[] Decompress11LZ(HexInputStream his) throws Exception {
		int[] outData = new int[getLength(his)];
		int curr_size = 0;
		int flags;
		boolean flag;
		int b1, bt, b2, b3, len, disp, cdest;

		while (curr_size < outData.length) {
			try {
				flags = his.readU8();
			} catch (EOFException ex) {
				break;
			}
			for (int i = 0; i < 8 && curr_size < outData.length; i++) {
				flag = (flags & (0x80 >> i)) > 0;
				if (flag) {
					try {
						b1 = his.readU8();
					} catch (EOFException ex) {
						throw new Exception("Incomplete data");
					}

					switch (b1 >> 4) {
						case 0:
							len = b1 << 4;
							try {
								bt = his.readU8();
							} catch (EOFException ex) {
								throw new Exception("Incomplete data");
							}
							len |= bt >> 4;
							len += 0x11;
							disp = (bt & 0x0F) << 8;
							try {
								b2 = his.readU8();
							} catch (EOFException ex) {
								throw new Exception("Incomplete data");
							}
							disp |= b2;
							break;
						case 1:
							try {
								bt = his.readU8();
								b2 = his.readU8();
								b3 = his.readU8();
							} catch (EOFException ex) {
								throw new Exception("Incomplete data");
							}
							len = (b1 & 0xF) << 12; // len = b000
							len |= bt << 4; // len = bcd0
							len |= (b2 >> 4); // len = bcde
							len += 0x111; // len = bcde + 0x111
							disp = (b2 & 0x0F) << 8; // disp = f
							disp |= b3; // disp = fgh
							break;

						default:
							len = (b1 >> 4) + 1;
							disp = (b1 & 0x0F) << 8;
							try {
								b2 = his.readU8();
							} catch (EOFException ex) {
								throw new Exception("Incomplete data");
							}
							disp |= b2;
							break;
					}

					if (disp > curr_size)
						throw new Exception("Cannot go back more than already written");

					cdest = curr_size;

					for (int j = 0; j < len && curr_size < outData.length; j++)
						outData[curr_size++] = outData[cdest - disp - 1 + j];

                }
				else {
					try {
						outData[curr_size++] = his.readU8();
					} catch (EOFException ex) {
						break;
					}// throw new Exception("Incomplete data"); }
                }
                if (curr_size > outData.length)
                    break;
            }
		}
		return outData;
	}

	// note: untested
	private static int[] DecompressRLE(HexInputStream his) throws Exception {
		int[] outData = new int[getLength(his)];
		int curr_size = 0;
		int i, rl;
		int flag, b;
		boolean compressed;
		while (true) {
			// get tag
			try {
				flag = his.readU8();
			} catch (EOFException ex) {
				break;
			}
			compressed = (flag & 0x80) > 0;
			rl = flag & 0x7F;
			if (compressed)
				rl += 3;
			else
				rl += 1;

			if (compressed) {
				try {
					b = his.readU8();
				} catch (EOFException ex) {
					break;
				}
				for (i = 0; i < rl; i++)
					outData[curr_size++] = b;
			}
			else
				for (i = 0; i < rl; i++)
					try {
						outData[curr_size++] = his.readU8();
					}
					catch (EOFException ex) {
						break;
					}

			if (curr_size > outData.length)
				throw new Exception("curr_size > decomp_size; " + curr_size + ">" + outData.length);
			if (curr_size == outData.length)
				break;
		}
		return outData;
	}

	// note: untested
	private static int[] DecompressHuff(HexInputStream his) throws Exception {
		his.skip(-1);
		int firstByte = his.readU8();
		int dataSize = firstByte & 0x0F;
		if (dataSize != 8 && dataSize != 4)
			throw new Exception("Unhandled dataSize " + Integer.toHexString(dataSize));

		int decomp_size = getLength(his);
		int treeSize = his.readU8();
		HuffTreeNode.maxInpos = 4 + (treeSize + 1) * 2;
		HuffTreeNode rootNode = new HuffTreeNode();
		rootNode.parseData(his);
		his.setPosition(4 + (treeSize + 1) * 2L);
		// read all data
		int[] indata = new int[(int) (his.available() - his.getPosition()) / 4];
		for (int i = 0; i < indata.length; i++)
			indata[i] = his.readS32();

		int curr_size = 0;
		decomp_size *= dataSize == 8 ? 1 : 2;
		int[] outdata = new int[decomp_size];

		int idx = -1;
		StringBuilder codestr = new StringBuilder();
		NLinkedList<Integer> code = new NLinkedList<>();
		while (curr_size < decomp_size) {
			try {
				codestr.append(Integer.toBinaryString(indata[++idx]));
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new Exception("not enough data.", e);
			}
			while (codestr.length() > 0) {
				code.addFirst(Integer.parseInt(codestr.charAt(0) + ""));
				codestr = new StringBuilder(codestr.substring(1));
				Pair<Boolean, Integer> attempt = rootNode.getValue(code.getLast());
				if (attempt.getFirst()) {
					try {
						outdata[curr_size++] = attempt.getSecond();
					} catch (ArrayIndexOutOfBoundsException ex) {
						if (code.getFirst().getValue() != 0)
							throw ex;
					}
					code.clear();
				}
			}
		}
		if (codestr.length() > 0 || idx < indata.length - 1) {
			while (idx < indata.length - 1)
				codestr.append(Integer.toBinaryString(indata[++idx]));
			codestr = new StringBuilder(codestr.toString().replace("0", ""));
			if (codestr.length() > 0)
				System.out.println("too much data; str=" + codestr + ", idx=" + idx + "/" + indata.length);
		}

		int[] realout;
		if (dataSize == 4) {
			realout = new int[decomp_size / 2];
			for (int i = 0; i < decomp_size / 2; i++)
			{
				if ((outdata[i * 2] & 0xF0) > 0 || (outdata[i * 2 + 1] & 0xF0) > 0)
					throw new Exception("first 4 bits of data should be 0 if dataSize = 4");
				realout[i] = (byte) ((outdata[i * 2] << 4) | outdata[i * 2 + 1]);
			}
		}
		else {
			realout = outdata;
		}
		return realout;
	}
}