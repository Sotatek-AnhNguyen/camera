package com.example.nguye.cameravo.Model;

public class LinkImageTab {
    private int mSelection;
    private int mUnSelection;

    public LinkImageTab(int mSelection, int mUnSelection) {
        this.mSelection = mSelection;
        this.mUnSelection = mUnSelection;
    }

    public int getmSelection() {
        return mSelection;
    }

    public void setmSelection(int mSelection) {
        this.mSelection = mSelection;
    }

    public int getmUnSelection() {
        return mUnSelection;
    }

    public void setmUnSelection(int mUnSelection) {
        this.mUnSelection = mUnSelection;
    }
}
