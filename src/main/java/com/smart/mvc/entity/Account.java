package com.smart.mvc.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * ACCOUNT
 * </p>
 *
 * @author xizhonghuai
 * @since 2022-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("account")
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 创建人
     */
    private Long createdBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdDate;

    /**
     * 更新人
     */
    private Long updatedBy;

    /**
     * 更新时间
     */
    private LocalDateTime updatedDate;

    /**
     * 账户名
     */
    private String name;

    private String nickname;

    private String threeAccount;

    /**
     * 密码
     */
    private String password;

    /**
     * 联系电话
     */
    private String tel;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像地址
     */
    private String headImgUrl;

    /**
     * 角色Id
     */
    private Long roleId;

    private Integer enabled;


}
