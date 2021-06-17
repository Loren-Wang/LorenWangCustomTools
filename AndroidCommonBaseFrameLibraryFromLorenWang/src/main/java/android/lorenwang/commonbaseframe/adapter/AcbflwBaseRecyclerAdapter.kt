package android.lorenwang.commonbaseframe.adapter

import android.app.Activity
import android.lorenwang.commonbaseframe.R
import android.lorenwang.commonbaseframe.bean.AcbflwPageShowViewDataBean
import android.lorenwang.commonbaseframe.list.AcbflwBaseListDataOptionsDecorator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinbase.lorenwang.tools.extend.kttlwEmptyCheck

/**
 * Created by Machenike on 2018/4/4.
 *
 * 1、添加数据：添加数据，同时传递进入该数据要加载的布局id，以布局id作为type类型使用
 * 2、添加数据：使用baseType列表作为参数，此时不用再adapter中做转换
 * 3、清除并添加：清除所有数据，按照1注释的方法添加数据
 * 4、清除并添加：清除所有数据，按照2注释的方法添加数据
 * 5、显示空数据布局
 * 6、显示空数据布局以及提示
 * 7、清除所有数据
 * 8、添加所有数据
 */
/**
 * 功能作用：基础recycleview适配器，用来简化部分逻辑代码处理，子类只需要实现获取viewholder方法即可
 * 初始注释时间： 2019/12/11 16:18
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 * 1、添加数据：添加数据，同时传递进入该数据要加载的布局id，以布局id作为type类型使用
 * 2、添加数据：使用baseType列表作为参数，此时不用再adapter中做转换
 * 3、清除并添加：清除所有数据，按照1注释的方法添加数据
 * 4、清除并添加：清除所有数据，按照2注释的方法添加数据
 * 5、显示空数据布局
 * 6、清空数据列表
 */
abstract class AcbflwBaseRecyclerAdapter<T> : RecyclerView.Adapter<AcbflwBaseRecyclerViewHolder<T>>, AcbflwBaseListDataOptionsDecorator<T> {
    /**
     * 显示的数据列表，内部使用
     */
    var dataList: ArrayList<AcbflwBaseType<T>> = arrayListOf()
        private set

    /**
     * 页面实例
     */
    var activity: Activity

    /**
     * 结束的数据显示
     */
    var scrollEndData: AcbflwBaseType<T>? = null

    /**
     * 是否显示最大数量
     */
    private var showWhetherTheCycle = false

    /**
     * 单图显示的时候是否进行轮播,默认不进行轮播了
     */
    private var onlyShowCycle = false

    /**
     * 自动循环时最大item数量，因为Int.MAX_VALUE的值回导致和外部的viewpager产生冲突导致无法滑动，所以要设置一个尽量大的值
     */
    private val autoCycleMaxCount = 60000

    /**
     * 获取数据列表
     *
     * @return 数据列表
     */
    override val adapterDataList: ArrayList<AcbflwBaseType<T>>
        get() = dataList

    /**
     * 当前页码
     */
    var currentPageIndex: Int? = null
        private set

    /**
     * @param activity 页面实例
     */
    constructor(activity: Activity) : this(activity, false)

    /**
     * @param activity 页面实例
     * @param showWhetherTheCycle 是否轮播
     */
    constructor(activity: Activity, showWhetherTheCycle: Boolean) {
        this.activity = activity
        this.showWhetherTheCycle = showWhetherTheCycle
    }

    /**
     * @param activity 页面实例
     * @param showWhetherTheCycle 是否轮播
     * @param onlyShowCycle 单张的适合是否轮播
     */
    constructor(activity: Activity, showWhetherTheCycle: Boolean, onlyShowCycle: Boolean) {
        this.activity = activity
        this.showWhetherTheCycle = showWhetherTheCycle
        this.onlyShowCycle = onlyShowCycle
    }

    override fun getItemViewType(position: Int): Int {
        return dataList[position % dataList.size].layoutResId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcbflwBaseRecyclerViewHolder<T> {
        val itemView = LayoutInflater.from(activity).inflate(viewType, parent, false)
        return getListViewHolder(viewType, itemView)
    }

    override fun onBindViewHolder(holder: AcbflwBaseRecyclerViewHolder<T>, position: Int) {
        holder.adapter = this
        holder.setViewData(activity, dataList[position % if (dataList.size > 0) dataList.size else 1].bean, position)
    }

    override fun getItemCount(): Int { //如果是单张显示也要轮播或者本身也是要轮播情况下会进行轮播
        return if (showWhetherTheCycle || onlyShowCycle) {
            autoCycleMaxCount.coerceAtLeast(dataList.size)
        } else {
            dataList.size
        }
    }

    /**
     * 清空数据列表
     */
    override fun clear() {
        dataList.clear()
        notifyDataSetChanged()
    }

    /**
     * 添加数据和布局id
     *
     * @param list     数据列表
     * @param layoutId 布局id
     */
    override fun singleTypeLoad(list: List<T>?, layoutId: Int, haveMoreData: Boolean, showScrollEnd: Boolean) {
        list?.let {
            for (t in list) {
                if (t != null) {
                    dataList.add(AcbflwBaseType(layoutId, t))
                } else {
                    return
                }
            }
            loadFinish(showScrollEnd)
        }
    }

    /**
     * 添加转换后的basetype数据
     *
     * @param list basetype数据列表
     */
    override fun multiTypeLoad(list: List<AcbflwBaseType<T>>?, haveMoreData: Boolean, showScrollEnd: Boolean) {
        list?.let {
            this.dataList.addAll(list)
            loadFinish(showScrollEnd)
        }
    }

    /**
     * 清除旧数据并添加新数据和布局id
     *
     * @param list     数据列表
     * @param layoutId 布局id
     */
    override fun singleTypeRefresh(list: List<T>?, layoutId: Int, haveMoreData: Boolean, showScrollEnd: Boolean) {
        list?.let {
            dataList.clear()
            for (t in list) {
                dataList.add(AcbflwBaseType(layoutId, t))
            }
            loadFinish(showScrollEnd)
        }
    }

    /**
     * 清除旧数据并添加转换后的basetype数据
     *
     * @param list basetype数据列表
     */
    override fun multiTypeRefresh(list: List<AcbflwBaseType<T>>?, haveMoreData: Boolean, showScrollEnd: Boolean) {
        if (list != null && list.isNotEmpty()) {
            dataList.clear()
            dataList.addAll(list)
            loadFinish(showScrollEnd)
        }
    }

    /**
     * 设置显示内容
     */
    override fun showContentData() {
        dataList.clear()
        notifyDataSetChanged()
    }

    /**
     * 设置列表显示数据
     * @param data 列表通用数据加载
     * @param showScrollEnd 是否显示滑动结束
     */
    override fun setListShowData(data: AcbflwPageShowViewDataBean<AcbflwBaseType<T>>?, showScrollEnd: Boolean) {
        data.kttlwEmptyCheck({
            showEmptyView(R.layout.acbflw_empty_view_data, null, false)
        }, {
            currentPageIndex = it.currentPageIndex
            if (it.isFirstPageData) {
                if (!it.list.isNullOrEmpty()) {
                    showContentData()
                }
                multiTypeRefresh(it.list, !it.isLastPageData, showScrollEnd)
            } else {
                multiTypeLoad(it.list, !it.isLastPageData, showScrollEnd)
            }
        })
    }

    /**
     * 设置列表显示数据
     * @param data 列表通用数据加载
     * @param itemLayoutRes 列表item布局资源
     * @param showScrollEnd 是否显示滑动结束
     */
    override fun setListShowData(data: AcbflwPageShowViewDataBean<T>?, itemLayoutRes: Int, showScrollEnd: Boolean) {
        data.kttlwEmptyCheck({
            showEmptyView(R.layout.acbflw_empty_view_data, null, false)
        }, {
            currentPageIndex = it.currentPageIndex
            if (it.isFirstPageData) {
                if (!it.list.isNullOrEmpty()) {
                    showContentData()
                }
                singleTypeRefresh(it.list, itemLayoutRes, !it.isLastPageData, showScrollEnd)
            } else {
                singleTypeLoad(it.list, itemLayoutRes, !it.isLastPageData, showScrollEnd)
            }
        })
    }

    /**
     * 显示空视图
     *
     * @param layoutId 布局资源id
     * @param desc     空视图实例
     */
    override fun showEmptyView(layoutId: Int, desc: T?, haveMoreData: Boolean) {
        dataList.clear()
        dataList.add(AcbflwBaseType(layoutId, desc))
        notifyDataSetChanged()
    }

    /**
     * 加载结束处理
     */
    private fun loadFinish(showScrollEnd: Boolean) {
        if (showScrollEnd && scrollEndData != null) {
            var haveData = false
            for (item in dataList) {
                if (item == scrollEndData) {
                    haveData = true
                    break
                }
            }
            if (!haveData) {
                dataList.add(scrollEndData!!)
            }
        }
        notifyDataSetChanged()
    }
}
