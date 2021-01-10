import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 
 */

/**
 * @author 
 *
 */
public class Work20210106 extends ClassLoader {

	private String classFilePath;
	
	Work20210106() {	
	}
	
	Work20210106(String classFilePath) {
		this.classFilePath = classFilePath;
	}
	
	/**
	 * 拼接字节数组
	 */
    private byte[] mergeBytes(byte[] bt1, byte[] bt2, int length) {
        byte[] bt3 = new byte[bt1.length + length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, length);
        return bt3;
    }
        
	/**
	 * 从文件读取字节
	 */
	private byte[] getClassDefFromFile(String filePath) {
		byte[] classBytes = new byte[0];
        InputStream in = null;
        try {
            byte[] tempBytes = new byte[1024];
            int byteRead = 0;
            in = new FileInputStream(filePath);
            while ((byteRead = in.read(tempBytes)) != -1) {
            	classBytes = this.mergeBytes(classBytes, tempBytes, byteRead);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                	e1.printStackTrace();
                }
            }
        }

        return classBytes;
	}
	
	/**
	 * byte数组做255-x转换
	 */
    private byte[] convertBytes(byte[] inputBytes) {
    	if(null == inputBytes || 0 == inputBytes.length) {
    		return inputBytes;
    	}
    	for(int i = 0; i < inputBytes.length; i++) {
    		int a = 255 - inputBytes[i];
    		inputBytes[i] = (byte)(a & 0xFF);
    	}
    	return inputBytes;
    }

	/**
	 * 加载类方法
	 */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		if(null == name || 0 == name.length()) {
			return null;
		}
		byte[] classDefBytes = this.getClassDefFromFile(this.classFilePath);
		classDefBytes = this.convertBytes(classDefBytes);
		return defineClass(name, classDefBytes, 0, classDefBytes.length);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Work20210106 work = new Work20210106("H:\\dev\\Java2021\\material\\2021_01_09\\Hello\\Hello.xlass");
		try {
			Class<?> clazz = work.findClass("Hello");
			Method method = clazz.getMethod("hello");
			method.invoke(clazz.newInstance());
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException 
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException 
				| InstantiationException e) {
			e.printStackTrace();
		}
		
		return;
	}

}
