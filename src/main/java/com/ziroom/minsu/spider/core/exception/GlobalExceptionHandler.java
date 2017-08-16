package com.ziroom.minsu.spider.core.exception;

import com.ziroom.minsu.spider.core.result.Result;
import com.ziroom.minsu.spider.core.result.ResultCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>全局异常处理器</p>
 * <p>
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 *
 * @author jixd
 * @version 1.0
 * @Date Created in 2017年08月16日 11:20
 * @since 1.0
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String DEFAULT_ERROR_VIEW = "error";

    /**
     * 处理页面返回
     * @param req
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }

    /**
     * 处理json返回
     * @param req
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = ServiceException.class)
    @ResponseBody
    public Result<String> jsonErrorHandler(HttpServletRequest req, ServiceException e) throws Exception {
        Result<String> r = new Result<>();
        r.setMessage(e.getMessage());
        r.setCode(ResultCode.INTERNAL_SERVER_ERROR.code);
        r.setData(e.getMessage());
        return r;
    }

}
