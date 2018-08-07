package IFTTT;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class SafeFile extends File{

	private File file;
	private String filepath,parentpath,name;
	private boolean iniwas;
	private long size,modifiedtime;
	
	public boolean initcon() {
		return iniwas;
	}
	
	public String prepath() {
		return filepath;
	}
	
	public String preparent() {
		return parentpath;
	}
	
	public String prename() {
		return name;
	}
	
	public long presize() {
		return size;
	}
	
	public long premodify() {
		return modifiedtime;
	}
	
	public void setInfo() {
		filepath = getPath();
		name = getName();
		parentpath = getParent();	
		size = length();
		modifiedtime = lastModified();
	}
	
	public SafeFile(String path_t) {
		super(path_t);
		file = new File(path_t);
		iniwas = isFile();
		filepath = getPath();
		name = getName();
		parentpath = getParent();	
		size = length();
		modifiedtime = lastModified();
	}
	
	
	
	public synchronized String getName() {
		try {
			return super.getName();
		} catch (Throwable t) {
			return null;
		}
	}
	
	public synchronized long length(){
		try{
			return super.length();
		}
		catch (Throwable t){
			return 0;
		}
	}
	
	public synchronized boolean exists(){
		try{
			return super.exists();
		}
		catch (Throwable t){
			return false;
		}
	}
	
	public synchronized String getParent() {
		try {
			return super.getParent();
		} catch (Throwable t) {
			return null;
		}
	}
	
	public synchronized long lastModified() {
		try {
			return super.lastModified();
		} catch (Throwable t) {
			return 0;
		}
	}
	
	public synchronized String getPath() {
		try {
			return super.getPath();
		} catch (Throwable t) {
			return null;
		}
	}
	
	public synchronized boolean isFile() {
		try {
			return super.isFile();
		} catch (Throwable t) {
			return false;
		}
	}
	
	public synchronized String[] list() {
		try {
			String[] pathList = super.list();
			for(int i=0; i<pathList.length; i++) {
				pathList[i] = getPath() + "\\" + pathList[i];
			}
			return pathList;
		} catch (Throwable t) {
			return null;
		}
	}
	
	public synchronized boolean delete()// 删除一个文件或者文件夹，与FILE类不同的是，删除文件夹可以什么都删除，而FILE类只能删除空文件夹
	{
		try {
			boolean flag;
			flag = true;
			File[] files;
			if (super.isFile()) {
				flag = super.delete();
			} else {
				files = super.listFiles();
				for (File t : files) {
					flag = flag & t.delete();
				}
				flag = super.delete();
			}
			return flag;
		} catch (Throwable t) {
			return false;
		}
	}



	public synchronized boolean mkdir() {// 创建一个文件夹
		try {
			return super.mkdir();
		} catch (Throwable t) {
			return false;
		}
	}

	public synchronized boolean mkdirs() {// 创建一个文件夹，会递归创建上层的没有出现的文件夹
		try {
			return super.mkdirs();
		} catch (Throwable t) {
			return false;
		}
	}

	// 测试线程中可用的方法
	public synchronized boolean renameTo(String newPath) {// 重命名一个文件去另一个路径
		try {
			File newFile = new File(newPath);
			return file.renameTo(newFile);
		} catch (Throwable t) {
			return false;
		}
	}

	 public synchronized void writeAppend(String s) {//	对文件追加写入字符串s				
		try {
			OutputStream f = new FileOutputStream(this.getPath(), true);	
			OutputStreamWriter writer = new OutputStreamWriter(f, "US-ASCII");
			writer.append(s);
			writer.append("\n");
			writer.close();
			f.close();
		} catch (Throwable t) {}
	 }
	 
	 public synchronized boolean createNewFile() throws IOException{ //创建一个文件
			try{
				boolean flag;
					flag = super.createNewFile();
				if (!flag){
					System.out.println("Create Error!");
				}
				return flag;
			}
			catch (Throwable t){
				return false;
			}
		}
}
