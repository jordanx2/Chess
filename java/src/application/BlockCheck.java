package application;

public class BlockCheck {
    private boolean blockCheck;
    private int blockCheckIndex;

    public BlockCheck(boolean blockCheck, int blockCheckIndex){
        this.blockCheck = blockCheck;
        this.blockCheckIndex = blockCheckIndex;
    }

    public boolean isBlockCheck() {
        return blockCheck;
    }

    public void setBlockCheck(boolean blockCheck) {
        this.blockCheck = blockCheck;
    }

    public int getBlockCheckIndex() {
        return blockCheckIndex;
    }

    public void setBlockCheckIndex(int blockCheckIndex) {
        this.blockCheckIndex = blockCheckIndex;
    }

    

}
