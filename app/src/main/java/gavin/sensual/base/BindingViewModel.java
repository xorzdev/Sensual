package gavin.sensual.base;

import android.databinding.ViewDataBinding;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/5
 */
public abstract class BindingViewModel<B extends ViewDataBinding> {

    protected B binding;

    public BindingViewModel(B binding) {
        this.binding = binding;
    }
}
