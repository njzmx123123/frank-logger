package com.hbfintech.logger;

import com.hbfintech.logger.configuration.CustomConfigBean;
import org.slf4j.Logger;

import com.hbfintech.logger.pattern.EndClassNameOfConverter;
import com.hbfintech.logger.pattern.EndMethodNameOfConverterextends;

import ch.qos.logback.classic.PatternLayout;

/**
 * LoggerFactory
 * <p>
 * com.hbfintech.logger.LoggerFactory.getLogger(Class<?>)
 *
 * @author kaylves
 * @since 1.0
 */
public final class LoggerFactory
{
    private static final Logger logger = org.slf4j.LoggerFactory
            .getLogger(LoggerFactory.class);

    public static final int UN_INITIALIZED = 0;

    public static final int ON_GOING_INITIALIZATION = 1;

    public static final int FAILED_INITIALIZATION = 2;

    public static final int SUCCESSFUL_INITIALIZATION = 3;

    public static int INIT_STATS = UN_INITIALIZED;

    public static final String UNSUCCESSFUL_INIT_MSG = "初始化未成功";

    static
    {
        PatternLayout.defaultConverterMap.put("ec", EndClassNameOfConverter.class.getName());
        PatternLayout.defaultConverterMap.put("em", EndMethodNameOfConverterextends.class.getName());
    }

    private LoggerFactory()
    {
        super();
    }

    /**
     * <一句话功能简述>
     * <功能详细描述>
     *
     * @param clazz
     * @return
     */
    public static ILogger getLogger(Class<?> clazz)
    {
        return getILoggerFactory().getLogger(clazz.getName());
    }

    /**
     * 根据Class获取<code>CustomLogger</code>
     *
     * @param clazz
     * @return
     */
    public static CustomLogger getCustomLogger(Class<?> clazz)
    {
        return getILoggerFactory().getCustomLogger(clazz.getName());
    }

    public static ILoggerFactory getILoggerFactory()
    {
        if (INIT_STATS == UN_INITIALIZED)
        {
            synchronized (LoggerFactory.class)
            {
                if (INIT_STATS == UN_INITIALIZED)
                {
                    INIT_STATS = ON_GOING_INITIALIZATION;
                    performInitialization();
                }
            }
        }

        if (INIT_STATS == SUCCESSFUL_INITIALIZATION)
        {
            return StaticLoggerFactoryBinder.getSingleton().getILoggerFactory();
        }
        else
        {
            throw new IllegalStateException(UNSUCCESSFUL_INIT_MSG);
        }
    }

    private static void performInitialization()
    {
        try
        {
            StaticLoggerFactoryBinder.getSingleton().init();
        }
        catch (Exception e)
        {
            INIT_STATS = FAILED_INITIALIZATION;
            logger.error(e.getMessage(), e);
        }
        INIT_STATS = SUCCESSFUL_INITIALIZATION;
    }

    public static void performInitialization(CustomConfigBean customConfigBean)
    {
        try
        {
            StaticLoggerFactoryBinder.getSingleton().init(customConfigBean);
        }
        catch (Exception e)
        {
            INIT_STATS = FAILED_INITIALIZATION;
            logger.error(e.getMessage(), e);
        }
        INIT_STATS = SUCCESSFUL_INITIALIZATION;
    }
}
