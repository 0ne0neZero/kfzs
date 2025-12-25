package com.wishare.finance.infrastructure.identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.UUID;

/**
 * 唯一标识工厂
 *
 * @Author dxclay
 * @Date 2022/12/29
 * @Version 1.0
 */
public abstract class IdentifierFactory {

    private static final Logger log = LoggerFactory.getLogger(IdentifierFactory.class);
    private static final IdentifierFactory INSTANCE;

    static {
        log.debug("Looking for IdentifierFactory implementation using the context class loader");
        IdentifierFactory factory = locateFactories(Thread.currentThread().getContextClassLoader(), "Context");
        if (factory == null) {
            log.debug("Looking for IdentifierFactory implementation using the IdentifierFactory class loader.");
            factory = locateFactories(IdentifierFactory.class.getClassLoader(), "IdentifierFactory");
        }

        if (factory == null) {
            factory = new DefaultIdentifierFactory();
        } else {
            log.info("Found custom IdentifierFactory implementation: {}", factory.getClass().getName());
        }

        INSTANCE = factory;
    }

    public IdentifierFactory() {
    }

    public static IdentifierFactory getInstance() {
        return INSTANCE;
    }

    /**
     * 生成UUID唯一key
     * @return
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取字符串唯一key
     * @return
     */
    public static String generateNSUUID() {
        return generateUUID().replaceAll("-", "");
    }

    /**
     * 获取字符串唯一key
     * @param key
     * @return
     */
    public String generateStrIdentifier(String key) {
        return generateIdentifier(key).toString();
    }

    /**
     * 获取Long唯一key
     * @param key
     * @return
     */
    public Long generateLongIdentifier(String key) {
        return Long.valueOf(generateIdentifier(key).toString());
    }

    /**
     * 获取Long唯一key
     * @return
     */
    public Long generateLongIdentifier() {
        return Long.valueOf(generateIdentifier().toString());
    }

    /**
     * 是否为 long uid
     * @param identifier
     * @return
     */
    public static boolean isLongIdentifier(String identifier){
        if (Objects.nonNull(identifier) && identifier.length() == 16){
            try {
                Long.valueOf(identifier);
                return true;
            }catch (Exception e){}
        }
        return false;
    }

    /**
     * 生成唯一key
     * @return
     */
    public abstract Object generateIdentifier();

    /**
     * 生成唯一id, id会在相同的key下唯一
     * @param key  id的唯一key
     * @return
     */
    public abstract Object generateIdentifier(String key);

    /**
     * 生成唯一id
     * @param key
     * @param digits
     * @return
     */
    public abstract Object generateIdentifier(String key, int digits);

    /**
     * 生成流水号
     *
     * @param key    流水号唯一键值
     * @param length 默认长度
     * @param prefix 前置字符 如： ZD202212290001 ZD
     * @return
     */
    public abstract String serialNumber(String key, String prefix, int length);

    /**
     * 加载唯一id工厂
     * @param classLoader
     * @param classLoaderName
     * @return
     */
    private static IdentifierFactory locateFactories(ClassLoader classLoader, String classLoaderName) {
        IdentifierFactory found = null;
        Iterator<IdentifierFactory> services = ServiceLoader.load(IdentifierFactory.class, classLoader).iterator();
        if (services.hasNext()) {
            log.debug("Found IdentifierFactory implementation using the {} Class Loader", classLoaderName);
            found = (IdentifierFactory)services.next();
            if (services.hasNext()) {
                log.warn("More than one IdentifierFactory implementation was found using the {} Class Loader. This may result in different selections being made after restart of the application.", classLoaderName);
            }
        }
        return found;
    }

}
