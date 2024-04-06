package com.itcz.czword.user.service;

import com.itcz.czword.model.vo.common.ValidateCodeVo;

public interface ValidateCodeService {
    /**
     * 生成图像验证码
     * @return
     */
    ValidateCodeVo generateValidateCode();
}
