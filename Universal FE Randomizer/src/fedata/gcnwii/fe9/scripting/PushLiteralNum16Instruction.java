package fedata.gcnwii.fe9.scripting;

import io.gcn.GCNCMBFileHandler;
import util.YuneUtil;

public class PushLiteralNum16Instruction extends ScriptInstruction {

	int literal;
	
	public PushLiteralNum16Instruction(byte[] arg) {
		literal = (int)(YuneUtil.longValueFromByteArray(arg, false) & 0xFFFF);
	}
	
	public PushLiteralNum16Instruction(int literal) {
		this.literal = (literal & 0xFFFF);
	}
	
	@Override
	public String displayString() {
		return "PUSH_LITERAL_NUM_16 (0x" + Integer.toHexString(literal) + ")";
	}

	@Override
	public byte[] rawBytes() {
		return new byte[] {0x1A, (byte)((literal & 0xFF00) >> 8), (byte)(literal & 0xFF)};
	}

	@Override
	public byte opcode() {
		return 0x1A;
	}

	@Override
	public int numArgBytes() {
		return 2;
	}

	@Override
	public ScriptInstruction createWithArgs(byte[] args, GCNCMBFileHandler handler) {
		return new PushLiteralNum16Instruction(args);
	}

}
