package gavin.sensual.test

import android.os.Bundle
import gavin.sensual.R
import gavin.sensual.base.BaseViewModel
import gavin.sensual.base.BindingFragment
import gavin.sensual.databinding.FragMainBinding

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/9/22
 */
class TestFragment2 : BindingFragment<FragMainBinding, BaseViewModel>() {

    companion object {
        fun newInstance(): TestFragment2 = TestFragment2()
    }

    override fun getLayoutId(): Int = R.layout.frag_main

    override fun bindViewModel(savedInstanceState: Bundle?) {

    }

    override fun afterCreate(savedInstanceState: Bundle?) {
        context.toast("TestFragment2")
        pop()
    }
}
