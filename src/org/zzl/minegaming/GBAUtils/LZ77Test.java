package org.zzl.minegaming.GBAUtils;

import java.awt.Point;
import java.io.IOException;
import java.net.URLDecoder;

public class LZ77Test {
	public static byte[] ROM = { 
			(byte) 0x10, (byte) 0x00, (byte) 0x08,
			(byte) 0x00, (byte) 0x3F, (byte) 0x00, (byte) 0x00, (byte) 0xF0,
			(byte) 0x01, (byte) 0xF0, (byte) 0x01, (byte) 0xF0, (byte) 0x01,
			(byte) 0xF0, (byte) 0x01, (byte) 0xF0, (byte) 0x01, (byte) 0xF0,
			(byte) 0x01, (byte) 0xFF, (byte) 0xF0, (byte) 0x01, (byte) 0xF0,
			(byte) 0x01, (byte) 0xF0, (byte) 0x01, (byte) 0xF0, (byte) 0x01,
			(byte) 0xF0, (byte) 0x01, (byte) 0xF0, (byte) 0x01, (byte) 0xF0,
			(byte) 0x01, (byte) 0xF0, (byte) 0x01, (byte) 0xFF, (byte) 0xF0,
			(byte) 0x01, (byte) 0xF0, (byte) 0x01, (byte) 0xF0, (byte) 0x01,
			(byte) 0xF0, (byte) 0x01, (byte) 0xF0, (byte) 0x01, (byte) 0xF0,
			(byte) 0x01, (byte) 0xF0, (byte) 0x01, (byte) 0xF0, (byte) 0x01,
			(byte) 0xFF, (byte) 0xF0, (byte) 0x01, (byte) 0xF0, (byte) 0x01,
			(byte) 0xF0, (byte) 0x01, (byte) 0xF0, (byte) 0x01, (byte) 0xF0,
			(byte) 0x01, (byte) 0xF0, (byte) 0x01, (byte) 0xF0, (byte) 0x01,
			(byte) 0xF0, (byte) 0x01, (byte) 0xFF, (byte) 0xF0, (byte) 0x01,
			(byte) 0xF0, (byte) 0x01, (byte) 0xF0, (byte) 0x01, (byte) 0xF0,
			(byte) 0x01, (byte) 0xF0, (byte) 0x01, (byte) 0xF0, (byte) 0x01,
			(byte) 0xF0, (byte) 0x01, (byte) 0xF0, (byte) 0x01, (byte) 0xFF,
			(byte) 0xF0, (byte) 0x01, (byte) 0xF0, (byte) 0x01, (byte) 0xF0,
			(byte) 0x01, (byte) 0xF0, (byte) 0x01, (byte) 0xF0, (byte) 0x01,
			(byte) 0xF0, (byte) 0x01, (byte) 0xF0, (byte) 0x01, (byte) 0xF0,
			(byte) 0x01, (byte) 0x28, (byte) 0x00, (byte) 0x30, (byte) 0xC0,
			(byte) 0x0B, (byte) 0x53, (byte) 0x30, (byte) 0x03, (byte) 0x33,
			(byte) 0x55, (byte) 0x00, (byte) 0x10, (byte) 0x33, (byte) 0x44,
			(byte) 0x44, (byte) 0x00, (byte) 0x02, (byte) 0x44, (byte) 0x00,
			(byte) 0x33, (byte) 0x33, (byte) 0xA0, (byte) 0x00, (byte) 0x0D,
			(byte) 0x55, (byte) 0x00, (byte) 0x02, (byte) 0x45, (byte) 0x44,
			(byte) 0x55, (byte) 0x55, (byte) 0x44, (byte) 0x64, (byte) 0x44,
			(byte) 0x00, (byte) 0x02, (byte) 0x80, (byte) 0x01, (byte) 0x04,
			(byte) 0x44, (byte) 0x00, (byte) 0x42, (byte) 0x33, (byte) 0x00,
			(byte) 0x0D, (byte) 0x00, (byte) 0x43, (byte) 0x44, (byte) 0x22,
			(byte) 0x10, (byte) 0x2C, (byte) 0xF0, (byte) 0x01, (byte) 0x04,
			(byte) 0x10, (byte) 0x73, (byte) 0x9E, (byte) 0x00, (byte) 0x20,
			(byte) 0x00, (byte) 0x44, (byte) 0x00, (byte) 0x47, (byte) 0x00,
			(byte) 0x4E, (byte) 0xE0, (byte) 0x25, (byte) 0xD0, (byte) 0x93,
			(byte) 0x02, (byte) 0xBD, (byte) 0x80, (byte) 0x03, (byte) 0x24,
			(byte) 0xF0, (byte) 0xB0, (byte) 0xF0, (byte) 0x01, (byte) 0xF0,
			(byte) 0x01, (byte) 0xD0, (byte) 0x01, (byte) 0x43, (byte) 0x80,
			(byte) 0x03, (byte) 0xF6, (byte) 0x10, (byte) 0xEF, (byte) 0x90,
			(byte) 0xF3, (byte) 0x10, (byte) 0x93, (byte) 0xE0, (byte) 0xB4,
			(byte) 0x42, (byte) 0x80, (byte) 0x03, (byte) 0xF0, (byte) 0x01,
			(byte) 0x33, (byte) 0x66, (byte) 0x22, (byte) 0xF0, (byte) 0x14,
			(byte) 0x71, (byte) 0x14, (byte) 0x11, (byte) 0x11, (byte) 0xF1,
			(byte) 0x09, (byte) 0xF0, (byte) 0x01, (byte) 0x44, (byte) 0x7F,
			(byte) 0x34, (byte) 0x50, (byte) 0x03, (byte) 0x10, (byte) 0xE3,
			(byte) 0x10, (byte) 0xE7, (byte) 0x90, (byte) 0xF7, (byte) 0xF0,
			(byte) 0x01, (byte) 0xF0, (byte) 0x01, (byte) 0xF0, (byte) 0x01,
			(byte) 0xD5, (byte) 0xF0, (byte) 0x01, (byte) 0x40, (byte) 0x0A,
			(byte) 0x20, (byte) 0x00, (byte) 0x12, (byte) 0x52, (byte) 0x01,
			(byte) 0xEA, (byte) 0x55, (byte) 0x30, (byte) 0x03, (byte) 0x5B,
			(byte) 0x53, (byte) 0x10, (byte) 0x03, (byte) 0x45, (byte) 0x01,
			(byte) 0xFA, (byte) 0x11, (byte) 0xBB, (byte) 0x43, (byte) 0x11,
			(byte) 0xCF, (byte) 0x01, (byte) 0xDA, (byte) 0xF6, (byte) 0x30,
			(byte) 0x03, (byte) 0xF0, (byte) 0xA8, (byte) 0xF0, (byte) 0xCB,
			(byte) 0x91, (byte) 0x09, (byte) 0x43, (byte) 0xF0, (byte) 0x13,
			(byte) 0x02, (byte) 0x32, (byte) 0x54, (byte) 0xF4, (byte) 0x10,
			(byte) 0x52, (byte) 0x81, (byte) 0x2B, (byte) 0x50, (byte) 0xEB,
			(byte) 0x12, (byte) 0x4E, (byte) 0x33, (byte) 0x02, (byte) 0x42,
			(byte) 0x33, (byte) 0x45, (byte) 0x11, (byte) 0x44, (byte) 0x34,
			(byte) 0x33, (byte) 0x00, (byte) 0x02, (byte) 0x33, (byte) 0x32,
			(byte) 0x33, (byte) 0x02, (byte) 0x5C, (byte) 0xBF, (byte) 0x81,
			(byte) 0xF7, (byte) 0x23, (byte) 0x80, (byte) 0x03, (byte) 0xF1,
			(byte) 0x0B, (byte) 0xF0, (byte) 0x01, (byte) 0xF0, (byte) 0x01,
			(byte) 0xF0, (byte) 0x01, (byte) 0x42, (byte) 0x7E, (byte) 0xDD,
			(byte) 0xF0, (byte) 0x01, (byte) 0x52, (byte) 0x1C, (byte) 0x44,
			(byte) 0x02, (byte) 0xDF, (byte) 0xF0, (byte) 0x21, (byte) 0x80,
			(byte) 0x3D, (byte) 0x43, (byte) 0x02, (byte) 0xE3, (byte) 0xC7,
			(byte) 0xF0, (byte) 0x21, (byte) 0x62, (byte) 0x3F, (byte) 0x33,
			(byte) 0x22, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0xF0,
			(byte) 0x19, (byte) 0x40, (byte) 0x0A, (byte) 0x2F, (byte) 0x22,
			(byte) 0x32, (byte) 0x00, (byte) 0x20, (byte) 0x22, (byte) 0xF0,
			(byte) 0x20, (byte) 0xF0, (byte) 0x01, (byte) 0xF0, (byte) 0x01,
			(byte) 0xF0, (byte) 0x01, (byte) 0xFF, (byte) 0xF0, (byte) 0x01,
			(byte) 0xF0, (byte) 0x01, (byte) 0xF0, (byte) 0x01, (byte) 0xF0,
			(byte) 0x01, (byte) 0xF0, (byte) 0x01, (byte) 0xF0, (byte) 0x01,
			(byte) 0xF0, (byte) 0x01, (byte) 0xF0, (byte) 0x01, (byte) 0xFF,
			(byte) 0xF0, (byte) 0x01, (byte) 0xF0, (byte) 0x01, (byte) 0xF0,
			(byte) 0x01, (byte) 0xF0, (byte) 0x01, (byte) 0xF0, (byte) 0x01,
			(byte) 0xF0, (byte) 0x01, (byte) 0xF0, (byte) 0x01, (byte) 0x10,
			(byte) 0x01, (byte) 0x3E, (byte) 0x00 };

	public static int[] uncompressed = { 
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x30, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x30, 0x00, 0x00, 0x00, 0x53, 0x00, 0x00, 0x00, 0x53, 0x00,
			0x00, 0x33, 0x55, 0x00, 0x33, 0x44, 0x44, 0x33, 0x44, 0x44, 0x44,
			0x00, 0x33, 0x33, 0x00, 0x33, 0x55, 0x55, 0x33, 0x55, 0x55, 0x45,
			0x44, 0x55, 0x55, 0x44, 0x44, 0x55, 0x44, 0x44, 0x44, 0x44, 0x44,
			0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x04, 0x44, 0x00,
			0x00, 0x30, 0x33, 0x00, 0x00, 0x43, 0x44, 0x22, 0x33, 0x44, 0x44,
			0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44,
			0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x04, 0x00, 0x00,
			0x00, 0x00, 0x33, 0x00, 0x00, 0x00, 0x44, 0x33, 0x33, 0x00, 0x44,
			0x44, 0x44, 0x33, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44,
			0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x02,
			0x00, 0x00, 0x00, 0x24, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x43, 0x00, 0x00,
			0x00, 0x43, 0x00, 0x00, 0x00, 0x43, 0x00, 0x00, 0x00, 0x30, 0x00,
			0x00, 0x00, 0x30, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44,
			0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44,
			0x44, 0x44, 0x42, 0x44, 0x44, 0x44, 0x42, 0x44, 0x44, 0x44, 0x42,
			0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44,
			0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x33,
			0x22, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44,
			0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44,
			0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x11, 0x11, 0x22, 0x33,
			0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44,
			0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44,
			0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44,
			0x44, 0x44, 0x34, 0x44, 0x44, 0x44, 0x34, 0x44, 0x44, 0x44, 0x34,
			0x24, 0x00, 0x00, 0x00, 0x24, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00,
			0x00, 0x02, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x20, 0x00, 0x00, 0x00, 0x52, 0x00, 0x00, 0x30, 0x55, 0x00, 0x00,
			0x30, 0x55, 0x00, 0x00, 0x53, 0x55, 0x00, 0x00, 0x53, 0x45, 0x00,
			0x00, 0x30, 0x44, 0x00, 0x00, 0x30, 0x43, 0x55, 0x44, 0x44, 0x44,
			0x55, 0x45, 0x44, 0x44, 0x55, 0x45, 0x44, 0x44, 0x55, 0x44, 0x44,
			0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44,
			0x44, 0x44, 0x44, 0x44, 0x44, 0x33, 0x44, 0x44, 0x44, 0x44, 0x44,
			0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44,
			0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44,
			0x44, 0x33, 0x43, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44,
			0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44,
			0x44, 0x44, 0x54, 0x44, 0x44, 0x44, 0x55, 0x44, 0x44, 0x44, 0x44,
			0x44, 0x44, 0x33, 0x22, 0x44, 0x44, 0x44, 0x34, 0x44, 0x44, 0x44,
			0x34, 0x44, 0x44, 0x44, 0x33, 0x44, 0x44, 0x44, 0x33, 0x55, 0x44,
			0x44, 0x33, 0x45, 0x44, 0x34, 0x33, 0x44, 0x34, 0x33, 0x33, 0x32,
			0x33, 0x33, 0x33, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x02, 0x00, 0x00, 0x00, 0x23, 0x00, 0x00, 0x00, 0x23, 0x00, 0x00,
			0x00, 0x23, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x02, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x33, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x43,
			0x44, 0x33, 0x00, 0x33, 0x33, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x33,
			0x43, 0x44, 0x00, 0x00, 0x33, 0x33, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x44, 0x33, 0x22,
			0x00, 0x33, 0x22, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x22, 0x32, 0x33, 0x22,
			0x00, 0x22, 0x22, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

	static int palette[] = {
			0x00, 0x00, 0xC6, 0x18, 0x2A, 0x5A,
			0xAE, 0x6A, 0x13, 0x77, 0x78, 0x7B,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00
		};
	
	public static void main(String[] args) throws Exception {
		CheckCompression();
	}
	
	public static byte[] makeBytes(byte b, int reps) {
		byte[] arr = new byte[reps];
		for(int i = 0; i < reps; i++)
			arr[i] = b;
		return arr;
	}
	
	public static void CheckCompression() {
		int[] decompressed =  Lz77.decompressLZ77(ROM, 0);
		boolean broken = false;
		for(int i = 0; i < decompressed.length; i++) {
			if(decompressed[i] != uncompressed[i]) {
				broken = true;
				break;
			}
			if(broken) {
				System.out.println("Decompression failed!");
				System.exit(0);
			}			
		}
		System.out.println("Decompression success!");
			
		byte[] recompressed = Lz77.compressLZ77(decompressed);
		for(int i = 0; i < recompressed.length; i++) {
			if(recompressed[i] != ROM[i]) {
				broken = true;
				break;
			}
			if(broken) {
				System.out.println("Recompression failed!");
				System.exit(0);
			}
		}
		CreateImage();
		System.out.println("Recompression is identical to original!");
	}
	
	public static void CreateImage() {
		byte[] data = null;
		byte[] paldats = null;
		String path = LZ77Test.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		
		try {
			String decodedPath = URLDecoder.decode(path, "UTF-8");
			data = BitConverter.toBytes(uncompressed);
			paldats = BitConverter.toBytes(palette);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		GBAImage g = new GBAImage(BitConverter.ToInts(data), new Palette(GBAImageType.c16,BitConverter.ToInts(paldats)), new Point(64,64));	
		GBAImage backwards = GBAImage.fromImage(g.getBufferedImage(), new Palette(GBAImageType.c16,BitConverter.ToInts(paldats)));
		
		PictureFrame pf = new PictureFrame(g.getBufferedImage());
		pf.setSize(128, 128);
		pf.setVisible(true);
		
		PictureFrame pf2 = new PictureFrame(backwards.getBufferedImage());
		pf2.setSize(128, 128);
		pf2.setVisible(true);
	}
}