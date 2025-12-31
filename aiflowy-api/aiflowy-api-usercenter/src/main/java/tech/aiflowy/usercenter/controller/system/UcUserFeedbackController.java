package tech.aiflowy.usercenter.controller.system;

import cn.dev33.satoken.annotation.SaIgnore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.aiflowy.common.annotation.UsePermission;
import tech.aiflowy.common.domain.Result;
import tech.aiflowy.common.web.controller.BaseCurdController;
import tech.aiflowy.common.web.jsonbody.JsonBody;
import tech.aiflowy.system.entity.SysUserFeedback;
import tech.aiflowy.system.service.SysUserFeedbackService;


/**
 *  控制层。
 *
 * @author 12076
 * @since 2025-12-30
 */
@RestController
@RequestMapping("/userCenter/sysUserFeedback")
@UsePermission(moduleName = "/api/v1/sysUserFeedback")
public class UcUserFeedbackController extends BaseCurdController<SysUserFeedbackService, SysUserFeedback> {

    @PostMapping("/save")
    @SaIgnore
    public Result<?> save(@JsonBody SysUserFeedback entity) {
        return super.save(entity);
    }

    public UcUserFeedbackController(SysUserFeedbackService service) {
        super(service);
    }

}