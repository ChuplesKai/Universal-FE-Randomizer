package fedata.general;

public class FEBase {

    public enum GameType {
        UNKNOWN, FE4, FE6, FE7, FE8, FE9;

        public boolean isGBA() {
            switch (this) {
                case FE6:
                case FE7:
                case FE8:
                    return true;
                default:
                    return false;
            }
        }

        public boolean isSFC() {
            return this == FE4;
        }

        public boolean isGCN() {
            return this == FE9;
        }

        public boolean hasSTRMAGSplit() {
            switch (this) {
                case FE4:
                case FE9:
                    return true;
                default:
                    return false;
            }
        }

        public boolean hasEnglishPatch() {
            switch (this) {
                case FE4:
                case FE6:
                    return true;
                default:
                    return false;
            }
        }

        public String[] getFileExtensions() {
            switch (this) {
                case FE4:
                    return new String[]{"*.smc"};
                case FE9:
                    return new String[]{"*.iso"};
                case FE6:
                case FE7:
                case FE8:
                    return new String[]{"*.gba"};
                default:
                    throw new UnsupportedOperationException("unkown game type " + this.name());
            }
        }
    }
}
