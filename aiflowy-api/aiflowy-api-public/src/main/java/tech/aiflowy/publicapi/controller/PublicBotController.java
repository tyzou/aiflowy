package tech.aiflowy.publicapi.controller;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.aiflowy.ai.entity.Bot;
import tech.aiflowy.ai.service.BotService;
import tech.aiflowy.common.domain.Result;

import javax.annotation.Resource;

/**
 * bot 接口
 */
@RestController
@RequestMapping("/public-api/bot")
public class PublicBotController {

    @Resource
    private BotService botService;

    /**
     * 根据id或别名获取bot详情
     */
    @GetMapping("/getByIdOrAlias")
    public Result<Bot> getByIdOrAlias(@NotBlank(message = "key不能为空") String key) {
        return Result.ok(botService.getDetail(key));
    }
}
