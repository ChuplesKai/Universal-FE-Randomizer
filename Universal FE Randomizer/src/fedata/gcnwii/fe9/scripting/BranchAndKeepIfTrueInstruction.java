package fedata.gcnwii.fe9.scripting;

import io.gcn.GCNCMBFileHandler;
import util.YuneUtil;

public class BranchAndKeepIfTrueInstruction extends ScriptInstruction {

	int offset;
	
	public BranchAndKeepIfTrueInstruction(byte[] arg) {
		offset = (int)(YuneUtil.longValueFromByteArray(arg, false) & 0xFFFF);
	}
	
	public BranchAndKeepIfTrueInstruction(int offset) { // Branches to specified offset + 1
		this.offset = offset;
	}
	
	@Override
	public String displayString() {
		return "BRANCH_AND_KEEP_IF_TRUE (+ 0x" + Integer.toHexString(offset) + ")";
	}

	@Override
	public byte[] rawBytes() {
		return new byte[] {0x3C, (byte)((offset & 0xFF00) >> 8), (byte)(offset & 0xFF)};
	}

	@Override
	public byte opcode() {
		return 0x3C;
	}

	@Override
	public int numArgBytes() {
		return 2;
	}

	@Override
	public ScriptInstruction createWithArgs(byte[] args, GCNCMBFileHandler handler) {
		return new BranchAndKeepIfTrueInstruction(args);
	}

}
