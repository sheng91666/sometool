package com.sometool.util;

import net.sf.cglib.beans.BeanCopier;

import java.util.HashMap;
import java.util.Map;

public class BeanCopyUtils {
    private static final Map<String, BeanCopier> BEAN_COPIER_MAP = new HashMap<>();


    /**
     * Cglib BeanCopier
     * 注意Cglib的拷贝函数方向: Copy(src, target)
     * 入参(target, source)是为了和常见方式保持一致
     * 加入缓存, 方便提升多次拷贝时的使用效率
     *
     * @param target
     * @param source
     */
    public static void copyByCglibBeanCopier(Object target, Object source) {
        String beanCopierKey = genBeanCopierKey(source.getClass(), target.getClass());
        BeanCopier beanCopier;
        if (BEAN_COPIER_MAP.containsKey(beanCopierKey)) {
            beanCopier = BEAN_COPIER_MAP.get(beanCopierKey);
        } else {
            beanCopier = BeanCopier.create(source.getClass(), target.getClass(), false);
            BEAN_COPIER_MAP.put(beanCopierKey, beanCopier);
        }
        beanCopier.copy(source, target, null);
    }

    /**
     * 根据src和targe生成组合Key
     *
     * @param srcClazz
     * @param destClazz
     * @return
     */
    private static String genBeanCopierKey(Class<?> srcClazz, Class<?> destClazz) {
        return srcClazz.getName() + destClazz.getName();
    }
}
