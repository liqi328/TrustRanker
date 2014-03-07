package util;


import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/* �ļ�������
 * 
 * */
public class FileUtil {

	/* ���ָ��Ŀ¼�µ������ļ�
	 * @param dirName  Ŀ¼·��
	 * */
	public static File[] getFileList(String dirName){
		File file = new File(dirName);
		if(!file.exists() || !file.isDirectory()){
			System.out.println("The given directory is not exist, or not a directory.");
			return null;
		}
		File[] files = file.listFiles(
				new FileFilter(){
					@Override
					public boolean accept(File file){
						if(file.isDirectory()){
							return false;
						}
						return true;
					}
				});
		return files;
	}
	
	/* ���ָ��Ŀ¼��,���������������ļ�,
	 * @param dirName  Ŀ¼·��
	 * */
	public static File[] getFileList(String dirName, FileFilter filter){
		File file = new File(dirName);
		if(!file.exists() || !file.isDirectory()){
			System.out.println("The given directory is not exist, or not a directory.");
			return null;
		}
		File[] files = file.listFiles(filter);
		return files;
	}
	
	/* ���ָ��Ŀ¼�µ�������Ŀ¼
	 * @param dirName  Ŀ¼·��
	 * */
	public static File[] getDirectoryList(String dirName){
		File file = new File(dirName);
		if(!file.exists() || !file.isDirectory()){
			System.out.println("The given directory is not exist, or not a directory.");
			return null;
		}
		File[] files = file.listFiles(
				new FileFilter() {					
					@Override
					public boolean accept(File pathname) {
						if(pathname.isDirectory()){
							return true;
						}
						return false;
					}
				});
		return files;
	}
	
	public static void makeDir(File dir){
		if(!dir.exists()){
			dir.mkdir();
		}
	}
	
	public static boolean copy(String fileFrom, String fileTo) {
        return copy(new File(fileFrom), new File(fileTo));
    }
	
	public static boolean copy(File fileFrom, File fileTo) {
        try {
            FileInputStream in = new java.io.FileInputStream(fileFrom);
            FileOutputStream out = new FileOutputStream(fileTo);
            byte[] bt = new byte[1024];
            int count;
            while ((count = in.read(bt)) > 0) {
                out.write(bt, 0, count);
            }
            in.close();
            out.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
	
	
	public static void main(String[] args){
		String dirName = "./ppi_symbol/output";
		File[] dirs = FileUtil.getDirectoryList(dirName);
		for(File dir: dirs){
			System.out.println(dir);
		}
		System.out.println("---------------------------");
		File[] files = FileUtil.getFileList(dirName);
		for(File file: files){
			System.out.println(file);
		}
		
	}
}
