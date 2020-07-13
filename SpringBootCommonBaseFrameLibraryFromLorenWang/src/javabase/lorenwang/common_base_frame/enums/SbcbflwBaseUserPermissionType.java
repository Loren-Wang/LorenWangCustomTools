package javabase.lorenwang.common_base_frame.enums;

/**
 * 功能作用：
 * 创建时间：2020-07-13 11:29 上午
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：基类以及子类必须使用java文件格式，否则会导致无法使用父类变量
 *
 * @author 王亮（Loren wang）
 */

public class SbcbflwBaseUserPermissionType {
    /**
     * 超级管理员。该权限可以做任何操作，但是无法删除禁用同级的人，基本上是唯一的
     */
    public final static SbcbflwBaseUserPermissionType SUPER_ADMIN =
            new SbcbflwBaseUserPermissionType(-1, "超级管理员权限");

    /**
     * 管理员权限，该权限可以做任何操作，但是无法删除禁用同级的人
     */
    public final static SbcbflwBaseUserPermissionType ADMIN =
            new SbcbflwBaseUserPermissionType(0, "管理员权限");

    private final int type;
    private final String des;

    public SbcbflwBaseUserPermissionType(int type, String des) {
        this.type = type;
        this.des = des;
    }

    public int getType() {
        return type;
    }

    public String getDes() {
        return des;
    }
}
