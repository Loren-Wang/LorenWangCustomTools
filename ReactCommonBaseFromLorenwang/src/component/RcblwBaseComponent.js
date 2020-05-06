import React from 'react'
import './RcblwBaseComponent.css'

/**
 * 最大加载中数量
 */
let allowMaxLoadingCount = 1;

/**
 * 功能作用：所有页面必有的基础页面
 * 初始注释时间： 2019/8/29 0029 下午 17:15:30
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：该页面不使用renderChild方法因为传值有限制，同时也因为这些部分也是通用数据，都要显示，所以直接使用了render
 * 修改人：
 * 修改时间：
 * 备注：当前为为了语言初始化使用的
 */
export default class RcblwBaseComponent extends React.Component {
    componentDidMount() {
        this.state = {
            /**
             * 加载中是否显示
             */
            showLoadingStatus: false,
        }
        //初始化加载中配置
        if (this.props.showLoading != null) {
            if (this.props.showLoading !== this.state.showLoadingStatus) {
                this.setState({
                    showLoadingStatus: this.props.showLoading
                })
            }
        } else {
            this.showLoading()
        }
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        //更新加载中配置判断
        if (this.props.showLoading != null && this.props.showLoading !== this.state.showLoadingStatus) {
            this.setState({
                showLoadingStatus: this.props.showLoading
            })
        }
    }

    /**
     * 显示加载中
     * @param maxLoadingCount 最大加载框数量，当多接口同时请求时传递，当搜索时可不传，默认没有加载中
     * @param isAddOneLoading 是否添加一个加载中，默认调用一次增加一个加载中
     */
    showLoading(maxLoadingCount = 0, isAddOneLoading = true) {
        allowMaxLoadingCount = maxLoadingCount
        if (isAddOneLoading) {
            allowMaxLoadingCount += 1;
        }
        this.setState({
            showLoadingStatus: true
        })
    }

    /**
     * 隐藏加载中
     */
    hideLoading() {
        //调用一次隐藏一个加载中
        allowMaxLoadingCount -= 1;
        if (allowMaxLoadingCount <= 0) {
            this.setState({
                showLoadingStatus: false
            })
        }
    }

    /**
     * 获取基础类实例
     */
    getRcblwBaseContext() {
        return this
    }


}
