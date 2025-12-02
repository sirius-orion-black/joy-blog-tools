package com.joy.config.Interceptor;


public class AuthInterceptor {
}

//
///**
// * 自定义认证拦截器
// * 实现HandlerInterceptor接口，用于拦截和处理HTTP请求
// * 主要功能：验证用户登录状态和权限
// */
//public class AuthInterceptor implements HandlerInterceptor {
//
//    /**
//     * 在控制器方法执行之前调用
//     * 用于进行权限验证和登录检查
//     * @param request HTTP请求对象
//     * @param response HTTP响应对象
//     * @param handler 处理器对象
//     * @return 是否允许继续执行控制器方法
//     */
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String requestURI = request.getRequestURI();
//
//        // 跳过公开路径的拦截
//        if (requestURI.startsWith("/api/auth") || requestURI.startsWith("/public")) {
//            return true; // 允许公开接口直接访问
//        }
//
//        // 检查用户是否登录（这里使用Session模拟）
//        Object user = request.getSession().getAttribute("user");
//        if (user == null) {
//            // 未登录，返回401状态码
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("{\"code\":401,\"message\":\"未登录或登录已过期\"}");
//            return false; // 阻止继续执行
//        }
//
//        // 检查用户权限（这里简单模拟权限验证）
//        if (requestURI.startsWith("/api/admin")) {
//            // 验证管理员权限
//            if (!"admin".equals(user)) {
//                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                response.getWriter().write("{\"code\":403,\"message\":\"无权限访问管理员接口\"}");
//                return false;
//            }
//
//            return true; // 允许继续执行控制器方法
//        }
//
//        /**
//         * 在控制器方法执行之后，视图渲染之前调用
//         * 可用于修改模型数据或添加通用响应头
//         */
//        @Override
//        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//            // 可以在这里添加通用的响应头或修改模型数据
//            response.setHeader("X-Request-Id", java.util.UUID.randomUUID().toString());
//        }
//
//        /**
//         * 在整个请求完成之后调用
//         * 用于资源清理和日志记录
//         */
//        @Override
//        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//            // 记录请求日志
//            System.out.println("请求完成: " + requestURI + " - 状态码: " + response.getStatus());
//        }
//    }

//
//public class WebMvcConfig implements WebMvcConfigurer {
//
//    /**
//     * 添加自定义拦截器到拦截器注册表
//     * @param registry 拦截器注册表
//     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 注册认证拦截器，并配置拦截规则
//        registry.addInterceptor(new AuthInterceptor())
//                .addPathPatterns("/**") // 拦截所有路径
//                .excludePathPatterns(
//                        "/api/auth/**",  // 排除认证接口
//                        "/public/**",   // 排除公开接口
//                        "/error",      // 排除错误页面
//                        "/favicon.ico" // 排除网站图标
//                );
//    }
//}