import RcblwBaseComponent from "./RcblwBaseComponent";

/**
 * 功能作用：基础列表高阶组件
 * 初始注释时间： 2020/5/6 3:21 下午
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 *@author LorenWang（王亮）
 */
function RcblwBaseAntdListHighComponent(ChildComponent, config: RcblwBaseListHighComponentConfig) {
    return class extends RcblwBaseComponent {

        /**
         * 第一次渲染之后调用数据
         */
        componentDidMount = () => {
            this.state = {
                /**
                 * 数据列表
                 */
                dataList: [],
                /**
                 * 每页展示数量
                 */
                everyPage: 10,
                /**
                 * 当前页码
                 */
                currentPage: 1,
                /**
                 * 总数
                 */
                total: 0,
                /**
                 * 搜索条件列表
                 */
                searchCriteriaList: {},
                /**
                 * 仅记录的搜索条件列表,仅记录使用，在搜索时候做合并使用，但是在其他接口中不做任何处理
                 */
                recordSearchCriteriaList: {},
                /**
                 * 选中的数据key列表
                 */
                selectedRowKeys: [],
                /**
                 * 选中的数据实体列表
                 */
                selectedRows: [],
                /**
                 * 是否显示Modal弹窗
                 */
                showModal: false,
                /**
                 * 显示modal弹窗key
                 */
                showModalKey: "",
                /**
                 * 子类变量数据参数
                 */
                ...config && config.childStateParams,
            };
            if (config && config.isComponentDidMountRequestData) {
                this.searchDataList();
            }
        };

        /**
         * 搜索数据列表
         * @param values 搜索数据
         * @param isUseRecord 是否使用记录数据，默认搜索时使用记录数据
         * @param currentPage 当前页码，默认不传
         * @param everyPage 每页请求数量，默认不传
         */
        searchDataList = (values, isUseRecord = true, currentPage = this.state.currentPage, everyPage = this.state.everyPage) => {
            this.showLoading();
            //先合并记录数据
            let params = isUseRecord
                ? {
                    ...this.state.searchCriteriaList,
                    ...this.state.recordSearchCriteriaList,
                }
                : {...this.state.searchCriteriaList};
            //格式化搜索前数据
            if (config && config.formatSearchCriteriaListFun != null) {
                params = {...params, ...config.formatSearchCriteriaListFun(this, values)};
            } else {
                params = {...params, ...values};
            }
            config && config.apiRequest && config.apiRequest({
                ...params,
                currentPage,
                everyPage
            }, this)
                .then((res) => {
                    this.hideLoading();
                    const {result, data, everyPage, total, currentPage} =
                        res.result != null ? res.result : (res.data != null ? res.data : res);
                    this.setState(
                        {
                            dataList: RcblwConllectionUtil.addKeyAndResultList(
                                result && data,
                                config && config.dataListOptionsKey
                            ),
                            everyPage,
                            total: total,
                            currentPage,
                            searchCriteriaList: params,
                            selectedRowKeys: [],
                        },
                        config && config.optionsResponseDataFun != null
                            ? function () {
                                //默认情况处理完交由调用该高阶组件方处理数据
                                config && config.optionsResponseDataFun(this, res);
                            }
                            : null
                    );
                })
                .catch(() => {
                    this.hideLoading();
                });
        };

        /**
         * 选择改变回调
         * @param selectedRowKeys
         * @param selectedRows
         */
        rowSelectChange = (selectedRowKeys, selectedRows) => {
            this.setState({
                selectedRowKeys,
                selectedRows,
            });
        };

        /**
         * 更改分页
         * @param currentPage 目标页面
         * @param everyPage 每页数量
         */
        changePage = (currentPage, everyPage) => {
            this.searchDataList(
                this.state.searchCriteriaList,
                false,
                currentPage,
                everyPage
            );
        };

        /**
         * 获取表格筛选的selection
         */
        getTableRowSelection(type) {
            const {selectedRowKeys} = this.state;
            const rows = {
                selectedRowKeys,
                ...type,
            };
            return {
                onChange: this.rowSelectChange,
                ...rows,
            };
        }

        /**
         * 刷新数据列表，同时隐藏各个弹窗
         */
        refreshDataList = () => {
            //隐藏弹窗同时隐藏加载中
            config && config.onModalCancelClickFun != null
                ? config.onModalCancelClickFun(this)
                : this.setState({showModal: false});
            //刷新数据
            let currentPage = this.state.currentPage;
            let everyPage = this.state.everyPage;
            this.searchDataList({
                ...this.state.searchCriteriaList,
                currentPage,
                everyPage,
            });
        };

        /**
         * 选择的时间改变
         * @param values 改变值
         * @param isDefaultInitFinish 默认值初始化完成
         */
        selectTimeChange = (values, isDefaultInitFinish) => {
            const params = {...this.state.searchCriteriaList, ...values};
            this.setState({recordSearchCriteriaList: params});
            if (isDefaultInitFinish) {
                this.searchDataList(params);
            }
        };

        render() {
            return ChildComponent(this);
        }
    }
}
/**
 * 功能作用：基础列表高阶组件配置类
 * 初始注释时间： 2020/5/6 3:25 下午
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 *@author LorenWang（王亮）
 */
export class RcblwBaseListHighComponentConfig {

    /**
     * 请求体
     */
    apiRequest = null;

    /**
     * 是否在第一次渲染结束后发起请求，默认不发起
     */
    isComponentDidMountRequestData = false;

    /**
     * 数据列表操作时要读取的key值，为空时取index值
     */
    dataListOptionsKey = null;
    /**
     * 调用方传递参数，子类使用到的特殊参数
     */
    childStateParams = null;
    /**
     * 请求参数格式化，在发起请求时必调用方法，同时要接收格式化后的数据返回值
     */
    formatSearchCriteriaListFun: Function = null;
    /**
     *  modal取消弹窗点击
     */
    onModalCancelClickFun: Function = null;
    /**
     *  操作相应数据函数，当不为空时响应数据交由该函数处理
     *  也就是说在基类处理完数据之后再将实际的返回结果丢给子类处理
     */
    optionsResponseDataFun: Function = null;
}

export default RcblwBaseAntdListHighComponent

