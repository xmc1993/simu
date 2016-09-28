package cn.superid.webapp.controller.forms;

import java.util.List;

/**
 * Created by jizhenya on 16/9/26.
 */
public class InsertForm {

    private int position;
    private List<EasyBlock> blocks;


    public InsertForm(int position, List<EasyBlock> blocks) {
        this.position = position;
        this.blocks = blocks;
    }

    public InsertForm(){}

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<EasyBlock> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<EasyBlock> blocks) {
        this.blocks = blocks;
    }
}
