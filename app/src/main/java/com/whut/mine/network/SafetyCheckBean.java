package com.whut.mine.network;

import com.whut.mine.entity.CheckTableFirstIndex;
import com.whut.mine.entity.CheckTableSecondIndex;
import com.whut.mine.entity.SafetyCheckTable;

import java.util.List;

public class SafetyCheckBean {

    private List<SafetyCheckTable> checkTables;
    private List<CheckTableFirstIndex> firstIndices;
    private List<CheckTableSecondIndex> secondIndices;

    public SafetyCheckBean(List<SafetyCheckTable> checkTables, List<CheckTableFirstIndex> firstIndices
            , List<CheckTableSecondIndex> secondIndices) {
        this.checkTables = checkTables;
        this.firstIndices = firstIndices;
        this.secondIndices = secondIndices;
    }

    public List<SafetyCheckTable> getCheckTables() {
        return checkTables;
    }

    public List<CheckTableFirstIndex> getFirstIndices() {
        return firstIndices;
    }

    public List<CheckTableSecondIndex> getSecondIndices() {
        return secondIndices;
    }

}
