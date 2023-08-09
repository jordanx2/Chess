package application;

public class BlockCheck {
    private boolean blockCheck;
    private boolean[] blockCheckIndex;

    public BlockCheck(boolean blockCheck, boolean[] blockCheckIndex){
        this.blockCheck = blockCheck;
        this.blockCheckIndex = blockCheckIndex;
    }

    public boolean isBlockCheck() {
        return blockCheck;
    }

    public void setBlockCheck(boolean blockCheck) {
        this.blockCheck = blockCheck;
    }

    public boolean[] getBlockCheckIndex() {
        return blockCheckIndex;
    }

    public void setBlockCheckIndex(boolean[] blockCheckIndex) {
        this.blockCheckIndex = blockCheckIndex;
    }

}
