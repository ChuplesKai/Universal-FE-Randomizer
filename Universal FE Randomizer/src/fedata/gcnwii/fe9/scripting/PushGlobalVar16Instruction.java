package fedata.gcnwii.fe9.scripting;

import io.gcn.GCNCMBFileHandler;
import util.YuneUtil;

public class PushGlobalVar16Instruction extends ScriptInstruction {

	int globalVariableNumber;
	
	public PushGlobalVar16Instruction(byte[] arg) {
		globalVariableNumber = (int)(YuneUtil.longValueFromByteArray(arg, false) & 0xFFFF);
	}
	
	public PushGlobalVar16Instruction(int globalVariableNumber) {
		this.globalVariableNumber = (globalVariableNumber & 0xFFFF);
	}
	
	@Override
	public String displayString() {
		return "PUSH_GLOBAL_VAR_16 (0x" + Integer.toHexString(globalVariableNumber) + ")";
	}

	@Override
	public byte[] rawBytes() {
		return new byte[] {0xE, (byte)((globalVariableNumber & 0xFF00) >> 8), (byte)(globalVariableNumber & 0xFF)};
	}

	@Override
	public byte opcode() {
		return 0xE;
	}

	@Override
	public int numArgBytes() {
		return 2;
	}

	@Override
	public ScriptInstruction createWithArgs(byte[] args, GCNCMBFileHandler handler) {
		return new PushGlobalVar16Instruction(args);
	}

}
