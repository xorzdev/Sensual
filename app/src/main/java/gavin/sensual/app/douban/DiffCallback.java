package gavin.sensual.app.douban;

import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * 日报列表数据比对器
 *
 * @author gavin.xiong 2017/5/5
 */
class DiffCallback extends DiffUtil.Callback {

    private List<Image> oldList, newList;

    DiffCallback(List<Image> oldList, List<Image> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getUrl().equals(newList.get(newItemPosition).getUrl());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return true;
    }
}