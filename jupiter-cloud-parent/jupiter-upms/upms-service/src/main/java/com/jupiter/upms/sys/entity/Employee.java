package com.jupiter.upms.sys.entity;

import java.util.Date;

import org.apache.ibatis.type.JdbcType;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jupiterframework.model.GenericPo;

import io.swagger.annotations.ApiModelProperty;


/**
 * 员工管理
 *
 * @author WUDUFENG
 * @since 2020-04-25
 */
@lombok.Data
@lombok.NoArgsConstructor
@lombok.EqualsAndHashCode(callSuper = false)
@TableName("sys_employee")
public class Employee extends GenericPo {

    private static final long serialVersionUID = 1L;

    /** 主键ID , 不要使用此值用于其它表关联 */
    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    @TableField(jdbcType = JdbcType.INTEGER)
    private Integer id;

    /** 租户编码 */
    @ApiModelProperty(value = "租户编码")
    @TableField("tenant_id")
    private Long tenantId;

    /** 机构 */
    @ApiModelProperty(value = "机构")
    @TableField(value = "organization_id", jdbcType = JdbcType.INTEGER)
    private Long organizationId;

    /** 用户ID */
    @ApiModelProperty(value = "用户ID")
    @TableField("user_id")
    private Long userId;

    /** 岗位 */
    @ApiModelProperty(value = "岗位")
    @TableField(value = "post_id", jdbcType = JdbcType.INTEGER)
    private Long postId;

    /** 工号 */
    @ApiModelProperty(value = "工号")
    @TableField("job_num")
    private String jobNum;

    /** 入职日期 */
    @ApiModelProperty(value = "入职日期")
    @TableField("enter_time")
    private Date enterTime;

    /** 离职日期 */
    @ApiModelProperty(value = "离职日期")
    @TableField("leave_time")
    private Date leaveTime;

    /** 状态:1-在职,2-离职 */
    @ApiModelProperty(value = "状态:1-在职,2-离职")
    private String status;


    public Employee(Long userId) {
        super();
        this.userId = userId;
    }

}
