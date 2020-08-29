package fedata.gcnwii.fe9.scripting;

import io.gcn.GCNCMBFileHandler;

public class IsLessThanOrEqualInstruction extends ScriptInstruction {

	public IsLessThanOrEqualInstruction() {
		
	}
	
	@Override
	public String displayString() {
		return "IS_LESS_THAN_OR_EQUAL";
	}

	@Override
	public byte[] rawBytes() {
		return new byte[] {0x32};
	}

	@Override
	public byte opcode() {
		return 0x32;
	}

	@Override
	public int numArgBytes() {
		return 0;
	}

	@Override
	public ScriptInstruction createWithArgs(byte[] args, GCNCMBFileHandler handler) {
		return new IsLessThanOrEqualInstruction();
	}

}