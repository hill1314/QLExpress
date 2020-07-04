package com.ql.util.express.instruction.opdata;

/**
 * 指令运行时参数集合
 */
public final class ArraySwap {
    OperateData[] arrays;
    int start;
    public int length;

    public void swap(OperateData[] aArrays, int aStart, int aLength) {
        this.arrays = aArrays;
        this.start = aStart;
        this.length = aLength;
    }

    public OperateData get(int i) {
        return this.arrays[i + start];
    }

}
