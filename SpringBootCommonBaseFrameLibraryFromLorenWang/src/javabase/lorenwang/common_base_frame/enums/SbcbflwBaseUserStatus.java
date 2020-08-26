package javabase.lorenwang.common_base_frame.enums;

/**
 * 功能作用：用户状态
 * 创建时间：2020-07-13 11:36 上午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：基类以及子类必须使用java文件格式，否则会导致无法使用父类变量
 *
 * @author 王亮（Loren wang）
 */

public class SbcbflwBaseUserStatus {
    /**
     * 启用
     */
    public final static SbcbflwBaseUserStatus ENABLE =
            new SbcbflwBaseUserStatus(0);

    /**
     * 禁用
     */
    public final static SbcbflwBaseUserStatus DISABLE =
            new SbcbflwBaseUserStatus(1);

    /**
     * 删除
     */
    public final static SbcbflwBaseUserStatus DELETE =
            new SbcbflwBaseUserStatus(2);
    private final int status;

    public SbcbflwBaseUserStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}

