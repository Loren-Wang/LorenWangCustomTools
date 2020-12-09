/**
 * 默认配置
 *
 * @param isComponentDidMountRequestData 是否在第一次渲染完成是请求数据
 * @param dataListOptionsKey  数据列表操作时要读取的key值，为空时取index值
 * @param childStateParams 调用方传递参数
 * @param formatSearchCriteriaList 请求参数格式化
 * @param onModalCancelClick modal取消弹窗点击
 * @param optionsResponseDataFun 操作相应数据函数，当不为空时响应数据交由该函数处理
 * @param checkSearchCriteriaList 对即将进行搜索的数据进行数据校验，返回false即为校验失败，直接return，返回true即为校验成功，继续下一步执行
 * @param showLoadingFun 显示加载中函数
 * @param hideLoadingFun 隐藏加载中函数
 */
const defaultConfig = {
    //是否在第一次渲染完成是请求数据
    isComponentDidMountRequestData: false,
    //数据列表操作时要读取的key值，为空时取index值
    dataListOptionsKey: null,
    //调用方传递参数
    childStateParams: null,
    //请求参数格式化
    formatSearchCriteriaList: null,
    //modal取消弹窗点击
    onModalCancelClick: null,
    //操作相应数据函数，当不为空时响应数据交由该函数处理
    optionsResponseDataFun: null,
    //对即将进行搜索的数据进行数据校验，返回false即为校验失败，直接return，返回true即为校验成功，继续下一步执行
    checkSearchCriteriaList: null,
    //显示加载中函数
    showLoadingFun: null,
    //隐藏加载中函数
    hideLoadingFun: null
}
/**
 * 功能作用：基础列表组件
 * 初始注释时间： 2020/7/22 6:02 下午
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 首先因为在分页的过程中如果没有点击搜索的话是不会对已修改的搜素条件进行搜素的，即搜索条件和修改搜索内容但未搜索使用两个字段
 * searchCriteriaList：当前已经搜索后的条件记录，只有在请求成功时更新记录
 * recordSearchCriteriaList：当前记录的搜索数据，该数据非全部搜索条件的记录，当前为各种时间修改后的记录体
 * childStateParams：接收除了基础数据结构之外的要使用的数据state；
 *
 * 流程1：首先在第一次渲染结束后需要通过config下的isComponentDidMountRequestData变量判断是否在第一次渲染结束后
 *       请求数据，因为在某些情况下请求数据是在某些参数初始化完成之后执行的，例如：请求需要在事件范围初始化之后执行
 *
 * 搜索流程：1、先使用变量记录上一次的搜索条件（searchCriteriaList）
 *          2、判断是否使用记录搜索数据（recordSearchCriteriaList），如果使用的话向上合并
 *          3、根据config.formatSearchCriteriaList判断是否要特别处理接收到的搜索条件（values）
 *          4、将前三步的到的数据结果依顺序进行合并的到最后的搜索条件
 *          5、通过apiRequest、分页请求参数、第四步得到的搜索条件进行数据请求；
 *          6、请求成功后更新相应的分页参数、数据源、当前搜索条件，请求失败什么都不更新；
 *          7、请求成功之后如果config.optionsResponseDataFun有单独的要处理响应数据的则回传数据处理；
 *
 * 刷新流程：1、隐藏记录弹窗；
 *          2、获取记录的分页数据；
 *          3、使用当前已经请求过的搜索条件（searchCriteriaList）搜索数据；
 *
 * 时间改变回调：1、按照已搜索条件、记录搜索条件、values顺序进行合并数据；
 *             2、如果初始化完成且需要进行请求的话则进行数据请求，否则只做记录；
 *
 * 修改人：
 * 修改时间：
 * 备注：
 * 1、如果在搜索中有时间组件使用，建议使用：FilterSearchRangeTime 配合使用
 *
 * @author LorenWang（王亮）
 * @param ChildComponent 渲染Dom树
 * @param apiRequest 数据列表请求function
 * @param config 配置文件
 */
export default function (ChildComponent, apiRequest, config = defaultConfig) {
    return class extends React.Component {
        state = {
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
        /**
         * 实际的操作数据
         */
        optionsConfig = {...defaultConfig, ...config}

        /**
         * 第一次渲染之后调用数据
         */
        componentDidMount = () => {
            if (this.optionsConfig && this.optionsConfig.isComponentDidMountRequestData) {
                this.searchDataList();
            }
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
         * 搜索数据列表
         * @param values 搜索数据
         * @param isUseRecord 是否使用记录数据，默认搜索时使用记录数据
         * @param currentPage 当前页码，默认不传
         * @param everyPage 每页请求数量，默认不传
         */
        searchDataList = (values,
                          isUseRecord = true,
                          currentPage = 1,
                          everyPage = 10
        ) => {
            config && config.showLoadingFun && config.showLoadingFun();
            //先合并记录数据
            let params = isUseRecord ? {
                ...this.state.searchCriteriaList,
                ...this.state.recordSearchCriteriaList,
            } : {...this.state.searchCriteriaList};
            //数据格式化处理逻辑
            if (this.optionsConfig && this.optionsConfig.formatSearchCriteriaList != null) {
                params = {...params, ...this.optionsConfig && this.optionsConfig.formatSearchCriteriaList(this, values)};
            } else {
                params = {...params, ...values};
            }
            //搜索条件校验处理
            if (this.optionsConfig && this.optionsConfig.checkSearchCriteriaList != null &&
                !this.optionsConfig.checkSearchCriteriaList(this, params)) {
                return
            }

            apiRequest({...params, currentPage, everyPage}, this)
                .then((res) => {
                    this.hideLoading();
                    const {result, everyPage, total, currentPage, ...dataSurplus} =
                        res.result != null ? res.result : res;
                    this.setState(
                        {
                            dataList: TableDataListUtil.addKeyAndResultList(
                                result,
                                this.optionsConfig && this.optionsConfig.dataListOptionsKey
                            ),
                            dataSurplus,
                            everyPage,
                            total: total,
                            currentPage,
                            searchCriteriaList: params,
                            selectedRowKeys: [],
                        },
                        this.optionsConfig && this.optionsConfig.optionsResponseDataFun != null
                            ? function () {
                                //默认情况处理完交由调用该高阶组件方处理数据
                                this.optionsConfig && this.optionsConfig.optionsResponseDataFun(this, res);
                            }
                            : null
                    );
                    if (this.state.tableDefaultListStatus) this.state.changeDataListFun(this)
                })
                .catch(() => {
                    this.hideLoading();
                });
        };

        /**
         * 刷新数据列表，同时隐藏各个弹窗
         */
        refreshDataList = () => {
            //隐藏弹窗同时隐藏加载中
            this.optionsConfig && this.optionsConfig.onModalCancelClick != null
                ? this.optionsConfig && this.optionsConfig.onModalCancelClick(this)
                : this.setState({showModal: false});
            //刷新数据
            let currentPage = this.state.currentPage;
            let everyPage = this.state.everyPage;
            this.searchDataList(
                this.state.searchCriteriaList,
                false,
                currentPage,
                everyPage,
            );
        };

        /**
         * 选择的时间改变
         * @param values 改变值
         * @param isDefaultInitFinish 默认值初始化完成
         * @param isRequest 是否进行请求，默认进行数据请求
         */
        selectTimeChange = (values, isDefaultInitFinish, isRequest = true) => {
            const params = {
                ...this.state.searchCriteriaList,
                ...this.state.recordSearchCriteriaList,
                ...values
            };
            this.setState({recordSearchCriteriaList: params});
            if (isDefaultInitFinish && isRequest) {
                this.searchDataList(params);
            }
        };

        render() {
            return (
                <div>{ChildComponent(this)}</div>
            );
        }
    };
}
