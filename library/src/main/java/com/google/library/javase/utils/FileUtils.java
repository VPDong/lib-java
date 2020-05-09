package com.google.library.javase.utils;

import java.io.*;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.regex.Pattern;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileUtils {
	private FileUtils() throws Exception {
		throw new IllegalAccessException("can not to init instance for file utils");
	}
	
	public static File getRunPath(Class clazz) {
		ProtectionDomain domain = clazz.getProtectionDomain();
		return domain == null ? null : new File(domain.getCodeSource().getLocation().getPath()).getParentFile();
	}
	
	public static URL getResUrl(Class clazz, String name) {
		try {
			if (name.startsWith("/")) name = name.substring(1);
			URL url;
			ClassLoader loader = clazz.getClassLoader();
			if (loader != null) {
				url = loader.getResource(name);
				if (url != null) return url;
			}
			File path = getRunPath(clazz);
			if (path == null) return null;
			File resFile = new File(path.getParentFile().getParentFile(), "resources/main");
			url = (new File(resFile, name)).toURI().toURL();
			return url;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String getPrefix(String name) {
		try {
			int dot = name.lastIndexOf('.');
			return (dot == -1) ? name : name.substring(0, dot);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getSuffix(String name) {
		try {
			int dot = name.lastIndexOf('.');
			return (dot == -1) ? "" : name.substring(dot + 1);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getContent(File file) {
		if (file == null || !file.exists()) return null;
		
		try (FileChannel channel = new RandomAccessFile(file, "r").getChannel()) {
			byte[] content = new byte[(int) channel.size()];
			MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size()).load();
			if (buffer.remaining() > 0) {
				buffer.get(content, 0, buffer.remaining());
			}
			return new String(content);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void getContent(File file, FileReadListener listener) {
		if (file == null || !file.exists() || listener == null) return;
		
		try (LineNumberReader reader = new LineNumberReader(new FileReader(file))) {
			for (String line; (line = reader.readLine()) != null; ) {
				listener.onRead(line, reader.getLineNumber());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public interface FileReadListener {
		void onRead(String content, int lineNr);
	}
	
	public static boolean setContent(File dest, Object data, boolean append) {
		if (dest == null || data == null) return false;
		if (!append && !toDelete(dest, true)) return false;
		if (!toCreate(dest, false)) return false;
		
		if (data instanceof byte[]) {
			try (OutputStream os = new FileOutputStream(dest, append)) {
				os.write(((byte[]) data), 0, ((byte[]) data).length);
				os.flush();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				if (!append) toDelete(dest, true);
				return false;
			}
		} else if (data instanceof String) {
			try (OutputStream os = new FileOutputStream(dest, append)) {
				byte[] content = ((String) data).getBytes();
				os.write(content, 0, content.length);
				os.flush();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				if (!append) toDelete(dest, true);
				return false;
			}
		} else if (data instanceof InputStream) {
			try (OutputStream os = new FileOutputStream(dest, append)) {
				byte[] buffer = new byte[4096];
				for (int len; (len = ((InputStream) data).read(buffer, 0, 4096)) != -1; ) {
					os.write(buffer, 0, len);
					os.flush();
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				if (!append) toDelete(dest, true);
				return false;
			} finally {
				try {
					((InputStream) data).close();
				} catch (Exception ignored) {
					// ignored
				}
			}
		} else {
			throw new Error("can not support data type:" + data.getClass().getName());
		}
	}
	
	public static boolean toCreate(File file, boolean isDir) {
		try {
			if (file.exists()) {
				return (isDir && file.isDirectory()) || (!isDir && file.isFile());
			}
			
			if (isDir) {
				return file.mkdirs();
			} else {
				if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) return false;
				return file.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean toDelete(File file, boolean isSelf) {
		try {
			if (file == null || !file.exists()) return true;
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				if (files != null) {
					for (File tmp : files) {
						if (!toDelete(tmp, true)) return false;
					}
				}
				return !isSelf || file.delete();
			} else {
				return file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// 拷贝后不包含空文件夹
	public static boolean toCopy(File src, File dest, String... filters) {
		if (src == null || !src.exists()) return false;
		
		if (filters == null || filters.length == 0) {
			filters = new String[]{".*"};
		}
		
		if (src.isFile()) {
			boolean matched = false;
			for (String filter : filters) {
				if (filter == null) continue;
				if (matched = Pattern.matches(filter, src.getAbsolutePath())) {
					break;
				}
			}
			if (!matched) return false;
			
			toDelete(dest, true);
			if (!toCreate(dest, false)) return false;
			try (FileChannel inChannel = new FileInputStream(src).getChannel();
			     FileChannel outChannel = new FileOutputStream(dest).getChannel()) {
				inChannel.transferTo(0, inChannel.size(), outChannel);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				toDelete(dest, true);
				return false;
			}
		} else if (src.isDirectory()) {
			try {
				File[] files = src.listFiles();
				if (files == null || files.length == 0) return false;
				for (File file : files) {
					toCopy(file, new File(dest, file.getName()), filters);
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				toDelete(dest, true);
				return false;
			}
		}
		
		return false;
	}
	
	public static boolean toCopyRes(Class clazz, String name, File dest) {
		try {
			clazz = clazz == null ? FileUtils.class : clazz;
			name = name.startsWith("/") ? name.substring(1) : name;
			URL url = clazz.getClassLoader().getResource(name);
			if (url == null) {
				File codeDir = new File(clazz.getProtectionDomain().getCodeSource().getLocation().getPath());
				File resDir = new File(codeDir.getParentFile().getParentFile().getParentFile(), "resources/main");
				url = (new File(resDir, name)).toURI().toURL();
			}
			return setContent(dest, url.openStream(), false);
		} catch (Exception e) {
			e.printStackTrace();
			toDelete(dest, true);
			return false;
		}
	}
	
	public static boolean toMove(File src, File dest) {
		if (toCopy(src, dest)) {
			toDelete(src, true);
			return true;
		} else {
			return false;
		}
	}
	
	public static List<File> toFilter(File src, String... filters) {
		List<File> result = new LinkedList<>();
		
		if (!src.exists()) return result;
		
		if (filters == null || filters.length == 0) {
			filters = new String[]{".*"};
		}
		
		if (src.isDirectory()) {
			File[] files = src.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isDirectory()) {
						List<File> subs = toFilter(file, filters);
						result.addAll(subs);
					} else {
						boolean matched = false;
						for (String filter : filters) {
							if (filter == null) continue;
							if (matched = Pattern.matches(filter, file.getAbsolutePath())) {
								break;
							}
						}
						if (matched) result.add(file);
					}
				}
			}
		}
		
		return result;
	}
	
	// level为0到9，一般取3，7，9
	// 压缩后不包含空文件夹
	public static boolean toEnCompress(File src, File dest, int level, String... filters) {
		if (src == null || !src.exists()) return false;
		if (!toDelete(dest, true)) return false;
		if (!toCreate(dest, false)) return false;
		
		level = level < Deflater.DEFAULT_COMPRESSION ? Deflater.DEFAULT_COMPRESSION : level;
		level = level > Deflater.BEST_COMPRESSION ? Deflater.BEST_COMPRESSION : level;
		
		if (filters == null || filters.length == 0) {
			filters = new String[]{".*"};
		}
		
		try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(dest))) {
			zos.setLevel(level);
			byte[] buffer = new byte[4096];
			Stack<AbstractMap.SimpleEntry<String, File>> stack = new Stack<>();
			stack.push(new AbstractMap.SimpleEntry<>(src.getName(), src));
			while (!stack.empty()) {
				AbstractMap.SimpleEntry<String, File> topEntry = stack.pop();
				if (topEntry.getValue().isFile()) {
					boolean matched = false;
					for (String filter : filters) {
						if (filter == null) continue;
						if (matched = Pattern.matches(filter, topEntry.getValue().getAbsolutePath())) {
							break;
						}
					}
					if (matched) {
						try (FileInputStream in = new FileInputStream(topEntry.getValue())) {
							zos.putNextEntry(new ZipEntry(topEntry.getKey()));
							for (int len; (len = in.read(buffer)) != -1; ) {
								zos.write(buffer, 0, len);
								zos.flush();
							}
							zos.closeEntry();
						} catch (Exception e) {
							e.printStackTrace();
							stack.clear();
							return false;
						}
					}
				} else if (topEntry.getValue().isDirectory()) {
					try {
						File[] files = topEntry.getValue().listFiles();
						if (files != null && files.length > 0) {
							for (File file : files) {
								// 注意file.getName()前面需要带上父文件夹的名字加一斜杠,
								// 不然最后压缩包中就不能保留原来的文件结构(即所有文件都跑到压缩包根目录下)
								String entryName = topEntry.getKey() + "/" + file.getName();
								stack.push(new AbstractMap.SimpleEntry<>(entryName, file));
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						stack.clear();
						return false;
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			toDelete(dest, true);
			return false;
		}
	}
	
	public static boolean toUnCompress(File src, File dest) {
		if (src == null || !src.exists()) return false;
		if (!toCreate(dest, true)) return false;
		
		try (ZipFile zipFile = new ZipFile(src)) {
			for (Enumeration entries = zipFile.entries(); entries.hasMoreElements(); ) {
				ZipEntry entryItem = (ZipEntry) entries.nextElement();
				String outPath = dest.getAbsolutePath() + File.separator + entryItem.getName();
				outPath = outPath.replace("/", File.separator);
				if (new File(outPath).isDirectory()) continue;
				
				File outDir = new File(outPath.substring(0, outPath.lastIndexOf(File.separator)));
				if (!outDir.exists() && !outDir.mkdirs()) {
					toDelete(dest, true);
					return false;
				}
				
				try (InputStream in = zipFile.getInputStream(entryItem);
				     OutputStream os = new FileOutputStream(outPath, false)) {
					byte[] buf = new byte[4096];
					for (int len; (len = in.read(buf)) > 0; ) {
						os.write(buf, 0, len);
						os.flush();
					}
				} catch (Exception e) {
					toDelete(dest, true);
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			toDelete(dest, true);
			return false;
		}
	}
}
