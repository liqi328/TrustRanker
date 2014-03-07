package util;


import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/* 文件工具类
 * 
 * */
public class FileUtil {

	/* 获得指定目录下的所有文件
	 * @param dirName  目录路径
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
	
	/* 获得指定目录下,满足条件的所有文件,
	 * @param dirName  目录路径
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
	
	/* 获得指定目录下的所有子目录
	 * @param dirName  目录路径
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
