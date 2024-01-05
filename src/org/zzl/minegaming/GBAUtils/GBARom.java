package org.zzl.minegaming.GBAUtils;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GBARom implements Cloneable {

	private final String headerCode;
	private final String headerName;
	private final String headerMaker;

	static byte[] rom_bytes;
	public String input_filepath;
	static byte[] current_rom_header;

	HashMap<String, String> rom_header_names = new HashMap<>();
	HashMap<String, String> hex_tbl = new HashMap<>();

	public boolean isPrimalDNAdded = false;
	public boolean isRTCAdded = false;
	public boolean isDNPkmnPatchAdded = false;

	/**
	 * Loads a ROM using a file dialog.
	 */
	public static String getLocationFromFileDialog() {
		boolean errorOccurred = false;
		String location = "";
		try {
			FileDialog fd = new FileDialog(new Frame(), "Load ROM", FileDialog.LOAD);
			fd.setFilenameFilter((dir, name) -> (
							name.toLowerCase().endsWith(".gba") ||
							name.toLowerCase().endsWith(".bin") ||
							name.toLowerCase().endsWith(".rbc") ||
							name.toLowerCase().endsWith(".rbh") ||
							name.toLowerCase().endsWith(".but") ||
							name.toLowerCase().endsWith(".bmp")));
			fd.setDirectory(System.getProperty("user.home"));
			fd.setVisible(true);
			String dialogLocation = fd.getDirectory() + fd.getFile();
			if (dialogLocation.isEmpty()) { // The user canceled the selection
				location = ""; // Set to empty string to signify canceled selection
			} else {// Valid location obtained from FileDialog
				location = dialogLocation;
			}
		} catch (NullPointerException | SecurityException e) { // Catch specific exceptions that might indicate an error
			e.printStackTrace();
			errorOccurred = true; // Set error flag
		}
		return errorOccurred ? null : location;
	}

	/**
	 * Loads a ROM using a file chooser.
	 */
	public static String getLocationFromFileChooser() {
		FileFilter filter = new FileNameExtensionFilter("GBA ROM", "gba", "bin");
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(filter);
		int result = fileChooser.showOpenDialog(new Frame());
		if (result != JFileChooser.APPROVE_OPTION) {
			System.out.println("User canceled the file selection.");
			return null; // Indicate failure to obtain a location
		}
		String location = fileChooser.getSelectedFile().getAbsolutePath();
		if (location.isEmpty()) {
			return null; // Indicate failure to obtain a location
		}
		return location;
	}

	/**
	 * Attempts to load a ROM using a FileDialog, adds it to ROMManager, and sets it as the active ROM.
	 * If the ROM file is successfully loaded, attempts to load a hex table file if the active ROM's table is empty.
	 *
	 * @return The ROMManager ROM ID if successful.
	 *         -1 if no file is selected.
	 *         -2 for a general IO error during ROM loading or hex table loading.
	 *         -3 if the hex table file is not found.
	 */
	public static int loadRom() {
		String location = getLocationFromFileDialog();
		if (location == null){
			location = getLocationFromFileChooser();
		}
        assert location != null;
        if (location.isEmpty()) {
			System.out.println("No file selected.");
			return -1; // Indicate no file selected
		}
		int romID = ROMManager.getID();
		try {
			ROMManager.AddROM(romID, new GBARom(location));}
		catch (IOException e) {
			e.printStackTrace();
			return -2;
		}
		ROMManager.ChangeROM(romID);
		if(ROMManager.getActiveROM().hex_tbl.isEmpty()) {
			try {
				ROMManager.getActiveROM().loadHexTBL("/resources/poketable.tbl");
			}
			catch (IOException e) {
				e.printStackTrace();
				return -3;
			}
		}
		System.out.println(romID);
		return romID;
	}

	/**
	 * Initializes the GBARom instance with the provided ROM file path.
	 * Loads the ROM file into bytes and extracts essential header information.
	 * Throws an IOException if there are issues during the ROM loading process.
	 *
	 * @param rom_path Path to the ROM file
	 * @throws IOException if there's an issue loading the ROM into bytes
	 */
	public GBARom(String rom_path) throws IOException {
		input_filepath = rom_path;
		try {
			loadRomToBytes();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		
		headerCode = readText(0xAC,4);
		headerName = readText(0xA0, 12).trim();
		headerMaker = readText(0xB0, 2);

		updateROMHeaderNames();
		updateFlags();
	}

	/**
	 * Updates flags based on header codes.
	 * For "BPRE" header code:
	 * - Checks specific addresses for DNPkmnPatch and RTC presence, sets corresponding flags.
	 * For "BPEE" header code:
	 * - Always sets RTC flag and checks for DNPkmnPatch presence.
	 */
	public void updateFlags() {
		if(headerCode.equalsIgnoreCase("BPRE")) {
			if(readByte(0x082903) == 0x8)
				isDNPkmnPatchAdded = true;
			if(readByte(0x427) == 0x8)
				isRTCAdded = true;
		} else if(headerCode.equalsIgnoreCase("BPEE")) {
			isRTCAdded = true;
			if(readByte(0x0B4C7F) == 0x8)
				isDNPkmnPatchAdded = true;
		}
	}

	/**
	 * Loads the content of the ROM file into the byte array 'rom_bytes'.
	 * Throws an IOException if there's an issue reading the file.
	 */
	public void loadRomToBytes() throws IOException {
		// Create a File object using the provided input file path
		File file = new File(input_filepath);
		// Create an InputStream to read the file content
		try (InputStream is = Files.newInputStream(file.toPath())) {
			// Determine the length of the file
			long length = file.length();
			// Initialize the byte array to hold the ROM file content
			rom_bytes = new byte[(int) length];
			int offset = 0, bytesRead;
			// Read content from the file into the byte array
			while (offset < rom_bytes.length && (bytesRead = is.read(rom_bytes, offset, rom_bytes.length - offset)) >= 0) {
				offset += bytesRead; // Update the offset after each read
			}
		} // The try-with-resources block will automatically close the InputStream
	}

	/**
	 * Reads bytes from the ROM from the given offset as a hexadecimal string into an array of a given size.
	 * @param offset Offset in ROM as a hexadecimal string
	 * @param size   Amount of bytes to retrieve
	 * @return       Byte array containing the read bytes
	 */
	public byte[] readBytes(String offset, int size) {
		int offs = convertOffsetToInt(offset);
		return readBytes(offs, size);
	}

	/**
	 * Reads bytes from the ROM from the given offset into an array of a given size.
	 * @param offset Offset in ROM
	 * @param size   Amount of bytes to retrieve
	 * @return       Byte array containing the read bytes
	 */
	public byte[] readBytes(int offset, int size) {
		return BitConverter.GrabBytes(rom_bytes, offset, size);
	}

	/**
	 * Reads a specified number of bytes from the ROM from an internal offset into an array.
	 * Advances the internal offset after reading.
	 * @param size Amount of bytes to retrieve
	 * @return     Byte array containing the read bytes
	 */
	public byte[] readBytes(int size) {
		byte[] t=BitConverter.GrabBytes(rom_bytes, internalOffset, size);
		internalOffset+=size;	
		return t;
	}

	/**
	 * Reads a byte from an offset
	 * @param offset Offset to read from
	 */
	public byte readByte(int offset) {
		return readBytes(offset,1)[0];
	}

	public byte readByte() {
		byte t = rom_bytes[internalOffset];
		internalOffset+=1;
		return t;
	}

	public int readByteAsInt(int offset) {
		return BitConverter.ToInts(readBytes(offset,1))[0];
	}

	public int readByteAsInt() {
		int tmp=BitConverter.ToInts(readBytes(internalOffset,1))[0];
		internalOffset++;
		return tmp;
	}

	public long readLong() {
		byte[] t=readBytes(4);
		internalOffset+=4;
		return BitConverter.ToInt32(t);
		
	}

	public long readLong(int offset) {
		byte[] t=readBytes(offset, 4);
		return BitConverter.ToInt32(t);
		
	}
	/**
	 * Reads a 16 bit word from an offset
	 * @param offset Offset to read from
	 */
	public int readWord(int offset) {
		int[] words = BitConverter.ToInts(readBytes(offset,2));
		return (words[1] << 8) + (words[0]);
	}
	
	public void writeWord(int offset, int toWrite) {
		int[] bytes = new int[] {toWrite & 0xFF, (toWrite & 0xFF00) >> 8};
		byte[] nBytes = BitConverter.toBytes(bytes);
		writeBytes(offset,nBytes);
	}
	
	public void writeWord(int toWrite) {
		writeWord(internalOffset,toWrite);
		internalOffset += 2;
	}

	public int readWord() {
		int[] words = BitConverter.ToInts(readBytes(internalOffset,2));
		internalOffset+=2;
		return (words[1] << 8) + (words[0]);
	}

	public void writeBytes(int offset, byte[] bytes_to_write) {
        for (byte b : bytes_to_write) {
            try {
                rom_bytes[offset] = b;
                offset++;
            } catch (Exception e) {
                System.out.println("Tried to write outside of bounds! (" + (offset) + ")");
            }
        }
	}
	
	public void writeByte(byte b, int offset) {
		rom_bytes[offset] = b;
	}
	
	public void writeByte(byte b) {
		rom_bytes[internalOffset] = b;
		internalOffset++;
	}
	
    public int internalOffset;

    public void writeBytes(byte[] bytes_to_write) {
        for (byte b : bytes_to_write) {
            rom_bytes[internalOffset] = b;
            internalOffset++;
        }
	}

	public int commitChangesToROMFile() {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(input_filepath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return 1;
		}
		try {
			fos.write(rom_bytes);
		} catch (IOException e) {
			e.printStackTrace();
			return 2;
		}
		try {
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return 3;
		}
		return 0;
	}

	/**
	 * Convert a string offset i.e. 0x943BBD into a decimal
	 * Used for directly accessing the ROM byte array
	 * @param offset Offset to convert to an integer
	 * @return The offset as an int
	 */
	public int convertOffsetToInt(String offset)
	{
		return Integer.parseInt(offset, 16);
	}

	/**
	 * Retrieve the header of the ROM, based on offset and size
	 * Identical to readBytesFromROM just with a different name
	 */
	@Deprecated
	public byte[] getROMHeader(String header_offset, int header_size) {
		current_rom_header = readBytes(header_offset, header_size);
		return current_rom_header;
	}

	/**
	 * Validate the file loaded based on a given byte and offset
	 * @param validation_offset Offset to check in the ROM
	 * @param validation_byte Byte to check it with
	 */
	public Boolean validateROM(int validation_offset, byte validation_byte) {
        return rom_bytes[validation_offset] == validation_byte;
	}

	/**
	 * Load a HEX table file for character mapping i.e. Poketext
	 * @param tbl_path File path to the character table
	 */
	public void loadHexTBLFromFile(String tbl_path) throws IOException {
		File file = new File(tbl_path);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while ((line = br.readLine()) != null) {
			String[] separated = line.split("=");
			String key;
			String value;

			if (separated.length > 1) {
				key = separated[0];
				value = separated[1];
			} else {
				key = separated[0];
				value = " ";
			}
			hex_tbl.put(key, value);
		}
		br.close();
	}
	
	/**
	 *  Load a HEX table file for character mapping i.e. Poketext
	 * @param tbl_path File path to the character table
	 */
	public void loadHexTBL(String tbl_path) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(GBARom.class.getResourceAsStream(tbl_path))));
		String line;
		while ((line = br.readLine()) != null) {
			String[] seperated = line.split("=");
			String key;
			String value;

			if (seperated.length > 1) {
				key = seperated[0];
				value = seperated[1];
			} else {
				key = seperated[0];
				value = " ";
			}
			hex_tbl.put(key, value);
		}
		br.close();
	}

	/**
	 *  Convert Poketext to ascii, takes an array of bytes of poketext
	 *  Basically returns the results from the given HEX Table <- must loadHexTBL first
	 * @param poketext Poketext as a byte array
	 */
	public String convertPoketextToAscii(byte[] poketext) {
		StringBuilder converted = new StringBuilder();
        for (byte b : poketext) {
            String temp;
            temp = hex_tbl.get(String.format("%02X", b));
            converted.append(temp);
        }
		return converted.toString().trim();
	}

	/**
	 * Return a string of the friendly ROM header based on the current ROM
	 */
	public String getFriendlyROMHeader() {
		return rom_header_names.get(new String(current_rom_header));
	}

	// Update the list of friendly ROM headers
	private void updateROMHeaderNames() {
		rom_header_names.put("POKEMON FIREBPRE01", "Pokémon: FireRed");
		rom_header_names.put("POKEMON LEAFBPGE01", "Pokémon: LeafGreen");
		rom_header_names.put("POKEMON EMERBPEE01", "Pokémon: Emerald");
	}

	/**
	 * Read a structure of data from the ROM at a given offset, a set numner of times, with a set structure size
	 * For example returning the names of Pokémon into an ArrayList of bytes
	 * @param offset Offset to read the structure from
	 * @param amount Amount to read
	 * @param max_struct_size Maximum structure size
	 */
	public ArrayList<byte[]> loadArrayOfStructuredData(int offset, int amount, int max_struct_size) {
		ArrayList<byte[]> data = new ArrayList<>();
		int offs = offset & 0x1FFFFFF;
		for (int count = 0; count < amount; count++) {
			byte[] temp_byte = new byte[max_struct_size];
			for (int c2 = 0; c2 < temp_byte.length; c2++) {
				temp_byte[c2] = rom_bytes[offs];
				offs++;
			}
			data.add(temp_byte);
		}
		return data;
	}

	/**
	 * Reads ASCII text from the ROM
	 * @param offset The offset to read from
	 * @param length The amount of text to read
	 * @return Returns the text as a String object
	 */
	public String readText(int offset, int length) {
		return new String(BitConverter.GrabBytes(rom_bytes, offset, length));
	}
	
	public String readPokeText(int offset) {
		return readPokeText(offset, -1);
	}
	
	public String readPokeText(int offset, int length) {
		if(length > -1)
			return convertPoketextToAscii(BitConverter.GrabBytes(getData(), offset, length));

		byte b = 0x0;
		int i = 0;
		while(b != -1) {
			b = getData()[offset+i];
			i++;
		}
		return convertPoketextToAscii(BitConverter.GrabBytes(getData(), offset, i));
	}
	
	public String readPokeText() {
		byte b = 0x0;
		int i = 0;
		while(b != -1) {
			b = getData()[internalOffset+i];
			i++;
		}
		String s = convertPoketextToAscii(BitConverter.GrabBytes(getData(), internalOffset, i));
		internalOffset += i;
		return s;
	}
	
	public byte[] getData() {
		return rom_bytes;
	}
	
	/**
	 * Gets a pointer at an offset
	 * @param offset Offset to get the pointer from
	 * @param fullPointer Whether we should fetch the full 32 bit pointer or the 24 bit byte[] friendly version.
	 * @return Pointer as a Long
	 */
	public long getPointer(int offset, boolean fullPointer) {
		byte[] data = BitConverter.GrabBytes(getData(), offset, 4);
		if(!fullPointer)
			data[3]=0;
		return BitConverter.ToInt32(data);
	}
	
	/**
	 * Gets a 24 bit pointer in the ROM as an integer. 
	 * @param offset Offset to get the pointer from
	 * @return Pointer as a Long
	 */
	public long getPointer(int offset) {
		return getPointer(offset,false) & 0x1FFFFFF;
	}
	
	/**
	 * Gets a pointer in the ROM as an integer. 
	 * Does not support 32 bit pointers due to Java's integer size not being long enough.
	 * @param offset Offset to get the pointer from
	 * @return Pointer as an Integer
	 */
	public int getPointerAsInt(int offset) {
		return (int)getPointer(offset,false);
	}
	
	public long getSignedLong(boolean fullPointer) {
		byte[] data = BitConverter.GrabBytes(getData(), internalOffset, 4);
		if(!fullPointer)
			data[3] = 0;
		internalOffset+=4;
        return (BitConverter.ToInt32(data));
	}
	
	/**
	 * Reverses and writes a pointer to the ROM
	 * @param pointer Pointer to write
	 * @param offset Offset to write it at
	 */
	public void writePointer(long pointer, int offset) {
		byte[] bytes = BitConverter.GetBytes(pointer);
		writeBytes(offset,bytes);
	}
	
	/**
	 * Reverses and writes a pointer to the ROM. Assumes pointer is ROM memory and appends 08 to it.
	 * @param pointer Pointer to write (appends 08 automatically)
	 * @param offset Offset to write it at
	 */
	public void writePointer(int pointer, int offset) {
		byte[] bytes = BitConverter.GetBytes(pointer);
		bytes = BitConverter.ReverseBytes(bytes);
		bytes[3] = 0x08;
		writeBytes(offset,bytes);
	}
	
	/**
	 * Gets the game code from the ROM, ie BPRE for US Pkmn Fire Red
	 */
	public String getGameCode() {
		return headerCode;
	}
	
	/**
	 * Gets the game text from the ROM, ie POKÉMON FIRE for US Pkmn Fire Red
	 */
	public String getGameText() {
		return headerName;
	}
	
	/**
	 * Gets the game creator ID as a String, ie '01' is GameFreak's Company ID
	 */
	public String getGameCreatorID() {
		return headerMaker;
	}

	public long getPointer(boolean fullPointer) {
		byte[] data = BitConverter.GrabBytes(getData(), internalOffset, 4);
		if(!fullPointer && data[3] >= 0x8)
			data[3] -= 0x8;
		internalOffset+=4;
		return BitConverter.ToInt32(data);
	}

	public long getPointer() {
		return getPointer(false);
	}

	public int getPointerAsInt() {
		return (int)getPointer(internalOffset,false);
	}

	public void writePointer(long pointer) {
		byte[] bytes = BitConverter.ReverseBytes(BitConverter.GetBytes(pointer));
		writeBytes(internalOffset,bytes);
		internalOffset+=4;
	}
	
	public void writeSignedPointer(long pointer) {
		byte[] bytes = BitConverter.ReverseBytes(BitConverter.GetBytes(pointer));
		writeBytes(internalOffset,bytes);
		internalOffset+=4;
	}

	public void writePointer(int pointer) {
		byte[] bytes = BitConverter.ReverseBytes(BitConverter.GetBytes(pointer));
		bytes[3] += 0x8;
		writeBytes(internalOffset,bytes);
		internalOffset+=4;
	}
	
	/**
	 * Gets the game code from the ROM, ie BPRE for US Pkmn Fire Red
	 */
	public void Seek(int offset) {
		if(offset > 0x08000000)
			offset &= 0x1FFFFFF;

		internalOffset=offset;
	}

	public byte freeSpaceByte = (byte)0xFF;
	public int findFreespace(int length) {
		return findFreespace(length, 0, false);
	}
	
	public int findFreespace(int length, boolean asmSafe) {
		return findFreespace(length, 0, asmSafe);
	}
	
	public int findFreespace(long freespaceStart, int startingLocation) {
		return findFreespace(freespaceStart, startingLocation, false);
	}
	
	public int findFreespace(long freespaceSize, int startingLocation, boolean asmSafe) {
		byte free = freeSpaceByte;
		 byte[] searching = new byte[(int) freespaceSize];
		 for(int i = 0; i < freespaceSize; i++)
			 searching[i] = free;
		 int numMatches = 0;
		 int freespace = -1;
		 for(int i = startingLocation; i < rom_bytes.length; i++) {
			 byte b = rom_bytes[i];
			 byte c = searching[numMatches];
			 if(b == c) {
				 numMatches++;
				 if(numMatches == searching.length - 1) {
					 freespace = i - searching.length + 2;
					 break;
				 }
			 } else numMatches = 0;
		 }
		 return freespace;
	}
	
	public void floodBytes(int offset, byte b, int length) {
		if(offset > 0x1FFFFFF)
			return;
		
		for(int i = offset; i < offset+length; i++)
			rom_bytes[i] = b;
	}
	
	public void repoint(int pOriginal, int pNew) {
		repoint(pOriginal, pNew, -1);
	}
	
	public void repoint(int pOriginal, int pNew, int numbertolookfor) {
		pOriginal |= 0x08000000;
		 byte[] searching = BitConverter.ReverseBytes(BitConverter.GetBytes(pOriginal));
		 int numMatches = 0;
		 int totalMatches = 0;
		 int offset;
		 for(int i = 0; i < rom_bytes.length; i++) {
			 byte b = rom_bytes[i];
			 byte c = searching[numMatches];
			 if(b == c) {
				 numMatches++;
				 if(numMatches == searching.length - 1) {
					 offset = i - searching.length + 2;
					 this.Seek(offset);
					 this.writePointer(pNew);
					 System.out.println(BitConverter.toHexString(offset));
					 totalMatches++;
					 if(totalMatches == numbertolookfor)
						 break;
					 numMatches = 0;
				 }
			 } else numMatches = 0;
		 }
		 System.out.println("Found " + totalMatches + " occurences of the pointer specified.");
	}
	
	public Object clone(){  
	    try{  
	        return super.clone();  
	    }catch(Exception e){ 
	        return null; 
	    }
	}
}