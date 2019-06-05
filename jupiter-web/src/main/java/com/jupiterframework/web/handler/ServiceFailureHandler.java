package com.jupiterframework.web.handler;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import javax.annotation.Resource;
import javax.security.cert.CertificateException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.NestedServletException;

import com.jupiterframework.constant.SysRespCodeEnum;
import com.jupiterframework.context.ServiceContext;
import com.jupiterframework.exception.ServiceException;
import com.jupiterframework.util.ExceptionUtils;
import com.jupiterframework.util.StringUtils;
import com.jupiterframework.web.annotation.MicroService;
import com.jupiterframework.web.converter.ExceptionConverter;
import com.jupiterframework.web.model.ServiceFailureResponse;
import com.netflix.client.ClientException;

import feign.FeignException;
import feign.RetryableException;
import feign.codec.DecodeException;


/**
 * 通用错误错误，返回错误码、错误信息
 * 
 * @author wudf
 *
 */
@RestController
@ControllerAdvice(annotations = MicroService.class) // 对于
													 // Interceptor（拦截器）层的异常，Spring
													 // 框架层的异常，就无能为力了
public class ServiceFailureHandler implements ErrorController {
	private static final Logger log = LoggerFactory.getLogger(ServiceFailureHandler.class.getSimpleName());

	@Resource
	private ExceptionConverter exceptionConverter;

	private void changeResponseStatusSuccess(HttpServletResponse response) {
		if (response != null)
			response.setStatus(HttpStatus.OK.value());
	}

	/** 参数校验异常处理 */
	@ExceptionHandler({ org.springframework.web.bind.MethodArgumentNotValidException.class })
	public ServiceFailureResponse validationError(HttpServletResponse response, MethodArgumentNotValidException e) {
		changeResponseStatusSuccess(response);
		log.debug("", e);
		return this.getErrMsg(e.getBindingResult());
	}

	/** 参数校验异常处理 */
	@ExceptionHandler({ BindException.class })
	public ServiceFailureResponse validationError(HttpServletResponse response, BindException e) {
		changeResponseStatusSuccess(response);
		log.debug("", e);
		return this.getErrMsg(e.getBindingResult());
	}

	/** 抛业务异常的处理 */
	@ExceptionHandler(ServiceException.class)
	public ServiceFailureResponse serviceException(HttpServletResponse response, ServiceException e) {
		changeResponseStatusSuccess(response);
		log.debug("", e);
		return exceptionConverter.buildServiceFailureResponse(e);
	}

	/** feignclient解析响应报文的处理 */
	@ExceptionHandler(DecodeException.class)
	public ServiceFailureResponse decodeException(HttpServletResponse response, DecodeException e) {
		changeResponseStatusSuccess(response);
		if (e.getCause() instanceof ServiceException) {
			// 已获取远程的完整错误码，所有不需要拼接当前系统的appId，直接返回
			log.debug("", e.getCause());
			ServiceException se = (ServiceException) e.getCause();
			return exceptionConverter.buildServiceFailureResponse(se);
		} else {
			log.error("解析响应报文出错", e);
			return exceptionConverter.buildServiceFailureResponse(SysRespCodeEnum.UNKNOW_ERR,
				ExceptionUtils.getCause(e).getMessage());
		}
	}

	/** 非法参数错误 */
	@ExceptionHandler({	MissingServletRequestParameterException.class, IllegalArgumentException.class,
						HttpMessageNotReadableException.class })
	public ServiceFailureResponse illegalArgument(HttpServletResponse response, Exception e) {
		log.error("非法的请求参数", e);
		changeResponseStatusSuccess(response);
		return exceptionConverter.buildServiceFailureResponse(SysRespCodeEnum.PARAMS_ERR,
			ExceptionUtils.getCause(e).getMessage());
	}

	/** 违反唯一索引 */
	@ExceptionHandler(DuplicateKeyException.class)
	public ServiceFailureResponse duplicateKeyError(HttpServletResponse response, Exception e) {
		log.error("{}违反唯一约束", ServiceContext.getServiceCode(), e);
		changeResponseStatusSuccess(response);
		return exceptionConverter.buildServiceFailureResponse(SysRespCodeEnum.DUPLICATE_KEY_ERR,
			ExceptionUtils.getCause(e).getMessage());
	}

	/** 操作数据库或者redis失败 */
	@ExceptionHandler(QueryTimeoutException.class)
	public ServiceFailureResponse queryTimeout(HttpServletResponse response, QueryTimeoutException e) {
		log.error("{} SQL超时", ServiceContext.getServiceCode(), e);
		changeResponseStatusSuccess(response);
		return exceptionConverter.buildServiceFailureResponse(SysRespCodeEnum.REQUEST_TIMEOUT, e.getMessage());
	}

	/** 操作数据库或者redis失败 */
	@ExceptionHandler(DataAccessException.class)
	public ServiceFailureResponse dataAccessError(HttpServletResponse response, Exception e) {
		log.error("{}数据访问异常", ServiceContext.getServiceCode(), e);
		changeResponseStatusSuccess(response);
		return exceptionConverter.buildServiceFailureResponse(SysRespCodeEnum.DATA_ACCESS_ERR,
			ExceptionUtils.getCause(e).getMessage());
	}

	/** 无可用服务 */
	@ExceptionHandler({ ClientException.class, FeignException.class })
	public ServiceFailureResponse clientException(HttpServletResponse response, Exception e) {
		log.error("{}远程服务调用失败", ServiceContext.getServiceCode(), e);
		changeResponseStatusSuccess(response);

		return exceptionConverter.buildServiceFailureResponse(SysRespCodeEnum.RESOURCE_INVALID,
			ExceptionUtils.getCause(e).getMessage());
	}

	/** session找不到/失效 */
	@ExceptionHandler({	javax.security.auth.login.AccountNotFoundException.class,
						javax.security.auth.login.AccountExpiredException.class })
	public ServiceFailureResponse accountNotFoundHandler(HttpServletResponse response, Exception e) {
		log.error("session 失效", e);
		changeResponseStatusSuccess(response);

		return exceptionConverter.buildServiceFailureResponse(SysRespCodeEnum.AUTH_SESSION_NOTFOUND,
			ExceptionUtils.getCause(e).getMessage());
	}

	/** 签名验证不通过 */
	@ExceptionHandler(CertificateException.class)
	public ServiceFailureResponse certificateExceptionHandler(HttpServletResponse response, Exception e) {
		log.error("签名验证不通过", e);
		changeResponseStatusSuccess(response);
		return exceptionConverter.buildServiceFailureResponse(SysRespCodeEnum.AUTH_SIGNATURE_NOTFOUND,
			ExceptionUtils.getCause(e).getMessage());

	}

	/** 无访问权限 */
	@ExceptionHandler(SecurityException.class)
	public ServiceFailureResponse securityExceptionHandler(HttpServletResponse response, Exception e) {
		log.error("{}无访问权限", ServiceContext.getServiceCode(), e);
		changeResponseStatusSuccess(response);
		return exceptionConverter.buildServiceFailureResponse(SysRespCodeEnum.AUTH_INVALID,
			ExceptionUtils.getCause(e).getMessage());
	}

	/** feignclient调用服务失败 */
	@ExceptionHandler({ RetryableException.class })
	public ServiceFailureResponse retryableException(HttpServletResponse response, Exception e) {
		log.error("{}远程服务调用失败", ServiceContext.getServiceCode(), e);
		changeResponseStatusSuccess(response);
		if (e.getCause() instanceof SocketTimeoutException) {
			return exceptionConverter.buildServiceFailureResponse(SysRespCodeEnum.REQUEST_TIMEOUT,
				ExceptionUtils.getCause(e).getMessage());
		} else {
			return exceptionConverter.buildServiceFailureResponse(SysRespCodeEnum.UNKNOW_ERR,
				ExceptionUtils.getCause(e).getMessage());
		}
	}

	/** 通用异常处理 */
	@ExceptionHandler(Exception.class)
	public ServiceFailureResponse commonHandleException(HttpServletResponse response, Throwable e) {

		try {
			throw ExceptionUtils.getCause(e);
		} catch (SocketTimeoutException e1) {
			log.error("", e);
			return exceptionConverter.buildServiceFailureResponse(SysRespCodeEnum.REQUEST_TIMEOUT,
				e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
		} catch (ConnectException e1) {
			// 拒绝连接 (Connection refused)
			log.error("", e);
			return exceptionConverter.buildServiceFailureResponse(SysRespCodeEnum.SERVICE_REJECT,
				e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
		} catch (ServiceException e1) {
			return this.serviceException(response, e1);
		} catch (DuplicateKeyException e1) {
			return this.duplicateKeyError(response, e1);
		} catch (QueryTimeoutException e1) {
			return this.queryTimeout(response, e1);
		} catch (DataAccessException e1) {
			return this.dataAccessError(response, e1);
		} catch (DecodeException e1) {
			return this.decodeException(response, e1);
		} catch (RetryableException e1) {
			return this.retryableException(response, e1);
		} catch (ClientException | FeignException e1) {
			return this.clientException(response, e1);
		} catch (RuntimeException e1) {
			// Hystrix熔断后只抛出RuntimeException
			if ("Hystrix circuit short-circuited and is OPEN".equals(e1.getMessage())) {
				return this.clientException(response, (Exception) e);
			}

			log.error("{} 系统异常", ServiceContext.getServiceCode(), e);
			changeResponseStatusSuccess(response);
			return exceptionConverter.buildServiceFailureResponse(SysRespCodeEnum.UNKNOW_ERR, e1.getMessage());
		} catch (Throwable e1) {
			log.error("{} 系统异常", ServiceContext.getServiceCode(), e);
			changeResponseStatusSuccess(response);
			return exceptionConverter.buildServiceFailureResponse(SysRespCodeEnum.UNKNOW_ERR, e1.getMessage());
		}

	}

	/** 参数检验失败,获取错误信息 */
	private ServiceFailureResponse getErrMsg(BindingResult br) {
		StringBuilder fieldErrorMsg = new StringBuilder();
		br.getFieldErrors().forEach(error -> fieldErrorMsg.append("[").append(error.getField()).append("] ")
			.append(error.getDefaultMessage()).append(" "));

		return exceptionConverter.buildServiceFailureResponse(SysRespCodeEnum.PARAMS_ERR.getCode(),
			fieldErrorMsg.toString());
	}

	@RequestMapping(value = "/error", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ServiceFailureResponse handleError(HttpServletRequest request, HttpServletResponse response) {
		// request.getRequestUri 为error , 此处获取的是原出错的uri
		String uri = (String) request.getAttribute("javax.servlet.error.request_uri");
		if (uri == null)
			uri = ServiceContext.getServiceCode();
		else {
			ServiceContext.setServiceCode(uri);
		}
		ServiceFailureResponse sr = null;

		int status = response.getStatus();
		changeResponseStatusSuccess(response);

		switch (status) {
		case 401:
			// 无权限
			sr = exceptionConverter.buildServiceFailureResponse(SysRespCodeEnum.AUTH_INVALID,
				HttpStatus.UNAUTHORIZED.toString());
			break;
		/** 接口不存在 */
		case 404:// HttpStatus.NOT_FOUND
			sr = exceptionConverter.buildServiceFailureResponse(SysRespCodeEnum.SERVICE_NOTFOUND,
				HttpStatus.NOT_FOUND.toString());
			break;

		/** 不支持的请求方式 */
		case 405:// HttpStatus.METHOD_NOT_ALLOWED
			sr = exceptionConverter.buildServiceFailureResponse(SysRespCodeEnum.METHOD_INVALID,
				HttpStatus.METHOD_NOT_ALLOWED.toString());
			break;
		default:
			Throwable t = (Throwable) request
				.getAttribute("org.springframework.boot.autoconfigure.web.DefaultErrorAttributes.ERROR");
			if (t == null) {
				t = (Throwable) request.getAttribute("javax.servlet.error.exception");
				if (t instanceof NestedServletException) {
					t = t.getCause();
				}
			}
			log.error("Request uri [{}] error ", uri);
			if (t != null)
				sr = this.commonHandleException(response, t);
			else
				sr = exceptionConverter.buildServiceFailureResponse(SysRespCodeEnum.UNKNOW_ERR, String.valueOf(status));
		}

		if (StringUtils.isBlank(sr.getUrl()))
			sr.setUrl(uri);
		return sr;
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}

}
