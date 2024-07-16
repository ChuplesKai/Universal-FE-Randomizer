package fedata.gcnwii.fe9;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fedata.gcnwii.fe9.scripting.ScriptInstruction;
import io.gcn.GCNCMBFileHandler;
import util.ByteArrayBuilder;
import util.DebugPrinter;
import util.YuneUtil;

public class FE9ScriptScene {
	int pointerOffset;
	Integer updatedPointerOffset;
	
	int sceneHeaderOffset;
	Integer updatedSceneHeaderOffset;
	
	int identifierOffset;
	Integer updatedIdentifierOffset;
	String identifierName;
	int scriptOffset;
	Integer updatedScriptOffset;
	int parentOffset;
	Integer updatedParentOffset;
	
	byte sceneKind;
	byte numberOfArgs;
	byte parameterCount;
	byte unknownByte;
	
	short sceneIndex;
	short varCount;
	
	short[] params;
	
	byte[] originalBytes;
	byte[] scriptBytes;
	byte[] updatedBytes;
	
	List<ScriptInstruction> originalInstructions;
	List<ScriptInstruction> instructions;
	List<ScriptInstruction> updatedInstructions;
	
	boolean wasModified = false;
	boolean hasChanges = false;
	
	GCNCMBFileHandler handler;
	
	public FE9ScriptScene(GCNCMBFileHandler handler, int pointerOffset) {
		this.handler = handler;
		this.pointerOffset = pointerOffset;
		
		byte[] headerOffset = handler.cmb_readBytesAtOffset(pointerOffset, 4);
		sceneHeaderOffset = (int)YuneUtil.longValueFromByteArray(headerOffset, true);
		
		byte[] identifier = handler.cmb_readBytesAtOffset(sceneHeaderOffset, 4);
		identifierOffset = (int)YuneUtil.longValueFromByteArray(identifier, true);
		if (identifierOffset != 0) {
			byte[] name = handler.cmb_readBytesUpToNextTerminator(identifierOffset);
			identifierName = YuneUtil.stringFromAsciiBytes(name);
		}
		
		byte[] script = handler.cmb_readBytesAtOffset(sceneHeaderOffset + 4, 4);
		scriptOffset = (int)YuneUtil.longValueFromByteArray(script, true);
		
		byte[] parent = handler.cmb_readBytesAtOffset(sceneHeaderOffset + 8, 4);
		parentOffset = (int)YuneUtil.longValueFromByteArray(parent, true);
		
		sceneKind = handler.cmb_readBytesAtOffset(sceneHeaderOffset + 0xC, 1)[0];
		numberOfArgs = handler.cmb_readBytesAtOffset(sceneHeaderOffset + 0xD, 1)[0];
		parameterCount = handler.cmb_readBytesAtOffset(sceneHeaderOffset + 0xE, 1)[0];
		unknownByte = handler.cmb_readBytesAtOffset(sceneHeaderOffset + 0xF, 1)[0];
		
		byte[] index = handler.cmb_readBytesAtOffset(sceneHeaderOffset + 0x10, 2);
		sceneIndex = (short)YuneUtil.longValueFromByteArray(index, true);
		
		byte[] vars = handler.cmb_readBytesAtOffset(sceneHeaderOffset + 0x12, 2);
		varCount = (short)YuneUtil.longValueFromByteArray(vars, true);
		
		params = new short[parameterCount];
		
		int parsingOffset = sceneHeaderOffset + 0x14;
		int paramIndex = 0;
		while (parsingOffset < scriptOffset && paramIndex < params.length) {
			byte[] parameter = handler.cmb_readBytesAtOffset(parsingOffset, 2);
			params[paramIndex++] = (short)YuneUtil.longValueFromByteArray(parameter, true);
			parsingOffset += 2;
		}
		
		byte[] nextHeaderOffset = handler.cmb_readBytesAtOffset(pointerOffset + 4, 4);
		int nextHeader = (int)YuneUtil.longValueFromByteArray(nextHeaderOffset, true);
		
		int scriptLength = nextHeader - scriptOffset;
		if (nextHeader == 0) { // This is the last script. Read to the end of the file.
			scriptLength = (int)handler.getFileLength() - scriptOffset;
		}
		
		scriptBytes = handler.cmb_readBytesAtOffset(scriptOffset, scriptLength);
		originalBytes = scriptBytes;
		
		instructions = FE9ScriptInterpreter.instructionsFromScript(this);
		originalInstructions = new ArrayList<ScriptInstruction>(instructions);
	}
	
	public int getPointerOffset() { return updatedPointerOffset != null ? updatedPointerOffset : pointerOffset; }
	public void setPointerOffset(int newOffset) { updatedPointerOffset = newOffset; } // This isn't written. It's just here for reference.
	
	public int getSceneHeaderOffset() { return updatedSceneHeaderOffset != null ? updatedSceneHeaderOffset : sceneHeaderOffset; }
	public void setSceneHeaderOffset(int newOffset) { updatedSceneHeaderOffset = newOffset; } // Also not written.
	
	public int getIdentifierOffset() { return updatedIdentifierOffset != null ? updatedIdentifierOffset : identifierOffset; }
	public void setIdentifierOffset(int newOffset) { 
		if (newOffset == identifierOffset) { return; }
		updatedIdentifierOffset = newOffset; 
		DebugPrinter.log(DebugPrinter.Key.FE9_CHAPTER_SCRIPT, "Changing identifierOffset from " + identifierOffset + " to " + newOffset + " for " + handler.getName());
		wasModified = true;
	}
	public String getIdentifierName() { return identifierName; }
	public int getScriptOffset() { return updatedScriptOffset != null ? updatedScriptOffset : scriptOffset; }
	public void setScriptOffset(int newOffset) { 
		if (newOffset == scriptOffset) { return; }
		updatedScriptOffset = newOffset; 
		DebugPrinter.log(DebugPrinter.Key.FE9_CHAPTER_SCRIPT, "Changing scriptOffset from " + scriptOffset + " to " + newOffset + " for " + handler.getName());
		wasModified = true; 
	}
	public int getParentOffset() { return updatedParentOffset != null ? updatedParentOffset : parentOffset; }
	public void setParentOffset(int newOffset) {
		if (newOffset == parentOffset) { return; }
		updatedParentOffset = newOffset; 
		DebugPrinter.log(DebugPrinter.Key.FE9_CHAPTER_SCRIPT, "Changing parentOffset from " + parentOffset + " to " + newOffset + " for " + handler.getName());
		wasModified = true;
	}
	
	public byte getSceneKind() { return sceneKind; }
	public byte getNumberOfArgs() { return numberOfArgs; }
	public byte getParameterCount() { return parameterCount; }
	
	public short getSceneIndex() { return sceneIndex; }
	public short getVarCount() { return varCount; }
	
	public short[] getParams() { return params; }
	
	public byte[] getOriginalScriptBytes() { return originalBytes; }
	public byte[] getScriptBytes() { return updatedBytes != null ? updatedBytes : scriptBytes; }
	public void setScriptBytes(byte[] newBytes) {
		updatedBytes = newBytes;
		updatedInstructions = FE9ScriptInterpreter.instructionsFromBytes(updatedBytes, handler);
		DebugPrinter.log(DebugPrinter.Key.FE9_CHAPTER_SCRIPT, "Changing scriptBytes for " + handler.getName());
		wasModified = true;
	}
	
	public List<ScriptInstruction> getOriginalInstructions() { return YuneUtil.createMutableCopy(originalInstructions); }
	public List<ScriptInstruction> getInstructions() { return YuneUtil.createMutableCopy(updatedInstructions != null ? updatedInstructions : instructions); }
	public void setInstructions(List<ScriptInstruction> newInstructions) {
		updatedInstructions = newInstructions;
		ByteArrayBuilder newBytes = new ByteArrayBuilder();
		updatedInstructions.stream().map(instruction -> { return instruction.rawBytes(); }).forEachOrdered(bytes -> { newBytes.appendBytes(bytes); });
		updatedBytes = newBytes.toByteArray();
		DebugPrinter.log(DebugPrinter.Key.FE9_CHAPTER_SCRIPT, "Changing instructions for " + handler.getName());
		DebugPrinter.log(DebugPrinter.Key.FE9_CHAPTER_SCRIPT, "Old Instructions: ");
		DebugPrinter.log(DebugPrinter.Key.FE9_CHAPTER_SCRIPT, String.join("\n", instructions.stream().map(instruction -> { return instruction.displayString(); }).collect(Collectors.toList())));
		DebugPrinter.log(DebugPrinter.Key.FE9_CHAPTER_SCRIPT, "New Instructions: ");
		DebugPrinter.log(DebugPrinter.Key.FE9_CHAPTER_SCRIPT, String.join("\n", updatedInstructions.stream().map(instruction -> { return instruction.displayString(); }).collect(Collectors.toList())));
		
		DebugPrinter.log(DebugPrinter.Key.FE9_CHAPTER_SCRIPT, "Old bytes: " + YuneUtil.displayStringForBytes(scriptBytes));
		DebugPrinter.log(DebugPrinter.Key.FE9_CHAPTER_SCRIPT, "New Bytes: " + YuneUtil.displayStringForBytes(updatedBytes));
		wasModified = true;
	}
	
	public GCNCMBFileHandler getHandler() { return handler; }
	
	public void resetScript() {
		updatedBytes = null;
		updatedInstructions = null;
		updatedPointerOffset = null;
		updatedSceneHeaderOffset = null;
		updatedIdentifierOffset = null;
		updatedScriptOffset = null;
		updatedParentOffset = null;
		wasModified = false;
	}
	
	public void commit() {
		if (!wasModified) { return; }
		
		if (updatedBytes != null) { 
			scriptBytes = updatedBytes;
		}
		updatedBytes = null;
		if (updatedInstructions != null) { 
			instructions = YuneUtil.createMutableCopy(updatedInstructions);
		}
		updatedInstructions = null;
		if (updatedPointerOffset != null) {
			pointerOffset = updatedPointerOffset;
		}
		updatedPointerOffset = null;
		if (updatedSceneHeaderOffset != null) {
			sceneHeaderOffset = updatedSceneHeaderOffset;
		}
		updatedSceneHeaderOffset = null;
		if (updatedIdentifierOffset != null) {
			identifierOffset = updatedIdentifierOffset;
		}
		updatedIdentifierOffset = null;
		if (updatedScriptOffset != null) {
			scriptOffset = updatedScriptOffset;
		}
		updatedScriptOffset = null;
		if (updatedParentOffset != null) {
			parentOffset = updatedParentOffset;
		}
		updatedParentOffset = null;
		wasModified = false;
		hasChanges = true;
	}
	
	public boolean wasModified() {
		return wasModified;
	}
	
	public boolean hasChanges() {
		return hasChanges;
	}
	
	public byte[] buildHeader() {
		// If there's any chages outstanding, we need to commit them now before we build the header.
		commit();
		
		// We basically have to reverse the steps we used to parse the header in the first place.
		ByteArrayBuilder builder = new ByteArrayBuilder();
		builder.appendBytes(YuneUtil.byteArrayFromLongValue(identifierOffset, true, 4));
		// Remember to append the string after the proper header.
		builder.appendBytes(YuneUtil.byteArrayFromLongValue(scriptOffset, true, 4));
		builder.appendBytes(YuneUtil.byteArrayFromLongValue(parentOffset, true, 4));
		
		builder.appendByte(sceneKind);
		builder.appendByte(numberOfArgs);
		builder.appendByte(parameterCount);
		builder.appendByte(unknownByte);
		
		builder.appendBytes(YuneUtil.byteArrayFromLongValue(sceneIndex, true, 2));
		builder.appendBytes(YuneUtil.byteArrayFromLongValue(varCount, true, 2));
		
		if (builder.getBytesWritten() != 0x14) {
			DebugPrinter.error(DebugPrinter.Key.FE9_CHAPTER_SCRIPT, "Incorrect number of bytes written for header.");
		}
		
		for (short param : params) {
			builder.appendBytes(YuneUtil.byteArrayFromLongValue(param, true, 2));
		}
		
		if (identifierOffset != 0) {
			if (builder.getBytesWritten() != identifierOffset - sceneHeaderOffset) {
				DebugPrinter.error(DebugPrinter.Key.FE9_CHAPTER_SCRIPT, "Script identifier written at the wrong place.");
			}
			builder.appendBytes(YuneUtil.shiftJISBytesFromString(identifierName));
			if (builder.getLastByteWritten() != 0) { builder.appendByte((byte)0); }
		}

		return builder.toByteArray();
	}
	
	public byte[] buildScriptBytes() {
		// This immediately follow the header or identifier strings.
		// We actually already built this.
		return getScriptBytes();
	}
}