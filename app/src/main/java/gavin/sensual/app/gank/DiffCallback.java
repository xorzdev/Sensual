package gavin.sensual.app.gank;

import android.support.v7.util.DiffUtil;

import java.util.List;

import gavin.sensual.app.daily.Daily;

/**
 * 日报列表数据比对器
 *
 * @author gavin.xiong 2017/5/5
 */
class DiffCallback extends DiffUtil.Callback {

    private List<Welfare> oldList, newList;

    DiffCallback(List<Welfare> oldList, List<Welfare> newList) {
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
        return oldList.get(oldItemPosition).getId().equals(newList.get(newItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getUrl().equals(newList.get(newItemPosition).getUrl());
    }
}
