package com.github.hollykunge.security.task.controller;

import com.github.hollykunge.security.common.msg.BaseResponse;
import org.springframework.web.bind.annotation.*;

/**
 * @author fansq
 * @since 20-3-2
 * @deprecation 对应sourceLink.js
 */
@RestController
@RequestMapping(value = "/source_link")
public class SourceLinkController {

    /**
     * 删除关联资源
     * @param {*} sourceCode
     *   export function del(sourceCode) {
     *       return $http.delete('project/source_link/delete', {sourceCode: sourceCode});
     *   }
     */
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public BaseResponse delete(@RequestParam("sourceCode") String sourceCode){
        return new BaseResponse(200,"关联资源已删除！");
    }

}
