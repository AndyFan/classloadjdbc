
/**
 * description 自定义classloader加载器当前线程的ContextClassLoader处理
 *
 * @author Cyber
 * <p> Created By 2022/11/22
 * @version 1.0
 */
public class JarClassLoaderSwapper {

    private ClassLoader storeClassLoader = null;

    private JarClassLoaderSwapper() {
    }

    public static JarClassLoaderSwapper newCurrentThreadClassLoaderSwapper() {
        return new JarClassLoaderSwapper();
    }

    /**
     * description 保存当前classLoader，并将当前线程的classLoader设置为所给classLoader
     *
     * @param classLoader
     * @return java.lang.ClassLoader
     * @author Cyber
     * <p> Created by 2022/11/22
     */
    public ClassLoader setCurrentThreadClassLoader(ClassLoader classLoader) {
        this.storeClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        return this.storeClassLoader;
    }

    /**
     * description 将当前线程的类加载器设置为保存的类加载
     *
     * @param
     * @return java.lang.ClassLoader
     * @author Cyber
     * <p> Created by 2022/11/22
     */
    public ClassLoader restoreCurrentThreadClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(this.storeClassLoader);
        return classLoader;
    }
}
